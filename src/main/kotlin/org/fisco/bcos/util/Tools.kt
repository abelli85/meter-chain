package org.fisco.bcos.util

import org.fisco.bcos.web3j.crypto.Credentials
import org.fisco.bcos.web3j.crypto.ECKeyPair
import org.fisco.bcos.web3j.crypto.Keys
import org.fisco.bcos.web3j.crypto.Sign
import org.fisco.bcos.web3j.crypto.Sign.SignatureData
import org.fisco.bcos.web3j.utils.Numeric
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.SignatureException
import java.security.interfaces.ECPrivateKey

/**
 * 工具对象, 加载密钥/验证签名/...
 * 关于地址：
 * FISCO BCOS的账户地址由ECDSA公钥计算得来，对ECDSA公钥的16进制表示计算keccak-256sum哈希，
 * 取计算结果的后20字节的16进制表示作为账户地址，每个字节需要两个16进制数表示，所以账户地址长度为40。
 * FISCO BCOS的账户地址与以太坊兼容。 注意keccak-256sum与SHA3不相同，详情参考
 * <a href='https://ethereum.stackexchange.com/questions/550/which-cryptographic-hash-function-does-ethereum-use'>这里</a>.
 */
object Tools {
    private val lgr = LoggerFactory.getLogger(Tools::class.java)

    /**
     * 根据消息及签名, 获取公钥地址.
     * @param message - 明文, 在本例中表示报告的哈希HEX文本
     * @param signatureData - 签名, 在本例中表示报告的哈希签名的HEX文本
     * @return 签名的地址
     * @see Keys
     */
    @Throws(SignatureException::class)
    fun verifySignedMessage(message: String, signatureData: String): String {
        val signatureData1 = stringToSignatureData(signatureData)
        return "0x" + Keys.getAddress(Sign.signedMessageToKey(message.toByteArray(), signatureData1))
    }

    /**
     * 从ks文件中加载密钥.
     * @param keyStoreFileName - ks文件
     * @param keyStorePassword - ks口令
     * @param keyPassword - 密钥的口令
     * @param keyAlias - 密钥的别名, 默认ec
     * @return - 验证实例
     */
    fun loadkey(keyStoreFileName: String, keyStorePassword: String, keyPassword: String, keyAlias: String = "ec"): Credentials? {
        var ksInputStream: InputStream? = null
        try {
            val ks = KeyStore.getInstance("JKS")
            ksInputStream = this.javaClass.getResourceAsStream(keyStoreFileName)
            ks.load(ksInputStream, keyStorePassword.toCharArray())
            val key = ks.getKey(keyAlias, keyPassword.toCharArray())
            val keyPair = ECKeyPair.create((key as ECPrivateKey).s)
            val credentials = Credentials.create(keyPair)
            if (credentials != null) {
                return credentials
            } else {
                lgr.error("秘钥参数输入有误！")
            }
        } catch (e: Exception) {
            lgr.error("fail to load key {} caused by {}", keyStoreFileName, e.message)
            lgr.debug(e.message, e)
        } finally {
            try {
                ksInputStream?.close()
            } catch (e: IOException) {
                lgr.warn("fail to close {} caused by {}", keyStoreFileName, e.message)
            }
        }

        return null
    }

    /**
     * 从ks文件中获取公钥地址 (详情参考
     * <a href='https://ethereum.stackexchange.com/questions/550/which-cryptographic-hash-function-does-ethereum-use'>这里</a>)
     * @param keyStoreFileName - ks文件
     * @param keyStorePassword - ks口令
     * @param keyPassword - 密钥的口令
     * @param keyAlias - 密钥的别名, 默认ec
     * @return - 公钥地址
     */
    @Throws(Exception::class)
    fun getPublicKey(keyStoreFileName: String, keyStorePassword: String, keyPassword: String, keyAlias: String = "ec"): String? {
        val credentials = loadkey(keyStoreFileName, keyStorePassword, keyPassword)
        return credentials?.address
    }

    /**
     * 将签名HEX文本 转换为签名对象.
     * @param signatureData - 签名HEX文本
     * @return - 签名对象
     */
    fun stringToSignatureData(signatureData: String): SignatureData {
        val byte_3 = Numeric.hexStringToByteArray(signatureData)
        val signR = ByteArray(32)
        System.arraycopy(byte_3, 1, signR, 0, signR.size)
        val signS = ByteArray(32)
        System.arraycopy(byte_3, 1 + signR.size, signS, 0, signS.size)
        return SignatureData(byte_3[0], signR, signS)
    }

    /**
     * 将签名HEX文本 转换为签名对象.
     * @param signatureData - 签名HEX文本
     * @return - 签名对象
     */
    fun signatureDataToString(signatureData: SignatureData): String {
        val byte_3 = ByteArray(1 + signatureData.r.size + signatureData.s.size)
        byte_3[0] = signatureData.v
        System.arraycopy(signatureData.r, 0, byte_3, 1, signatureData.r.size)
        System.arraycopy(signatureData.s, 0, byte_3, signatureData.r.size + 1, signatureData.s.size)
        return Numeric.toHexString(byte_3, 0, byte_3.size, false)
    }
}