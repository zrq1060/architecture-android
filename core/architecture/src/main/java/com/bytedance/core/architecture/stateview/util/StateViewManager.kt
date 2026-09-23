package com.bytedance.core.architecture.stateview.util

import androidx.core.view.isInvisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.bytedance.core.architecture.base.BaseViewModel
import com.bytedance.core.architecture.stateview.LoadStateUiState
import com.bytedance.core.architecture.stateview.interfaces.DefaultStateViewManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/12/8 下午6:36
 */

/**
 * 初始化-加载状态UIState的收集
 */
fun DefaultStateViewManager.initLoadStateUiStateCollect(
    viewModel: BaseViewModel<*>,
    coroutineScope: CoroutineScope,
    lifecycle: Lifecycle,
) {
    coroutineScope.launch {
        viewModel.loadStateUiState
            .flowWithLifecycle(lifecycle)
            .collect { uiState ->
                collect(uiState)
            }
    }
}

private fun DefaultStateViewManager.collect(
    uiState: LoadStateUiState?,
) {
    uiState ?: return // 为空，不设置。

    // 设置是否展示StateView
    if (uiState.isShowStateView) {
        // 展示
        when (uiState) {
            is LoadStateUiState.Loading -> showLoading(uiState.isLoadingDialog)
            is LoadStateUiState.Error -> showErrorView(uiState.error, uiState.retry)
            is LoadStateUiState.Empty -> showEmptyView()
            is LoadStateUiState.Success -> showSuccess()
        }
    } else {
        // 不展示，隐藏所有（防止之前已经显示）。
        hideAll()
    }

    // 设置是否展示StateViewReplaceView（如：列表RecyclerView）
    val showStateViewReplaceView = uiState.isShowStateViewReplaceView ?: return // 为空，不设置。
    getStateViewReplaceView().isInvisible = !showStateViewReplaceView
}