package com.bytedance.douyin.core.architecture.util

import android.view.View
import androidx.lifecycle.viewModelScope
import com.bytedance.core.architecture.base.BaseViewModel
import com.bytedance.core.architecture.stateview.LoadStateUiState
import com.bytedance.douyin.core.designsystem.util.toErrorMessage
import kotlinx.coroutines.launch

/**
 * 描述：App内所有的请求StateView样式
 *
 * @author zhangrq
 * createTime 2024/12/18 10:17
 */
/**
 * 请求异步-通用
 */
private fun <Data> BaseViewModel<*>.requestAsyncBase(
    // 默认值，用于统一修改下面状态的默认值。
    isShowStateViewReplaceViewAllStateDefault: Boolean?,
    isShowStateViewAllStateDefault: Boolean,
    // loading
    isLoadingShowStateViewReplaceView: Boolean? = isShowStateViewReplaceViewAllStateDefault,
    isLoadingShowStateView: Boolean = isShowStateViewAllStateDefault,
    isLoadingDialog: Boolean = false,
    loadingCallback: (LoadStateUiState.Loading.() -> Unit)? = null,
    // error
    isErrorShowStateViewReplaceView: Boolean? = isShowStateViewReplaceViewAllStateDefault,
    isErrorShowStateView: Boolean = isShowStateViewAllStateDefault,
    errorCallback: (LoadStateUiState.Error.() -> Unit)? = null,
    // empty
    isEmptySuccess: Data.() -> Boolean = { false }, // 判断Success下什么情况下为空，默认不支持为空。
    isEmptyShowStateViewReplaceView: Boolean? = isShowStateViewReplaceViewAllStateDefault,
    isEmptyShowStateView: Boolean = isShowStateViewAllStateDefault,
    emptyCallback: (LoadStateUiState.Empty.() -> Unit)? = null,
    // success
    successCallback: (LoadStateUiState.Success.() -> Unit)? = null,

    // asyncFun
    asyncFun: suspend () -> Data,
) {
    viewModelScope.launch {
        // Loading
        val loading = LoadStateUiState.Loading(
            isLoadingShowStateViewReplaceView,
            isLoadingShowStateView,
            isLoadingDialog
        )
        loadStateUiState.value = loading
        loadingCallback?.invoke(loading)
        try {
            val data = asyncFun()
            if (isEmptySuccess(data)) {
                // Empty
                val empty = LoadStateUiState.Empty(
                    isEmptyShowStateViewReplaceView,
                    isEmptyShowStateView
                )
                loadStateUiState.value = empty
                emptyCallback?.invoke(empty)
            } else {
                // Success
                // 加载成功，一定要显示StateViewReplaceView、隐藏StateView
                val success = LoadStateUiState.Success(
                    isShowStateViewReplaceView = true,
                    isShowStateView = false
                )
                loadStateUiState.value = success
                successCallback?.invoke(success)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Error
            // 重试
            val retry: (v: View) -> Unit = {
                requestAsyncBase(
                    isShowStateViewReplaceViewAllStateDefault,
                    isShowStateViewAllStateDefault,
                    // loading
                    isLoadingShowStateViewReplaceView,
                    isLoadingShowStateView,
                    isLoadingDialog,
                    loadingCallback,
                    // error
                    isErrorShowStateViewReplaceView,
                    isErrorShowStateView,
                    errorCallback,
                    // empty
                    isEmptySuccess,
                    isEmptyShowStateViewReplaceView,
                    isEmptyShowStateView,
                    emptyCallback,
                    // success
                    successCallback,
                    // asyncFun
                    asyncFun
                )
            }
            val error =
                LoadStateUiState.Error(
                    isErrorShowStateViewReplaceView,
                    isErrorShowStateView,
                    e,
                    retry
                )
            loadStateUiState.value = error
            errorCallback?.invoke(error)
        }
    }
}

/**
 * 请求异步-Error提示。即：StateViewReplaceView全部显示，StateView仅展示加载中展示，加载失败默认改为展示消息提示。
 */
fun <Data> BaseViewModel<*>.requestAsyncErrorHint(
    isSingleRequest: Boolean = true, // 是否单（只有一个）请求
    errorCallback: (LoadStateUiState.Error.() -> Unit)? = null,
    asyncFun: suspend () -> Data,
) {
    if (isSingleRequest && loadStateUiState.value is LoadStateUiState.Loading) {
        // 只能一个请求并且正在请求中，直接返回。
        return
    }
    // 效果说明：不隐藏内容，仅展示加载中，加载失败改为展示消息提示。
    requestAsyncBase(
        // 所有状态显示StateViewReplaceView
        isShowStateViewReplaceViewAllStateDefault = true,
        // 所有状态不显示StateView（Loading除外，下面设置）
        isShowStateViewAllStateDefault = false,
        // 设置显示StateView-Loading
        isLoadingShowStateView = true,
        // 设置显示StateView-Error，改为定制（先用参数的，然后再用自己的消息提示）
        errorCallback = errorCallback ?: { showMessage(error.toErrorMessage()) },
        // 请求
        asyncFun = asyncFun
    )
}

/**
 * 请求异步-展示所有状态。即：StateView全部显示，StateViewReplaceView仅加载中显示。
 */
fun <Data> BaseViewModel<*>.requestAsyncShowAllState(
    isSingleRequest: Boolean = true, // 是否单（只有一个）请求
    asyncFun: suspend () -> Data,
) {
    if (isSingleRequest && loadStateUiState.value is LoadStateUiState.Loading) {
        // 只能一个请求并且正在请求中，直接返回。
        return
    }
    requestAsyncBase(
        // 所有状态不显示StateViewReplaceView（Loading除外，下面设置）
        isShowStateViewReplaceViewAllStateDefault = false,
        // 所有状态显示StateView
        isShowStateViewAllStateDefault = true,
        // Loading显示StateViewReplaceView
        isLoadingShowStateViewReplaceView = true,
        // 请求
        asyncFun = asyncFun
    )
}