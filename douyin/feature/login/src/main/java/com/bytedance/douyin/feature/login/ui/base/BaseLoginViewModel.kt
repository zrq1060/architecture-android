package com.bytedance.douyin.feature.login.ui.base

import androidx.lifecycle.SavedStateHandle
import com.bytedance.core.architecture.stateview.LoadStateUiState
import com.bytedance.core.architecture.stateview.getErrorMessageRule
import com.bytedance.core.architecture.stateview.isLoading
import com.bytedance.core.architecture.stateview.isNormalException
import com.bytedance.douyin.core.architecture.app.AppViewModel
import com.bytedance.douyin.core.architecture.util.requestAsyncErrorHint
import com.bytedance.douyin.core.designsystem.util.toErrorMessage
import com.bytedance.douyin.feature.login.R

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/8/30 下午3:34
 */
abstract class BaseLoginViewModel<UiState>(
    private val savedStateHandle: SavedStateHandle,
) : AppViewModel<UiState>() {
    // 手机号，说明：StateFlow，有去重功能，所以EditText的赋值，而导致的文本改变不会再通知此（因为内容相同），不会造成死循环。
    val phoneNumber =
        savedStateHandle.getStateFlow<String?>(STATE_KEY_PHONE_NUMBER, null)

    // 协议
    val isAgreementChecked =
        savedStateHandle.getStateFlow(STATE_KEY_IS_AGREEMENT_CHECKED, false)

    // 验证码
    val verificationCode =
        savedStateHandle.getStateFlow<String?>(STATE_KEY_VERIFICATION_CODE, null)

    // 密码
    val password = savedStateHandle.getStateFlow<String?>(STATE_KEY_PASSWORD, null)

    // 邮箱
    val email = savedStateHandle.getStateFlow<String?>(STATE_KEY_EMAIL, null)

    // 手机号，由上个页面传值。
    val argsPhoneNumber: String? = savedStateHandle[STATE_KEY_ARGS_PHONE]

    fun createBaseLoginUiState(isButtonValid: Boolean, loadStateUiState: LoadStateUiState?) =
        BaseLoginUiState(
            isButtonValid = isButtonValid,
            // 按钮只有在加载中不可用，其他都可以。
            isButtonEnable = !loadStateUiState.isLoading(),
            // 网络规则提示消息用于UI展示，普通异常消息用于消息提示（如：Toast），其它状态不显示。
            errorMessage = loadStateUiState.getErrorMessageRule() // 规则异常
        )

    fun requestAsync(asyncFun: suspend () -> Unit) {
        // 效果说明：不隐藏内容，仅展示加载中，加载失败改为展示消息提示（普通异常）。
        requestAsyncErrorHint(
            errorCallback = {
                // 网络规则提示消息用于UI展示，普通异常消息用于消息提示（如：Toast），其它状态不显示。
                if (error.isNormalException()) {
                    // 普通异常消息用于消息提示
                    showMessage(error.toErrorMessage())
                }
            },
            asyncFun = asyncFun
        )
    }

    // 手机号改变
    fun phoneNumberChanged(phoneNumber: String) {
        savedStateHandle[STATE_KEY_PHONE_NUMBER] = phoneNumber
    }

    // 协议改变
    fun agreementIsCheckedChanged(isChecked: Boolean) {
        savedStateHandle[STATE_KEY_IS_AGREEMENT_CHECKED] = isChecked
    }

    // 验证码改变
    fun verificationCodeChanged(verificationCode: String) {
        savedStateHandle[STATE_KEY_VERIFICATION_CODE] = verificationCode
    }

    // 邮箱改变
    fun emailChanged(phoneNumber: String) {
        savedStateHandle[STATE_KEY_EMAIL] = phoneNumber
    }

    // 密码改变
    fun passwordChanged(password: String) {
        savedStateHandle[STATE_KEY_PASSWORD] = password
    }

    fun checkPhoneNumberValid() = isPhoneNumberValid().also {
        // 请输入正确的手机号
        if (!it) showMessage(R.string.douyin_feature_login_phone_number_invalid)
    }

    fun checkAgreementChecked() = isAgreementChecked.value.also {
        // 请勾选协议
        if (!it) showMessage(R.string.douyin_feature_login_agreement_unchecked)
    }

    fun checkVerificationCodeValid() = isVerificationCodeValid().also {
        // 请输入正确的验证码
        if (!it) showMessage(R.string.douyin_feature_login_verification_code_invalid)
    }

    fun checkPasswordCodeValid() = isPasswordValid().also {
        // 请输入正确的密码
        if (!it) showMessage(R.string.douyin_feature_login_password_invalid)
    }

    fun checkEmailValid() = isEmailValid().also {
        // 请输入正确的邮箱
        if (!it) showMessage(R.string.douyin_feature_login_email_invalid)
    }

    // 手机号是否有效（11位）
    fun isPhoneNumberValid() = (phoneNumber.value?.length ?: 0) >= 11

    // 验证码是否有效（6位）
    fun isVerificationCodeValid() = (verificationCode.value?.length ?: 0) >= 6

    // 密码是否有效（6位-16位）
    fun isPasswordValid() = (password.value?.length ?: 0) >= 6

    // 邮箱是否有效（11位）
    fun isEmailValid() = (email.value?.length ?: 0) >= 11
}

private const val STATE_KEY_PHONE_NUMBER = "state_key_phone_number" // 手机号
private const val STATE_KEY_IS_AGREEMENT_CHECKED = "state_key_is_agreement_checked" // 协议
private const val STATE_KEY_EMAIL = "state_key_email" // 邮箱
private const val STATE_KEY_PASSWORD = "state_key_password" // 密码
private const val STATE_KEY_VERIFICATION_CODE = "state_key_verification_code" // 验证码

private const val STATE_KEY_ARGS_PHONE = "phone" // 手机号，和navigation xml里一样。

data class BaseLoginUiState(
    val isButtonValid: Boolean = false,
    val isButtonEnable: Boolean = true,
    val errorMessage: String? = null,
)