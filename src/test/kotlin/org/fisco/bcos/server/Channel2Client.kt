package org.fisco.bcos.server

import org.fisco.bcos.BaseTest
import org.fisco.bcos.channel.client.Service
import org.fisco.bcos.channel.dto.ChannelRequest
import org.joda.time.LocalDateTime
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

class Channel2Client : BaseTest() {
    @Autowired
    var service: Service? = null

    @Test
    fun testChannel2ClientTest() {
        val topic = "topic"
        val count = "2".toInt()
        lgr.info("3s ...")
        Thread.sleep(1000)
        lgr.info("2s ...")
        Thread.sleep(1000)
        lgr.info("1s ...")
        Thread.sleep(1000)
        lgr.info("start test")
        lgr.info("===================================================================")
        for (i in 0 until count) {
            Thread.sleep(2000)
            val request = ChannelRequest()
            request.toTopic = topic
            request.messageID = service!!.newSeq()
            request.timeout = 5000
            request.content = "request seq:" + request.messageID
            lgr.info("{} request seq: {}, Content: {}",
                    LocalDateTime.now(),
                    request.messageID.toString(),
                    request.content)
            val response = service!!.sendChannelMessage2(request)
            lgr.info("{} response seq: {}, ErrorCode: {}, Content:{}",
                    LocalDateTime.now(),
                    response.messageID.toString(),
                    response.errorCode,
                    response.content)
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(Channel2Client::class.java)
    }
}