package com.wj.sampleproject.biometric

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

/**
 * 生物识别接口
 *
 * - 创建时间：2021/1/11
 *
 * @author 王杰
 */
interface BiometricInterface {

    /** 标记 - 是否是加密 */
    var encrypt: Boolean

    /** 密钥别名 */
    var keyAlias: String

    /** 解密向量字节数组，加密可为 `null` */
    var ivBytes: ByteArray?

    /** 弹窗标题 */
    var title: String

    /** 弹窗副标题 */
    var subTitle: String

    /** 默认提示文本 */
    var hint: String

    /** 取消按钮文本 */
    var negative: String

    /** 检查指纹识别支持装汤 */
    fun checkBiometric(): Int

    /** 发起认证，成功回调 [onSuccess] 回传 [Cipher] 对象，失败回调 [onError] 回传错误码 [Int] 错误信息 [String] */
    fun authenticate(onSuccess: (Cipher) -> Unit, onError: (Int, String) -> Unit)

    /** 根据是否加密 [encrypt] 密钥别名 [keyAlias] 以及解密时必须的向量字节数组 [bytes] 创建 [Cipher] 对象 */
    @RequiresApi(Build.VERSION_CODES.M)
    fun loadCipher(encrypt: Boolean, keyAlias: String, bytes: ByteArray? = null): Cipher {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if (!keyStore.containsAlias(keyAlias)) {
            // 秘钥生成器
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val builder = KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(false)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            keyGenerator.init(builder.build())
            keyGenerator.generateKey()
        }
        val key = keyStore.getKey(keyAlias, null)
        val cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        if (encrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, key)
        } else {
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            // 解密时必穿，加密时生成
            val iv = IvParameterSpec(bytes)
            cipher.init(Cipher.DECRYPT_MODE, key, iv)
        }
        return cipher
    }

    companion object {

        /** 支持指纹 */
        const val SUPPORT = 0

        /** 不支持指纹 */
        const val UN_SUPPORT = -1

        /** 没有设置锁屏 */
        const val NO_KEYGUARD_SECURE = -2

        /** 没有录入指纹 */
        const val NO_ENROLLED_FINGERPRINTS = -3
    }
}