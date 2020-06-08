package org.fisco.bcos.server

import org.fisco.bcos.channel.client.ChannelPushCallback
import org.fisco.bcos.channel.dto.ChannelPush
import org.fisco.bcos.channel.dto.ChannelResponse
import org.joda.time.LocalDateTime
import org.slf4j.LoggerFactory

internal class PushCallback : ChannelPushCallback() {
    override fun onPush(push: ChannelPush) {
        log.debug("push:" + push.content)
        println(LocalDateTime.now().toString() + "server:push:" + push.content)
        val response = ChannelResponse()
        response.content = "receive request seq:" + push.messageID.toString()
        response.errorCode = 0
        push.sendResponse(response)
    }

    companion object {
        private val log = LoggerFactory.getLogger(PushCallback::class.java)
    }
}