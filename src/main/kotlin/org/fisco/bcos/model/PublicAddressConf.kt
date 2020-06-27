package org.fisco.bcos.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
@ConfigurationProperties(prefix = "addressconf")
class PublicAddressConf {
    /**
     * In the configuration:
     * User - key-store file user.jks, respond to 审核员;
     * Arbitrator - key-store file arbitrator.jks, respond to 城市计量局;
     * Depositor - key-store file depositor.jks, respond to 检定中心.
     */
    var allPublicAddress: ConcurrentHashMap<String, String>? = null

    companion object {
        const val USER_CONFIG = "User"
        const val ARBITRATOR_CONFIG = "Arbitrator"
        const val DEPOSITOR_CONFIG = "Depositor"
    }
}