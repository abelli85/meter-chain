package org.fisco.bcos.autoconfigure

import org.fisco.bcos.web3j.crypto.EncryptType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "encrypt-type")
open class EncryptTypeConfig {
    private var encryptType = 0

    @Bean
    open fun getEncryptType(): EncryptType {
        return EncryptType(encryptType)
    }

    /**
     * @param encryptType the encryptType to set
     */
    fun setEncryptType(encryptType: Int) {
        this.encryptType = encryptType
    }
}