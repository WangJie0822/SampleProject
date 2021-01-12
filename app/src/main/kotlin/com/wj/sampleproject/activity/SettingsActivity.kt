package com.wj.sampleproject.activity

import android.content.Context
import android.os.Bundle
import cn.wj.android.base.ext.startTargetActivity
import cn.wj.android.base.ext.string
import cn.wj.common.tools.toHexString
import com.tencent.mmkv.MMKV
import com.wj.sampleproject.R
import com.wj.sampleproject.base.ui.BaseActivity
import com.wj.sampleproject.biometric.biometric
import com.wj.sampleproject.biometric.supportBiometric
import com.wj.sampleproject.biometric.tryAuthenticate
import com.wj.sampleproject.constants.SP_KEY_CIPHER_IV
import com.wj.sampleproject.constants.SP_KEY_FINGERPRINT
import com.wj.sampleproject.databinding.AppActivitySettingsBinding
import com.wj.sampleproject.dialog.GeneralDialog
import com.wj.sampleproject.dialog.VerificationDialog
import com.wj.sampleproject.ext.toSnackbarModel
import com.wj.sampleproject.helper.ProgressDialogHelper
import com.wj.sampleproject.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

/**
 * 设置界面
 *
 * - 创建时间：2021/1/11
 *
 * @author 王杰
 */
class SettingsActivity
    : BaseActivity<SettingsViewModel, AppActivitySettingsBinding>() {

    override val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_settings)

        // 判断显示指纹登录
        viewModel.showFingerprint.set(supportBiometric())
    }

    override fun initObserve() {
        viewModel.run {
            // 进度弹窗
            progressData.observe(this@SettingsActivity, { progress ->
                if (null == progress) {
                    ProgressDialogHelper.dismiss()
                } else {
                    ProgressDialogHelper.show(mContext, progress.cancelable, progress.hint)
                }
            })
            // 显示验证弹窗
            showVerificationData.observe(this@SettingsActivity, {
                VerificationDialog.actionShow(mContext) { info ->
                    if (info.isBlank()) {
                        // 取消
                        viewModel.fingerprintChecked.set(false)
                    } else {
                        // 验证成功，拉起指纹验证
                        biometric.run {
                            encrypt = true
                            subTitle = "验证指纹以开启指纹登录"
                            tryAuthenticate({ cipher ->
                                // 验证成功，保存加密向量
                                MMKV.defaultMMKV().encode(SP_KEY_CIPHER_IV, cipher.iv.toHexString())
                                // 加密用户信息
                                val result = cipher.doFinal(info.toByteArray())
                                // 保存加密后的用户信息
                                MMKV.defaultMMKV().encode(SP_KEY_FINGERPRINT, result.toHexString())
                                viewModel.snackbarData.value = "指纹登录开启成功".toSnackbarModel()
                            }, { _, msg ->
                                // 验证失败
                                viewModel.fingerprintChecked.set(false)
                                viewModel.snackbarData.value = msg.toSnackbarModel()
                            })
                        }
                    }
                }
            })
            // 显示退出登录提示弹窗
            showLogoutData.observe(this@SettingsActivity, {
                GeneralDialog.newBuilder()
                        .contentStr(R.string.app_are_you_sure_to_logout.string)
                        .setOnPositiveAction {
                            // 退出登录
                            viewModel.logout()
                        }
                        .show(mContext)
            })
        }
    }

    companion object {

        /** 使用 [context] 对象打开 SettingsActivity 界面 */
        fun actionStart(context: Context) {
            context.startTargetActivity<SettingsActivity>()
        }
    }
}