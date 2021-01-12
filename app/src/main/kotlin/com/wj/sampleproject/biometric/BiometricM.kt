@file:Suppress("DEPRECATION")

package com.wj.sampleproject.biometric

import android.app.KeyguardManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import androidx.fragment.app.FragmentActivity
import com.orhanobut.logger.Logger
import com.wj.sampleproject.dialog.BiometricDialog
import javax.crypto.Cipher

/**
 * [Build.VERSION_CODES.M] 及以上版本实现
 *
 * - 创建时间：2021/1/11
 *
 * @author 王杰
 */
@RequiresApi(Build.VERSION_CODES.M)
class BiometricM(val activity: FragmentActivity)
    : BiometricInterface {

    /** [Build.VERSION_CODES.M] 以上指纹管理对象 */
    private val fingerprintManager: FingerprintManagerCompat by lazy {
        FingerprintManagerCompat.from(activity)
    }

    override var encrypt = true
    override var keyAlias = "DEFAULT_KEY_NAME"
    override var ivBytes: ByteArray? = null
    override var title = "验证指纹"
    override var subTitle = ""
    override var hint = "请按压指纹感应区验证指纹"
    override var negative = "取消"

    private var dialog: BiometricDialog? = null

    override fun checkBiometric(): Int {
        //键盘锁管理者
        val km = activity.getSystemService(KeyguardManager::class.java)
        return when {
            !fingerprintManager.isHardwareDetected -> {
                // 不支持指纹
                BiometricInterface.UN_SUPPORT
            }
            !km.isKeyguardSecure -> {
                // 未设置锁屏
                BiometricInterface.NO_KEYGUARD_SECURE
            }
            !fingerprintManager.hasEnrolledFingerprints() -> {
                // 未注册有效指纹
                BiometricInterface.NO_ENROLLED_FINGERPRINTS
            }
            else -> {
                // 支持指纹识别
                BiometricInterface.SUPPORT
            }
        }
    }

    override fun authenticate(onSuccess: (Cipher) -> Unit, onError: (Int, String) -> Unit) {
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            onError.invoke(5, "用户取消")
        }
        dialog?.dismiss()
        dialog = null
        dialog = BiometricDialog.actionCreate(title, subTitle, hint, negative) {
            cancellationSignal.cancel()
        }
        dialog?.show(activity)
        fingerprintManager.authenticate(
                FingerprintManagerCompat.CryptoObject(loadCipher(encrypt, keyAlias, ivBytes)),
                0,
                cancellationSignal,
                object : FingerprintManagerCompat.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                        try {
                            val cipher = result?.cryptoObject?.cipher
                                    ?: throw RuntimeException("cipher is null!")
                            onSuccess.invoke(cipher)
                        } catch (throwable: Throwable) {
                            Logger.e(throwable, "authenticate")
                            onError.invoke(-1, "指纹认证失败")
                        } finally {
                            dialog?.dismiss()
                        }
                    }

                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                        dialog?.setHint(helpString.toString())
                        onError.invoke(helpCode, helpString.toString())
                    }

                    override fun onAuthenticationFailed() {
                        dialog?.dismiss()
                        onError.invoke(-1, "指纹验证失败")
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        dialog?.dismiss()
                        onError.invoke(errorCode, errString.toString())
                    }
                },
                null)
    }
}