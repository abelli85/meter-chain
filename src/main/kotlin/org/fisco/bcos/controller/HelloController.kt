package org.fisco.bcos.controller

import org.fisco.bcos.constants.GasConstants
import org.fisco.bcos.model.Meter
import org.fisco.bcos.model.MeterBatch
import org.fisco.bcos.temp.UserMeter
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.protocol.Web3j
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

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

    companion object {
        private val lgr = LoggerFactory.getLogger(HelloController::class.java)
    }
}