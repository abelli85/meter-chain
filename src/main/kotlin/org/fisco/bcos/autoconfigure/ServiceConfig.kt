package org.fisco.bcos.autoconfigure

import org.fisco.bcos.channel.client.Service
import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig
import org.fisco.bcos.constants.ConnectConstants
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "channel-service")
open class ServiceConfig {
    /**
     * @return the agencyName
     */
    /**
     * @param agencyName the agencyName to set
     */
    var agencyName: String? = null
    /**
     * @return the groupId
     */
    /**
     * @param groupId the groupId to set
     */
    var groupId = 0

    @Bean
    open fun getService(groupChannelConnectionsConfig: GroupChannelConnectionsConfig?): Service {
        val channelService = Service()
        channelService.connectSeconds = ConnectConstants.CONNECT_SECONDS
        channelService.orgID = agencyName
        log.info("agencyName : {}", agencyName)
        channelService.connectSleepPerMillis = ConnectConstants.CONNECT_SLEEP_PER_MILLIS
        channelService.groupId = groupId
        channelService.allChannelConnections = groupChannelConnectionsConfig
        return channelService
    }

    companion object {
        private val log = LoggerFactory.getLogger(ServiceConfig::class.java)
    }
}