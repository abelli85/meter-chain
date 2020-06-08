package org.fisco.bcos.server

import org.fisco.bcos.channel.client.ChannelPushCallback
import org.fisco.bcos.channel.dto.ChannelPush
import org.fisco.bcos.channel.dto.ChannelResponse
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class PushCallback : ChannelPushCallback() {
    override fun onPush(push: ChannelPush) {
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        log.debug("push:" + push.content)
        println(df.format(LocalDateTime.now()) + "server:push:" + push.content)
        val response = ChannelResponse()
        response.content = "receive request seq:" + push.messageID.toString()
        response.errorCode = 0
        push.sendResponse(response)
    }

    companion object {
        private val log = LoggerFactory.getLogger(PushCallback::class.java)
    }
}