package com.bytedance.core.architecture.util

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bytedance.core.architecture.base.BaseViewModel
import com.bytedance.core.architecture.base.views.BaseViewsActivity
import com.bytedance.core.architecture.base.views.BaseViewsFragment
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/7/10 0:06
 */
/**
 * Fragment-viewModel的uiState，当其[mapFun]方法的值改变时（有去重），通知[flowCollector]改变。
 */
fun <UiState : Any, ViewModel : BaseViewModel<UiState>, Value> BaseViewsFragment<*, UiState, ViewModel>.uiStateMapValueChangedCollect(
    mapFun: (UiState) -> Value,
    flowCollector: FlowCollector<Value>
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.uiState.map { mapFun(it) }.distinctUntilChanged()
            .flowWithLifecycle(viewLifecycleOwner.lifecycle).collect(flowCollector)
    }
}

/**
 * Activity-viewModel的uiState，当其[mapFun]方法的值改变时（有去重），通知[flowCollector]改变。
 */
fun <UiState : Any, ViewModel : BaseViewModel<UiState>, Value> BaseViewsActivity<*, UiState, ViewModel>.uiStateMapValueChangedCollect(
    mapFun: (UiState) -> Value,
    flowCollector: FlowCollector<Value>
) {
    lifecycleScope.launch {
        viewModel.uiState.map { mapFun(it) }.distinctUntilChanged()
            .flowWithLifecycle(lifecycle).collect(flowCollector)
    }
}