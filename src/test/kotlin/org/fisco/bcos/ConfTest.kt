package org.fisco.bcos

import com.alibaba.fastjson.JSON
import org.fisco.bcos.model.PublicAddressConf
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ConfTest : BaseTest() {
    @Autowired
    var addressConf: PublicAddressConf? = null

    @Test
    fun testLoadConfig() {
        lgr.info("loaded config: {}", JSON.toJSONString(addressConf, true))
        assertEquals("293461115202753673762949982689068818706923229646", addressConf?.allPublicAddress!!["User"])
        assertEquals("615351660900449590975935785967886615259734610178", addressConf?.allPublicAddress!!["Arbitrator"])
        assertEquals("516228522428810728311669478473203883700105743441", addressConf?.allPublicAddress!!["Depositor"])
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(ConfTest::class.java)
    }
}