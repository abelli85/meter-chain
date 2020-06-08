package org.fisco.bcos

import org.fisco.bcos.constants.GasConstants
import org.fisco.bcos.temp.HelloWorld
import org.fisco.bcos.temp.UserMeter
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.protocol.Web3j
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

class ContractTest : BaseTest() {
    @Autowired
    private val web3j: Web3j? = null

    @Autowired
    private val credentials: Credentials? = null

    @Test
    fun testUserMeter() {
        val um = UserMeter.deploy(web3j, credentials,
                StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                "宁波水表").send()
        lgr.info("UserMeter contract address: {}", um.contractAddress)

        // 空委托单不允许完成
        um.finish().send()
        lgr.warn("空委托单不允许完成")

        um.verify("0221520012000123".toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        Thread.sleep(100)
        um.verify("0221520012000301".toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        lgr.info("2 meters verified.")

        um.finish().send()
        lgr.info("完成委托单")

        kotlin.run {
            val r1 = um.getInfo("0221520012000123".toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    "0221520012000123",
                    r1.value1, r1.value2, r1.value3, r1.value4)
        }

        kotlin.run {
            val r1 = um.getInfo("0221520012000301".toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    "0221520012000301",
                    r1.value1, r1.value2, r1.value3, r1.value4)
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

    companion object {
        private val lgr = LoggerFactory.getLogger(ContractTest::class.java)
    }
}