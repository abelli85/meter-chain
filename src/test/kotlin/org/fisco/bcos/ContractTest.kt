package org.fisco.bcos

import org.fisco.bcos.constants.GasConstants
import org.fisco.bcos.temp.HelloWorld
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.protocol.Web3j
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider
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
    fun testDeployAndCallHelloWorld() {
        // deploy contract
        val helloWorld: HelloWorld = HelloWorld.deploy(
                web3j,
                credentials,
                StaticGasProvider(
                        GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
                .send()
        if (helloWorld != null) {
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