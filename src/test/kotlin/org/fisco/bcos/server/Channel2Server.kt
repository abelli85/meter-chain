package org.fisco.bcos.server

import org.fisco.bcos.BaseTest
import org.fisco.bcos.channel.client.Service
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class Channel2Server : BaseTest() {
    @Autowired
    var service: Service? = null

    @Test
    @Throws(Exception::class)
    fun channel2ServerTest() {
        val topic = "topic"
        val topics: MutableSet<String> = HashSet()
        topics.add(topic)
        service!!.topics = topics
        val cb = PushCallback()
        service!!.pushCallback = cb
        println("3s...")
        Thread.sleep(1000)
        println("2s...")
        Thread.sleep(1000)
        println("1s...")
        Thread.sleep(1000)
        println("start test")
        println("===================================================================")
    }
}