package org.fisco.bcos.server

import org.fisco.bcos.BaseTest
import org.fisco.bcos.channel.client.Service
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class Channel2Server : BaseTest() {
    @Autowired
    var service: Service? = null

    @Test
    fun testChannel2Server() {
        val topic = "topic"
        val topics: MutableSet<String> = HashSet()
        topics.add(topic)
        service!!.topics = topics
        val cb = PushCallback()
        service!!.pushCallback = cb
        lgr.info("3s...")
        Thread.sleep(1000)
        lgr.info("2s...")
        Thread.sleep(1000)
        lgr.info("1s...")
        Thread.sleep(1000)
        lgr.info("start test")
        lgr.info("===================================================================")
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(Channel2Server::class.java)
    }
}