package org.fisco.bcos

import com.alibaba.fastjson.JSON
import org.fisco.bcos.constants.GasConstants
import org.fisco.bcos.model.Meter
import org.fisco.bcos.model.MeterBatch
import org.fisco.bcos.model.PublicAddressConf
import org.fisco.bcos.solidity.HelloWorld
import org.fisco.bcos.solidity.UserMeter
import org.fisco.bcos.util.Tools
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.crypto.Sign
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
import kotlin.test.assertEquals

class ContractTest : BaseTest() {
    @Autowired
    private val web3j: Web3j? = null

    @Autowired
    private val credentials: Credentials? = null

    @Autowired
    private val mongoTemplate: MongoTemplate? = null

    @Autowired
    var addressConf: PublicAddressConf? = null

    companion object {
        // 在本地库中查询合约
        const val KEY_BATCH_ID = "batchId"
        const val KEY_FINISH_DATE = "finishDate"
        const val KEY_REPORT_NAME = "reportName"

        const val FILE_AUDTIOR = "/user.jks"
        const val FILE_PUBLISHER = "/depositor.jks"
        const val FILE_ARBITRATOR = "/arbitrator.jks"

        // 接收来自厂家的检定委托单
        private val batch = MeterBatch().apply {
            batchId = "202006" + (1001..9999).random().toString()
            meterList = listOf("0221520012000123", "0221520012000301")
            manufacturer = "宁波水表"
            verifierName = "abel"
            deliverDate = LocalDateTime(2020, 6, 1, 9, 20).toDate()
            validDate = LocalDateTime(2026, 5, 31, 0, 0).toDate()
        }

        // 接收来自厂家的检定委托单, 并签名检定报告
        private val rpt = MeterBatch().apply {
            batchId = "2020050136"
            meterList = listOf("0228020012000133")
            manufacturer = "江苏沪仪"
            reportName = "HYLWGB-20210128.pdf"
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
        mongoTemplate!!.remove(Query.query(Criteria.where(MeterBatch.KEY_BATCH_ID).`is`(batch.batchId)), MeterBatch::class.java)
        lgr.info("clean meter-batch to avoid confliction: {}", batch.batchId)

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
                batch.manufacturer, batch.batchId, "罗工", "2026-6-30").send()
        lgr.info("UserMeter contract address: {}", um.contractAddress)
        batch.apply {
            verifierAddress = um.verifier().send().toString()
            contractAddress = um.contractAddress
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where(KEY_BATCH_ID).`is`(batch.batchId)),
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
                Query.query(Criteria.where(KEY_BATCH_ID).`is`(batch.batchId)),
                Update.update("finishDate", batch.finishDate),
                MeterBatch::class.java
        )
        lgr.info("完成委托单")

