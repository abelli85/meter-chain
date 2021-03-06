package org.fisco.bcos

import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.precompile.config.SystemConfigService
import org.fisco.bcos.web3j.protocol.Web3j
import org.junit.Assert
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

class PrecompiledServiceApiTest : BaseTest() {
    @Autowired
    var web3j: Web3j? = null

    @Autowired
    private val credentials: Credentials? = null

    @Test
    fun testSystemConfigService() {
        val systemConfigSerivce = SystemConfigService(web3j, credentials)
        systemConfigSerivce.setValueByKey("tx_count_limit", "2000")
        val value = web3j!!.getSystemConfigByKey("tx_count_limit").send().systemConfigByKey
        lgr.info(value)
        Assert.assertTrue("2000" == value)
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(PrecompiledServiceApiTest::class.java)
    }
}