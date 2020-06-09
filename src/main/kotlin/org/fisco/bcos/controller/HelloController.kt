package org.fisco.bcos.controller

import com.alibaba.fastjson.JSON
import org.fisco.bcos.constants.GasConstants
import org.fisco.bcos.model.Meter
import org.fisco.bcos.model.MeterBatch
import org.fisco.bcos.temp.UserMeter
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.protocol.Web3j
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider
import org.joda.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
class HelloController {
    @Autowired
    private val web3j: Web3j? = null

    @Autowired
    private val credentials: Credentials? = null

    @Autowired
    private val mongoTemplate: MongoTemplate? = null

    /**
     * 显示所有委托单.
     */
    @GetMapping("/")
    fun index(model: Model): String {
        lgr.info("request index: /")

        val batchList = mongoTemplate!!.findAll(MeterBatch::class.java)
        model.addAttribute("batchList", batchList)

        return "index"
    }

    /**
     * 根据表码查询从链上查询检定结果.
     */
    @PostMapping("/queryMeter")
    fun queryMeter(model: Model,
                   @RequestParam(Meter.KEY_METER_ID) meterId: String?): String {
        lgr.info("query verify result from chain for {}", meterId)

        // 表码是否存在
        val meter = mongoTemplate!!.find(Query.query(Criteria.where(Meter.KEY_METER_ID).`is`(meterId)),
                Meter::class.java).firstOrNull()

        if (!meter?.meterId.isNullOrBlank()) {
            // 委托单是否存在
            val batch = mongoTemplate.find(Query.query(Criteria.where(MeterBatch.KEY_BATCH_ID).`is`(meter!!.batchId)),
                    MeterBatch::class.java).firstOrNull()

            if (!batch?.contractAddress.isNullOrBlank()) {
                // 查询链
                val chain = UserMeter.load(batch!!.contractAddress, web3j, credentials,
                        StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
                        .getInfo(meter!!.meterId!!.toByteArray())
                        .send()

                // 取得合约
                meter.apply {
                    verifierName = batch.verifierName
                    validDate = batch.validDate
                    verifyTime = chain.value2
                    result = chain.value3
                }

                model.addAttribute("meter", meter)
            }
        }

        // 展示链上合约
        return "meterResult"
    }

    @GetMapping("/autoChain")
    fun autoChain(model: Model): String {
        lgr.info("auto chain demo data.")

        val batch = MeterBatch().apply {
            batchId = "202006" + (1001..9999).random().toString()
            meterList = listOf("0221520012" + (100001..999888).random().toString(),
                    "0221520012" + (100001..999888).random().toString())
            manufacturer = "宁波水表"
            verifierName = "abel"
            deliverDate = LocalDateTime(2020, 6, 1, 9, 20).toDate()
            validDate = LocalDateTime(2026, 5, 31, 0, 0).toDate()
        }

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

        model.addAttribute("batch", batch)
        return "autoResult"
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(HelloController::class.java)
    }
}