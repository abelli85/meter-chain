package org.fisco.bcos

import com.alibaba.fastjson.JSON
import org.fisco.bcos.constants.GasConstants
import org.fisco.bcos.model.Meter
import org.fisco.bcos.model.MeterBatch
import org.fisco.bcos.temp.HelloWorld
import org.fisco.bcos.temp.UserMeter
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.protocol.Web3j
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider
import org.joda.time.LocalDateTime
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.util.*

class ContractTest : BaseTest() {
    @Autowired
    private val web3j: Web3j? = null

    @Autowired
    private val credentials: Credentials? = null

    @Autowired
    private val mongoTemplate: MongoTemplate? = null

    companion object {
        // 接收来自厂家的检定委托单
        private val batch = MeterBatch().apply {
            batchId = "202006" + (1001..9999).random().toString()
            meterList = listOf("0221520012000123", "0221520012000301")
            manufacturer = "宁波水表"
            verifierName = "abel"
            deliverDate = LocalDateTime(2020, 6, 1, 9, 20).toDate()
            validDate = LocalDateTime(2026, 5, 31, 0, 0).toDate()
        }

        private val lgr = LoggerFactory.getLogger(ContractTest::class.java)
    }

    /**
     * prepare test environment.
     */
    @Before
    fun setUp() {
        mongoTemplate!!.remove(Query.query(Criteria.where(MeterBatch.KEY_BATCH_ID).`is`(batch.batchId)), MeterBatch::class.java)
        lgr.info("clean meter-batch to avoid confliction: {}", batch.batchId)
    }

    /**
     * clean test environment.
     */
    @After
    fun tearDown() {
    }

    /**
     * 测试水表检定结果的上链、添加检定结果、结单、查询。
     */
    @Test
    fun testUserMeter() {
        mongoTemplate!!.save(batch)
        batch.meterList?.forEach {
            mongoTemplate!!.save(Meter().apply {
                meterId = it
                batchId = batch.batchId
            })
        }
        lgr.info("新到检定委托单: {}", JSON.toJSONString(batch, true))

        // 委托单开始上链
        val um = UserMeter.deploy(web3j, credentials,
                StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                "宁波水表").send()
        lgr.info("UserMeter contract address: {}", um.contractAddress)
        batch.apply {
            verifierAddress = um.verifier().send().toString()
            contractAddress = um.contractAddress
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("batchId").`is`(batch.batchId)),
                Update.update("verifierAddress", batch.verifierAddress)
                        .addToSet("contractAddress", batch.contractAddress),
                MeterBatch::class.java)

        // 空委托单不允许完成
        um.finish().send()
        lgr.warn("空委托单不允许完成")

        um.verify(batch.meterList!![0].toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        Thread.sleep(100)
        um.verify(batch.meterList!![1].toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        lgr.info("2 meters verified.")

        // 完成委托单
        um.finish().send()
        batch.apply {
            finishDate = Date()
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("batchId").`is`(batch.batchId)),
                Update.update("finishDate", batch.finishDate),
                MeterBatch::class.java
        )
        lgr.info("完成委托单")

        // 立即查询检定结果
        kotlin.run {
            val r1 = um.getInfo(batch.meterList!![0].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![0],
                    r1.value1, r1.value2, r1.value3, r1.value4)
            Assert.assertEquals("PASS", r1.value3)

            val r2 = um.getInfo(batch.meterList!![1].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![1],
                    r2.value1, r2.value2, r2.value3, r2.value4)
            Assert.assertEquals("PASS", r2.value3)
        }

        // 根据合约地址查询水表检定结果.
        val batchList = mongoTemplate.findAll(MeterBatch::class.java)
        Assert.assertTrue(batchList.size > 0)
        batchList.first().also {
            lgr.info("retrieving contract for the meter-batch: {}", JSON.toJSONString(it, true))

            val contract = UserMeter.load(it.contractAddress, web3j, credentials,
                    StaticGasProvider(
                            GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))

            val r1 = contract.getInfo(batch.meterList!![0].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![0],
                    r1.value1, r1.value2, r1.value3, r1.value4)
            Assert.assertEquals("PASS", r1.value3)

            val r2 = contract.getInfo(batch.meterList!![1].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![1],
                    r2.value1, r2.value2, r2.value3, r2.value4)
            Assert.assertEquals("PASS", r2.value3)
        }
    }

    @Test
    fun testDeployAndCallHelloWorld() {
        // deploy contract
        val helloWorld: HelloWorld = HelloWorld.deploy(
                web3j,
                credentials,
                StaticGasProvider(
                        GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
                .send()

        kotlin.run {
            lgr.info("HelloWorld address is: {}", helloWorld.contractAddress)
            // call set function
            helloWorld.set("Hello, World!").send()
            // call get function
            val result = helloWorld.get().send()
            lgr.info(result)
            Assert.assertTrue("Hello, World!" == result)
        }
    }
}