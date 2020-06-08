package org.fisco.bcos.server

import org.fisco.bcos.BaseTest
import org.fisco.bcos.channel.client.Service
import org.fisco.bcos.channel.dto.ChannelRequest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Channel2Client : BaseTest() {
    @Autowired
    var service: Service? = null

    @Test
    @Throws(Exception::class)
    fun channel2ClientTest() {
        val topic = "topic"
        val count = "2".toInt()
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        println("3s ...")
        Thread.sleep(1000)
        println("2s ...")
        Thread.sleep(1000)
        println("1s ...")
        Thread.sleep(1000)
        println("start test")
        println("===================================================================")
        for (i in 0 until count) {
            Thread.sleep(2000)
            val request = ChannelRequest()
            request.toTopic = topic
            request.messageID = service!!.newSeq()
            request.timeout = 5000
            request.content = "request seq:" + request.messageID
            println(
                    df.format(LocalDateTime.now())
                            + " request seq:"
                            + request.messageID.toString() + ", Content:"
                            + request.content)
            val response = service!!.sendChannelMessage2(request)
            println(
                    df.format(LocalDateTime.now())
                            + "response seq:"
                            + response.messageID.toString() + ", ErrorCode:"
                            + response.errorCode
                            + ", Content:"
                            + response.content)
        }
    }
}