        // 立即查询检定结果
        kotlin.run {
            val r1 = um.getInfo(batch.meterList!![0].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 委托单号: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![0],
                    r1.value1, r1.value2, r1.value3, r1.value4, r1.value5)
            Assert.assertEquals("PASS", r1.value4)

            val r2 = um.getInfo(batch.meterList!![1].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 委托单号: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![1],
                    r2.value1, r2.value2, r2.value3, r2.value4, r2.value5)
            Assert.assertEquals("PASS", r2.value4)
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
            lgr.info("检定结果 - 表码: {}, 委托单号: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![0],
                    r1.value1, r1.value2, r1.value3, r1.value4, r1.value5)
            Assert.assertEquals("PASS", r1.value4)

            val r2 = contract.getInfo(batch.meterList!![1].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 委托单号: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![1],
                    r2.value1, r2.value2, r2.value3, r2.value4, r2.value5)
            Assert.assertEquals("PASS", r2.value4)
        }
    }

    /**
     * 测试水表检定报告的上链、三方签名、查询。
     */
    @Test
    fun testBuildReport() {
        mongoTemplate!!.remove(Query.query(Criteria.where(MeterBatch.KEY_BATCH_ID).`is`(rpt.batchId)), MeterBatch::class.java)
        lgr.info("clean batch-report to avoid confliction: {}", rpt.batchId)

        mongoTemplate!!.save(rpt)
        batch.meterList?.forEach {
            mongoTemplate!!.save(Meter().apply {
                meterId = it
                batchId = rpt.batchId
            })
        }
        lgr.info("新到检定报告: {}", JSON.toJSONString(rpt, true))

        // 委托单开始上链
        val um = UserMeter.deploy(web3j, credentials,
                StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                rpt.manufacturer, rpt.batchId, "罗工", "2022-5-31").send()
        lgr.info("UserMeter contract address: {}", um.contractAddress)
        rpt.apply {
            verifierAddress = um.verifier().send().toString()
            contractAddress = um.contractAddress
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where(KEY_BATCH_ID).`is`(rpt.batchId)),
                Update.update("verifierAddress", rpt.verifierAddress)
                        .addToSet("contractAddress", rpt.contractAddress),
                MeterBatch::class.java)

        // 空委托单不允许完成
        um.finish().send()
        lgr.warn("空委托单不允许完成")

        // 检定结果
        um.verify(rpt.meterList!![0].toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        Thread.sleep(100)
        lgr.info("1 meter verified.")

        // 报告签名
        // sha256sum doc/HYLWGB-20210128.pdf
        // 02b499fe69a7f8d0e7939f823c622cd7025f31cfad63b3382437e0a8c5b9e2b5  doc/HYLWGB-20210128.pdf
        val rptHash = "02b499fe69a7f8d0e7939f823c622cd7025f31cfad63b3382437e0a8c5b9e2b5"
        kotlin.run {
            val cred = Tools.loadkey(FILE_AUDTIOR)!!
            val data = Sign.getSignInterface().signMessage(rptHash.toByteArray(Charsets.US_ASCII), cred.ecKeyPair)
            val signStr = Tools.signatureDataToString(data)
            um.auditorSign(rptHash, cred.address, signStr).send()
            lgr.info("审核员签名 {}: {}.", rpt.batchId, signStr)
        }

        kotlin.run {
            val cred = Tools.loadkey(FILE_PUBLISHER)!!
            val data = Sign.getSignInterface().signMessage(rptHash.toByteArray(Charsets.US_ASCII), cred.ecKeyPair)
            val signStr = Tools.signatureDataToString(data)
            um.publisherSign(cred.address, signStr).send()
            lgr.info("检定中心签名 {}: {}.", rpt.batchId, signStr)
        }

        kotlin.run {
            val cred = Tools.loadkey(FILE_ARBITRATOR)!!
            val data = Sign.getSignInterface().signMessage(rptHash.toByteArray(Charsets.US_ASCII), cred.ecKeyPair)
            val signStr = Tools.signatureDataToString(data)
            um.arbitratorSign(cred.address, signStr).send()
            lgr.info("仲裁机构签名 {}: {}.", rpt.batchId, signStr)
        }

        // 完成委托单
        um.finish().send()
        rpt.apply {
            finishDate = Date()
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where(KEY_BATCH_ID).`is`(rpt.batchId)),
                Update.update(KEY_FINISH_DATE, rpt.finishDate),
                MeterBatch::class.java
        )
        lgr.info("完成委托单")

        // 根据合约地址查询水表检定结果.
        val batchList = mongoTemplate.find(Query(Criteria.where(KEY_REPORT_NAME).exists(true)),
                MeterBatch::class.java)
        Assert.assertTrue(batchList.size > 0)
        batchList.find { it.batchId == rpt.batchId }!!.also {
            lgr.info("retrieving contract for the meter-batch: {}", JSON.toJSONString(it, true))

            val contract = UserMeter.load(it.contractAddress, web3j, credentials,
                    StaticGasProvider(
                            GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))

            val r1 = contract.getSigner().send()
            lgr.info("检定报告 - 委托单号: {}, 审核员: {}, 审核员签名: {}, 检定中心: {}, 检定中心签名: {}, 仲裁机构: {}, 仲裁机构签名: {}.",
                    r1.value1,
                    r1.value2, r1.value3,
                    r1.value4, r1.value5,
                    r1.value6, r1.value7)

            // 验证签名
            lgr.info("审核员签名 {}: {} == {}.", rpt.batchId, Tools.getPublicKey(FILE_AUDTIOR),
                    Tools.verifySignedMessage(rptHash, r1.value3))
            assertEquals(Tools.getPublicKey(FILE_AUDTIOR),
                    Tools.verifySignedMessage(rptHash, r1.value3))

            lgr.info("检定中心签名 {}: {} == {}.", rpt.batchId, Tools.getPublicKey(FILE_PUBLISHER),
                    Tools.verifySignedMessage(rptHash, r1.value5))
            assertEquals(Tools.getPublicKey(FILE_PUBLISHER),
                    Tools.verifySignedMessage(rptHash, r1.value5))

            lgr.info("仲裁机构签名 {}: {} == {}.", rpt.batchId, Tools.getPublicKey(FILE_ARBITRATOR),
                    Tools.verifySignedMessage(rptHash, r1.value7))
            assertEquals(Tools.getPublicKey(FILE_ARBITRATOR),
                    Tools.verifySignedMessage(rptHash, r1.value7))
        }
    }

    /**
     * just comment because it's not used any more.
     */
    // @Test
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