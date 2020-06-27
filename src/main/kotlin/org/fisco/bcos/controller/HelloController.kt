package org.fisco.bcos.controller

import com.alibaba.fastjson.JSON
import org.fisco.bcos.constants.GasConstants
import org.fisco.bcos.model.Meter
import org.fisco.bcos.model.MeterBatch
import org.fisco.bcos.model.ReportVerifyResult
import org.fisco.bcos.solidity.UserMeter
import org.fisco.bcos.util.Tools
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.crypto.Sign
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
     * 显示所有委托单 (不含报告).
     */
    @GetMapping(PATH_BATCH_LIST)
    fun gallery(model: Model): String {
        lgr.info("request index: {}", PATH_BATCH_LIST)

        val batchList = mongoTemplate!!.find(
                Query(Criteria.where(KEY_REPORT_NAME).`in`(null, "")),
                MeterBatch::class.java)
        model.addAttribute("batchList", batchList)

        return "contract-list"
    }

    /**
     * 创建合约.
     */
    @GetMapping(PATH_BUILD_CONTRACT)
    fun buildContract(model: Model): String {
        return "contract-start"
    }

    /**
     * 根据表码查询从链上查询检定结果.
     * @param model - 需要填充网页的模式对象
     * @param _meterId - 要查询检定结果的表码， 不可为空.
     * @param _contractAddress - 水表所在的委托单合约地址， 如果在业务上链的节点上查看，该地址可空；
     *                           如果在其他节点上查询， 该地址不可为空。
     */
    @PostMapping(PATH_QUERY_METER)
    fun queryMeter(model: Model,
                   @RequestParam(Meter.KEY_METER_ID) _meterId: String,
                   @RequestParam(Meter.KEY_CONTRACT_ADDRESS, required = false) _contractAddress: String? = null): String {
        // 表码是否存在
        val contAddr = if (!_meterId.isNullOrBlank() && _contractAddress.isNullOrBlank())
            askContractAddress(_meterId)
        else {
            lgr.info("根据合约地址 {} 查询水表 {} 的检定结果", _contractAddress, _meterId)
            _contractAddress
        }

        // 查询链
        if (!contAddr.isNullOrBlank()) {
            try {
                val um = UserMeter.load(contAddr, web3j, credentials,
                        StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
                val chain = um.getInfo(_meterId.toByteArray())
                        .send()

                // 取得合约
                val meter = Meter().apply {
                    meterId = _meterId
                    verifierName = um.verifierName().send()
                    validDateFmt = um.validDate().send()
                    batchId = chain.value2
                    verifyTime = chain.value3
                    result = chain.value4
                    contractAddress = contAddr
                }

                model.addAttribute("meter", meter)
            } catch (ex: Exception) {
                lgr.error("contract can't be loaded: {}", ex.message)
                lgr.debug(ex.message, ex)
            }
        }

        // 展示链上合约
        return "contract-result"
    }

    /**
     * 查询水表所在的委托单合约地址， 在业务上链的节点上查询.
     * @param meterId - 要查询检定结果的表码， 不可为空
     * @return 水表所在的委托单合约地址
     */
    private fun askContractAddress(meterId: String): String? {
        lgr.info("根据水表表码请求合约地址: {}", meterId)
        val meter = mongoTemplate!!.find(Query.query(Criteria.where(Meter.KEY_METER_ID).`is`(meterId)),
                Meter::class.java).firstOrNull() ?: return null

        // 委托单是否存在
        val batch = mongoTemplate.find(Query.query(Criteria.where(MeterBatch.KEY_BATCH_ID).`is`(meter!!.batchId)),
                MeterBatch::class.java).firstOrNull() ?: return null

        return batch.contractAddress
    }

    /**
     * 查询委托单合约地址， 在业务上链的节点上查询.
     * @param batchId - 要查询检定结果的表码， 不可为空
     * @return 水表所在的委托单合约地址
     */
    private fun askBatchAddress(batchId: String): String? {
        lgr.info("根据委托单号请求合约地址: {}", batchId)
        val batch = mongoTemplate!!.find(Query.query(Criteria.where(MeterBatch.KEY_BATCH_ID).`is`(batchId)),
                MeterBatch::class.java).firstOrNull() ?: return null

        return batch.contractAddress
    }

    /**
     * 模拟检定台自动上链水表的检定结果. 生产系统中部署一个后台任务， 将检定员完成的水表检定结果自动上链.
     * @param model - 需要填充网页的模式对象
     */
    @GetMapping(PATH_AUTO_CHAIN)
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
                batch.manufacturer, batch.batchId, "黄工", "2026-6-30").send()
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
                Update.update(KEY_FINISH_DATE, batch.finishDate),
                MeterBatch::class.java
        )
        lgr.info("完成委托单")

        model.addAttribute("batch", batch)
        return "contract-upload"
    }

    /**
     * 模拟审核员上链水表的检定报告. 为方便演示, 在测试系统中提供服务接口, 用来模拟将审核员创建的水表检定报告上链.
     * @param model - 需要填充网页的模式对象
     */
    @GetMapping(PATH_AUTO_REPORT)
    fun autoReport(model: Model): String {
        lgr.info("auto report demo data.")

        val batch = MeterBatch().apply {
            batchId = "202005" + (1001..9999).random().toString()
            meterList = listOf("0228020012" + (100001..999888).random().toString())
            manufacturer = "江苏沪仪"
            reportName = "HYLWGB-20210128.pdf"
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
                batch.manufacturer, batch.batchId, "罗工", "2022-5-31").send()
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

        um.verify(batch.meterList!![0].toByteArray(), LocalDateTime.now().toString(), "PASS").send()
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
            lgr.info("审核员签名 {}: {}.", batch.batchId, signStr)
        }

        kotlin.run {
            val cred = Tools.loadkey(FILE_PUBLISHER)!!
            val data = Sign.getSignInterface().signMessage(rptHash.toByteArray(Charsets.US_ASCII), cred.ecKeyPair)
            val signStr = Tools.signatureDataToString(data)
            um.publisherSign(cred.address, signStr).send()
            lgr.info("检定中心签名 {}: {}.", batch.batchId, signStr)
        }

        kotlin.run {
            val cred = Tools.loadkey(FILE_ARBITRATOR)!!
            val data = Sign.getSignInterface().signMessage(rptHash.toByteArray(Charsets.US_ASCII), cred.ecKeyPair)
            val signStr = Tools.signatureDataToString(data)
            um.arbitratorSign(cred.address, signStr).send()
            lgr.info("仲裁机构签名 {}: {}.", batch.batchId, signStr)
        }

        // 完成委托单
        um.finish().send()
        batch.apply {
            finishDate = Date()
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where(KEY_BATCH_ID).`is`(batch.batchId)),
                Update.update(KEY_FINISH_DATE, batch.finishDate),
                MeterBatch::class.java
        )
        lgr.info("完成委托单")

        model.addAttribute("batch", batch)
        return "report-upload"
    }

    /**
     * 显示所有报告
     */
    @GetMapping(PATH_REPORT_LIST)
    fun reportList(model: Model): String {
        lgr.info("request {}", PATH_REPORT_LIST)

        val batchList = mongoTemplate!!.find(
                Query(Criteria.where(KEY_REPORT_NAME).exists(true)),
                MeterBatch::class.java)
        model.addAttribute("batchList", batchList)

        return "report-list"
    }

    /**
     * 显示所有报告
     */
    @GetMapping(PATH_SIGN)
    fun sign(model: Model): String {
        lgr.info("sign {}", PATH_SIGN)

        return "report-sign"
    }

    /**
     * 验证报告的签名.
     */
    @PostMapping(PATH_VERIFY_SIGN)
    fun verifySign(model: Model, @RequestParam("reportHash") _reportHash: String,
                   @RequestParam(KEY_BATCH_ID) _batchId: String,
                   @RequestParam(Meter.KEY_CONTRACT_ADDRESS, required = false) _contractAddress: String? = null): String {
        try {
            // 是否在上链节点查询合约地址
            val contAddr = if (_contractAddress.isNullOrBlank()) askBatchAddress(_batchId) else _contractAddress.trim()

            val um = UserMeter.load(contAddr, web3j, credentials,
                    StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))

            val vr = ReportVerifyResult().apply {
                batch = mongoTemplate!!.find(Query(Criteria.where(KEY_BATCH_ID).`is`(_batchId)), MeterBatch::class.java).firstOrNull()
                        ?: return "verify-result"

                val r1 = um.getSigner().send()

                reportHash = _reportHash

                auditor = r1.value2
                auditorSigner = r1.value3

                publisher = r1.value4
                publisherSigner = r1.value5

                arbitrator = r1.value6
                arbitratorSigner = r1.value7
            }

            val pubAuditor = Tools.getPublicKey(FILE_AUDTIOR)
            val pub1 = Tools.verifySignedMessage(vr.reportHash!!, vr.auditorSigner.orEmpty())
            vr.auditorVerify = if (pubAuditor == pub1) {
                "审核员签名验证无误"
            } else "审核员签名验证不一致: $pubAuditor != $pub1"

            val pubPublisher = Tools.getPublicKey(FILE_PUBLISHER)
            val pub2 = Tools.verifySignedMessage(vr.reportHash!!, vr.publisherSigner.orEmpty())
            vr.publisherVerify = if (pubPublisher == pub2) {
                "检定中心签名验证无误"
            } else "检定中心签名验证不一致: $pubPublisher != $pub2"

            val pubArbitrator = Tools.getPublicKey(FILE_ARBITRATOR)?.trim()
            val pub3 = Tools.verifySignedMessage(vr.reportHash!!, vr.arbitratorSigner.orEmpty()).trim()
            vr.arbitratorVerify = if (pubArbitrator == pub3) {
                "仲裁机构签名验证无误"
            } else "仲裁机构签名验证不一致: $pubArbitrator != $pub3"

            model.addAttribute("verify", vr)
        } catch (ex: Exception) {
            lgr.error("fail to fetch contract for {} caused by {}", _batchId, ex.message)
            lgr.debug(ex.message, ex)
        }

        return "verify-result"
    }

    companion object {
        // 在本地库中查询合约
        const val KEY_BATCH_ID = "batchId"
        const val KEY_FINISH_DATE = "finishDate"
        const val KEY_REPORT_NAME = "reportName"

        const val KEY_ERROR = "error"

        const val FILE_AUDTIOR = "/user.jks"
        const val FILE_PUBLISHER = "/depositor.jks"
        const val FILE_ARBITRATOR = "/arbitrator.jks"

        const val PATH_BATCH_LIST = "/gallery"
        const val PATH_BUILD_CONTRACT = "/contract"
        const val PATH_QUERY_METER = "/queryMeter"
        const val PATH_AUTO_CHAIN = "/autoChain"

        const val PATH_REPORT_LIST = "/report"
        const val PATH_SIGN = "/sign"
        const val PATH_VERIFY_SIGN = "/verifySign"
        const val PATH_AUTO_REPORT = "/autoReport"

        private val lgr = LoggerFactory.getLogger(HelloController::class.java)
    }
}