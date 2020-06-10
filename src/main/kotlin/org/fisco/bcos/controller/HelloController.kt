package org.fisco.bcos.controller

import com.alibaba.fastjson.JSON
import org.fisco.bcos.constants.GasConstants
import org.fisco.bcos.model.Meter
import org.fisco.bcos.model.MeterBatch
import org.fisco.bcos.solidity.UserMeter
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

/**
 * 请求地址的实现， 考虑本项目业务简单，暂且将全部请求地址放在一起.
 */
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
     * @param model - 需要填充网页的模式对象
     * @param _meterId - 要查询检定结果的表码， 不可为空.
     * @param _contractAddress - 水表所在的委托单合约地址， 如果在业务上链的节点上查看，该地址可空； 
     *                           如果在其他节点上查询， 该地址不可为空。
     */
    @PostMapping("/queryMeter")
    fun queryMeter(model: Model,
                   @RequestParam(Meter.KEY_METER_ID) _meterId: String,
                   @RequestParam(Meter.KEY_CONTRACT_ADDRESS, required = false) _contractAddress: String? = null): String {
        lgr.info("query verify result from chain for {}", _meterId)

        // 表码是否存在
        val contAddr = if (!_meterId.isNullOrBlank() && _contractAddress.isNullOrBlank())
            askContractAddress(_meterId)
        else _contractAddress

        // 查询链
        if (!contAddr.isNullOrBlank()) {
            val um = UserMeter.load(contAddr, web3j, credentials,
                    StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
            val chain = um.getInfo(_meterId.toByteArray())
                    .send()

            // 取得合约
            val meter = Meter().apply {
                meterId = _meterId
                verifierName = um.verifier().send()
                validDateFmt = um.validDate().send()
                verifyTime = chain.value2
                result = chain.value3
                contractAddress = contAddr
            }

            model.addAttribute("meter", meter)
        }

        // 展示链上合约
        return "meterResult"
    }

    /**
     * 查询水表所在的委托单合约地址， 在业务上链的节点上查询.
     * @param meterId - 要查询检定结果的表码， 不可为空
     * @return 水表所在的委托单合约地址
     */
    private fun askContractAddress(meterId: String): String? {
        val meter = mongoTemplate!!.find(Query.query(Criteria.where(Meter.KEY_METER_ID).`is`(meterId)),
                Meter::class.java).firstOrNull() ?: return null

        // 委托单是否存在
        val batch = mongoTemplate.find(Query.query(Criteria.where(MeterBatch.KEY_BATCH_ID).`is`(meter!!.batchId)),
                MeterBatch::class.java).firstOrNull() ?: return null

        return batch.contractAddress
    }

    /**
     * 模拟检定台自动上链水表的检定结果. 生产系统中部署一个后台任务， 将检定员完成的水表检定结果自动上链.
     * @param model - 需要填充网页的模式对象
     */
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
                "宁波水表", "黄工", "2026-5-31").send()
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