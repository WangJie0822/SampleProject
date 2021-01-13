package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.base.utils.AppManager
import cn.wj.common.tools.toHexByteArray
import com.google.android.material.transition.platform.MaterialFade
import com.gyf.immersionbar.ImmersionBar
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.biometric.biometric
import com.wj.sampleproject.biometric.supportBiometric
import com.wj.sampleproject.biometric.tryAuthenticate
import com.wj.sampleproject.constants.*
import com.wj.sampleproject.databinding.AppActivityLoginBinding
import com.wj.sampleproject.ext.toSnackbarModel
import com.wj.sampleproject.helper.ProgressDialogHelper
import com.wj.sampleproject.helper.UserInfoData
import com.wj.sampleproject.tools.decodeString
import com.wj.sampleproject.tools.encode
import com.wj.sampleproject.tools.safeMMKV
import com.wj.sampleproject.tools.withMMKV
import com.wj.sampleproject.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 登录界面
 *
 * 创建时间：2019/10/14
 *
 * @author 王杰
 */
class LoginActivity : BaseActivity<LoginViewModel, AppActivityLoginBinding>() {

    override val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_login)

        // 关闭除首页外所有界面
        AppManager.finishAllWithout(LoginActivity::class.java, MainActivity::class.java)

        // 清除登录信息
        UserInfoData.value = null

        // 同步指纹登录信息
        val canLoginByFingerprint = supportBiometric()
                && withMMKV(safeMMKV) {
            "$DATA_CACHE_KEY_FINGERPRINT${viewModel.userName.get()}".decodeString()
        }.isNotBlank()
        viewModel.showFingerprint.set(canLoginByFingerprint)
        if (canLoginByFingerprint) {
            // 自动拉起指纹认证
            viewModel.register.value = false
            viewModel.onFingerprintClick.invoke()
        }
    }

    override fun beforeOnCreate() {
        window.run {
            enterTransition = MaterialFade().apply {
                duration = ACTIVITY_ANIM_DURATION
            }
            exitTransition = MaterialFade().apply {
                duration = ACTIVITY_ANIM_DURATION
            }
        }
    }

    override fun initImmersionbar(immersionBar: ImmersionBar) {
        immersionBar.run {
            statusBarColor(R.color.app_white)
            statusBarDarkFont(true)
        }
    }

    override fun initObserve() {
        viewModel.run {
            // 是否是注册
            register.observe(this@LoginActivity, { register ->
                (mBinding.root as MotionLayout).run {
                    if (register) {
                        transitionToStart()
                    } else {
                        transitionToEnd()
                    }
                }
            })
            // 进度弹窗
            progressData.observe(this@LoginActivity, { progress ->
                if (null == progress) {
                    ProgressDialogHelper.dismiss()
                } else {
                    ProgressDialogHelper.show(mContext, progress.cancelable, progress.hint)
                }
            })
            // 指纹登录
            fingerprintData.observe(this@LoginActivity, {
                // 发起指纹认证
                biometric.run {
                    // 已加密的数据
                    val encodedData = withMMKV(safeMMKV) {
                        val result = "$DATA_CACHE_KEY_FINGERPRINT${viewModel.userName.get()}".decodeString()
                        ivBytes = "$DATA_CACHE_KEY_CIPHER_IV$result".decodeString().toHexByteArray()
                        result
                    }
                    encrypt = false
                    subTitle = "验证指纹以登录"
                    tryAuthenticate({ cipher ->
                        // 验证成功，解密用户信息
                        val result = cipher.doFinal(encodedData.toHexByteArray())
                        val info = result.decodeToString()
                        val infoArray = info.split(USER_INFO_SPLIT)
                        if (infoArray.size < 2) {
                            // 无效信息
                            viewModel.snackbarData.value = "加密信息异常！".toSnackbarModel()
                            withMMKV(safeMMKV) {
                                "$DATA_CACHE_KEY_FINGERPRINT${viewModel.userName.get()}".encode("")
                            }
                            viewModel.showFingerprint.set(false)
                            return@tryAuthenticate
                        }
                        viewModel.userName.set(infoArray[0])
                        viewModel.password.set(infoArray[1])
                        viewModel.repassword.set(infoArray[1])
                        viewModel.onButtonClick.invoke()
                    }, { _, msg ->
                        // 验证失败
                        viewModel.snackbarData.value = msg.toSnackbarModel()
                    })
                }
            })
        }
    }

    companion object {

        /**
         * 使用 [context] 打开 [LoginActivity] 界面
         * > 栈堆已有登录页时不会重复打开
         */
        fun actionStart(context: Context) {
            if (AppManager.contains(LoginActivity::class.java)) {
                // 堆栈中已有登录页，返回
                return
            }
            context.startTargetActivity<LoginActivity>()
        }
    }
}