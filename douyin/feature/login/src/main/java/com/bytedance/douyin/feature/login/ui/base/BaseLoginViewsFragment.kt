package com.bytedance.douyin.feature.login.ui.base

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bytedance.core.architecture.base.BaseViewModel
import com.bytedance.core.architecture.util.uiStateMapValueChangedCollect
import com.bytedance.core.common.util.getDataFromThemeAttr
import com.bytedance.douyin.core.architecture.app.views.AppViewsFragment
import com.bytedance.douyin.core.designsystem.util.NoUnderlineClickableSpan
import com.bytedance.douyin.core.webview.WebViewRouter
import com.bytedance.douyin.feature.login.R
import com.bytedance.douyin.feature.login.ui.login.password.LoginByPhoneNumberAndPasswordFragmentDirections
import com.zrq.spanbuilder.Spans
import com.bytedance.douyin.core.designsystem.R as designSystemR
import com.google.android.material.R as materialR

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/4/10 上午10:54
 */
abstract class BaseLoginViewsFragment<Binding : ViewBinding, UiState : Any, ViewModel : BaseViewModel<UiState>> :
    AppViewsFragment<Binding, UiState, ViewModel>() {
    private val navController by lazy { findNavController() }
    var uiStateNavigateInProgress: Boolean = false

    // 设置全局转换动画
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(designSystemR.anim.douyin_core_designsystem_slide_in_right)
        .setExitAnim(designSystemR.anim.douyin_core_designsystem_slide_out_left)
        .setPopEnterAnim(designSystemR.anim.douyin_core_designsystem_slide_in_left)
        .setPopExitAnim(designSystemR.anim.douyin_core_designsystem_slide_out_right).build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        uiStateNavigateInProgress =
            savedInstanceState?.getBoolean(KEY_UI_STATE_NAVIGATE_IN_PROGRESS) ?: false
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_UI_STATE_NAVIGATE_IN_PROGRESS, uiStateNavigateInProgress)
    }

    fun navigate(directions: NavDirections) {
        navController.navigate(directions, navOptions)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateToRetrievePassword(phone: String?) {
        if (phone.isNullOrEmpty()) {
            viewModel.showMessage(R.string.douyin_feature_login_phone_number_empty)
            return
        }
        navigate(LoginByPhoneNumberAndPasswordFragmentDirections.actionToRetrievePassword(phone))
    }

    fun handleBaseLoginUiState(
        baseLoginUiState: BaseLoginUiState,
        error: TextView,
        login: Button,
    ) {
        // 错误提示
        error.isVisible = !baseLoginUiState.errorMessage.isNullOrBlank()
        error.text = baseLoginUiState.errorMessage
        // 按钮状态
        login.isEnabled = baseLoginUiState.isButtonEnable
        login.alpha = if (baseLoginUiState.isButtonValid) 1f else 0.3f
    }

    fun initAgreement(agreement: TextView, isAllText: Boolean) {
        agreement.text = Spans.builder()
            .text(resources.getText(R.string.douyin_feature_login_phone_number_and_verification_code_agreement_1))
            .text(resources.getText(R.string.douyin_feature_login_phone_number_and_verification_code_agreement_2))
            .click(agreement, NoUnderlineClickableSpan {
                WebViewRouter.startUserAgreement(
                    requireActivity(),
                    R.string.douyin_feature_login_phone_number_and_verification_code_agreement_2
                )
            })
            .text(resources.getText(R.string.douyin_feature_login_phone_number_and_verification_code_agreement_3))
            .text(resources.getText(R.string.douyin_feature_login_phone_number_and_verification_code_agreement_4))
            .click(agreement, NoUnderlineClickableSpan {
                WebViewRouter.startPrivacyPolicy(
                    requireActivity(),
                    R.string.douyin_feature_login_phone_number_and_verification_code_agreement_4
                )
            })
            .text(resources.getText(R.string.douyin_feature_login_phone_number_and_verification_code_agreement_5))
            .text(resources.getText(R.string.douyin_feature_login_phone_number_and_verification_code_agreement_6))
            .click(agreement, NoUnderlineClickableSpan {
                WebViewRouter.startCarrierServiceAgreement(
                    requireActivity(),
                    R.string.douyin_feature_login_phone_number_and_verification_code_agreement_6
                )
            })
            .text(resources.getText(if (isAllText) R.string.douyin_feature_login_phone_number_and_verification_code_agreement_7_all else R.string.douyin_feature_login_phone_number_and_verification_code_agreement_7_start))
            .build()
    }

    // 初始化验证码已发送
    fun initVerificationCodeSent(subtitle: TextView, getPhoneNumber: (UiState) -> String?) {
        // 手机号，在此单独设置，加入去重处理，目的是为了优化UiState频繁改变。
        uiStateMapValueChangedCollect({ getPhoneNumber(it) }) { phoneNumber ->
            // 验证码提示
            val phoneColor =
                requireContext().getDataFromThemeAttr(materialR.attr.colorOnSurface)
            subtitle.text = Spans.builder()
                .text(resources.getText(R.string.douyin_feature_login_phone_number_and_verification_code_next_verification_code_hint_1))
                .text(phoneNumber).color(phoneColor).style(Typeface.BOLD).build()
        }
    }

    fun startHelpAndSetup() {
        WebViewRouter.startHelpAndSetup(
            requireActivity(),
            R.string.douyin_feature_login_help_and_setup
        )
    }

    companion object {
        private const val KEY_UI_STATE_NAVIGATE_IN_PROGRESS = "key_ui_state_navigate_in_progress"
    }
}