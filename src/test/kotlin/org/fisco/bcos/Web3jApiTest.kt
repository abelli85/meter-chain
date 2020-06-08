package org.fisco.bcos

import org.fisco.bcos.web3j.protocol.Web3j
import org.junit.Assert
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import java.math.BigInteger

class Web3jApiTest : BaseTest() {
    @Autowired
    var web3j: Web3j? = null

    @Test
    fun testGetBlockNumber() {
        val blockNumber = web3j!!.blockNumber.send().blockNumber
        lgr.info("current block number: {}", blockNumber)
        Assert.assertTrue(blockNumber.compareTo(BigInteger("0")) >= 0)
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(Web3jApiTest::class.java)
    }
}