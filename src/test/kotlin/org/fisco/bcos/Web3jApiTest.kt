package org.fisco.bcos

import org.fisco.bcos.web3j.protocol.Web3j
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.IOException
import java.math.BigInteger

class Web3jApiTest : BaseTest() {
    @Autowired
    var web3j: Web3j? = null

    @get:Throws(IOException::class)
    @get:Test
    val blockNumber: Unit
        get() {
            val blockNumber = web3j!!.blockNumber.send().blockNumber
            Assert.assertTrue(blockNumber.compareTo(BigInteger("0")) >= 0)
        }
}