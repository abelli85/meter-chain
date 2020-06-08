package org.fisco.bcos.autoconfigure

import org.fisco.bcos.channel.handler.ChannelConnections
import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.util.*

@Configuration
@ConfigurationProperties(prefix = "group-channel-connections-config")
open class GroupChannelConnectionsPropertyConfig {
    /**
     * @return the allChannelConnections
     */
    /**
     * @param allChannelConnections the allChannelConnections to set
     */
    var allChannelConnections: List<ChannelConnections> = ArrayList()
    /**
     * @return the caCert
     */
    /**
     * @param caCert the caCert to set
     */
    var caCert: Resource? = null
    /**
     * @return the sslCert
     */
    /**
     * @param sslCert the sslCert to set
     */
    var sslCert: Resource? = null
    /**
     * @return the sslKey
     */
    /**
     * @param sslKey the sslKey to set
     */
    var sslKey: Resource? = null

    @get:Bean
    open val groupChannelConnections: GroupChannelConnectionsConfig
        get() {
            val groupChannelConnectionsConfig = GroupChannelConnectionsConfig()
            groupChannelConnectionsConfig.caCert = caCert
            groupChannelConnectionsConfig.sslCert = sslCert
            groupChannelConnectionsConfig.sslKey = sslKey
            groupChannelConnectionsConfig.allChannelConnections = allChannelConnections
            return groupChannelConnectionsConfig
        }

}