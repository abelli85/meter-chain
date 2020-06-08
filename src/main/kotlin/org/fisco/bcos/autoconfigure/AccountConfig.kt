/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fisco.bcos.autoconfigure

import org.fisco.bcos.channel.client.P12Manager
import org.fisco.bcos.channel.client.PEMManager
import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.crypto.EncryptType
import org.fisco.bcos.web3j.crypto.gm.GenCredential
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException
import java.security.spec.InvalidKeySpecException

@Configuration
@ConfigurationProperties(prefix = "accounts")
open class AccountConfig {
    /**
     * @return the pemFile
     */
    /**
     * @param pemFile the pemFile to set
     */
    var pemFile: String? = null
    /**
     * @return the p12File
     */
    /**
     * @param p12File the p12File to set
     */
    var p12File: String? = null
    /**
     * @return the password
     */
    /**
     * @param password the password to set
     */
    var password: String? = null

    @Autowired
    val encryptType: EncryptType? = null

    // return loadP12Account();
    @get:Throws(UnrecoverableKeyException::class, KeyStoreException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class, NoSuchProviderException::class, CertificateException::class, IOException::class)
    @get:Bean
    open val credentials: Credentials
        get() = loadPemAccount()
    // return loadP12Account();

    // load pem account file
    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, CertificateException::class, IOException::class, NoSuchProviderException::class, InvalidKeySpecException::class, UnrecoverableKeyException::class)
    private fun loadPemAccount(): Credentials {
        log.info("pem accounts : {}", pemFile)
        val pem = PEMManager()
        pem.pemFile = "classpath:$pemFile"
        pem.load()
        val keyPair = pem.ecKeyPair
        val credentials = GenCredential.create(keyPair.privateKey.toString(16))
        println(credentials.address)
        return credentials
    }

    // load p12 account file
    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, CertificateException::class, IOException::class, NoSuchProviderException::class, InvalidKeySpecException::class, UnrecoverableKeyException::class)
    private fun loadP12Account(): Credentials {
        log.info("p12 accounts : {}", p12File)
        val p12Manager = P12Manager()
        p12Manager.p12File = "classpath:$p12File"
        p12Manager.password = password
        p12Manager.load()
        val keyPair = p12Manager.ecKeyPair
        val credentials = GenCredential.create(keyPair.privateKey.toString(16))
        println(credentials.address)
        return credentials
    }

    companion object {
        private val log = LoggerFactory.getLogger(AccountConfig::class.java)
    }
}