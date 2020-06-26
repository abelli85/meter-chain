package org.fisco.bcos

import org.fisco.bcos.web3j.crypto.ECKeyPair
import org.fisco.bcos.web3j.crypto.Keys
import org.junit.Test
import org.slf4j.LoggerFactory
import java.security.KeyStore
import java.security.interfaces.ECPrivateKey
import kotlin.test.assertEquals

/**
 * web3j-sdk can produce global address from public key.
 * The address can be compared with that of the contract.
 */
class HelloTest {
    companion object {
        const val storePass = "123456"
        const val keyPass = "123456"
        private val lgr = LoggerFactory.getLogger(HelloTest::class.java)
    }

    @Test
    fun testPubAddress() {
        printAddress("/user.jks", "0x33674063c4618f4773fac75dc2f07e55f6f391ce")
        printAddress("/arbitrator.jks", "0x6bc952a2e4db9c0c86a368d83e9df0c6ab481102")
        printAddress("/depositor.jks", "0x5a6c7ccf9efa702f4e8888ff7e8a3310abcf8c51")
    }

    private fun printAddress(storeFile: String, pubAddr: String) {
        val ks = KeyStore.getInstance("JKS")
        val ksInputStream = javaClass.getResourceAsStream(storeFile)
        ks.load(ksInputStream, storePass.toCharArray())
        val key = ks.getKey("ec", keyPass.toCharArray())
        val keyPair = ECKeyPair.create((key as ECPrivateKey).s)

        val addr = Keys.getAddress(keyPair)
        lgr.info("public key address: {}", addr)
        assertEquals(pubAddr, "0x$addr")
    }
}