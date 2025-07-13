package com.bytedance.douyin.feature.video.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bytedance.douyin.core.architecture.app.views.AppViewsFragment
import com.bytedance.douyin.core.architecture.util.setDataOrAdapter
import com.bytedance.douyin.core.common.interfaces.OnFragmentBackgroundListener
import com.bytedance.douyin.core.common.interfaces.OnTabClickRefreshFinishListener
import com.bytedance.douyin.core.common.interfaces.OnTabClickRefreshListener
import com.bytedance.douyin.core.common.util.setStatusBarDarkFont
import com.bytedance.douyin.core.data.repository.refreshloadmore.LoadState
import com.bytedance.douyin.feature.video.ui.video.VideoUiState
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zrq.test.point.annotation.TestEntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.bytedance.douyin.feature.video.databinding.DouyinFeatureVideoFragmentVideoBinding as ViewBinding
import com.bytedance.douyin.feature.video.ui.video.VideoUiState as UiState
import com.bytedance.douyin.feature.video.ui.video.VideoViewModel as ViewModel

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/4/1 下午3:33
 */
@TestEntryPoint("视频")
@AndroidEntryPoint
class VideoFragment : AppViewsFragment<ViewBinding, UiState, ViewModel>(),
    OnFragmentBackgroundListener, OnTabClickRefreshListener {
    private var onTabClickListener: OnTabClickRefreshFinishListener? = null
    private val fragmentStateAdapter by lazy { VideoFragmentStateAdapter(this) }

    override val viewModel: ViewModel by viewModels()

    // 背景颜色为亮色
    override var isBackgroundBright: Boolean = true

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = ViewBinding.inflate(inflater, container, false)

    override fun ViewBinding.initWindowInsets() {
        // 不设置底部Tab
        ViewCompat.setOnApplyWindowInsetsListener(smartRefreshLayout) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.displayCutout())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                0
            )
            insets
        }
    }

    override fun ViewBinding.initViews() {
        // 支持刷新加载
        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.setEnableLoadMore(true)
        // 解决底部加载成功后，内容向上偏移。
        smartRefreshLayout.setEnableScrollContentWhenLoaded(false)
    }

    override fun ViewBinding.initListeners() {
        // 刷新、加载
        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                // 刷新
                viewModel.refresh()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                // 加载
                viewModel.load()
            }
        })
        // 刷新加载状态观察
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.onRefreshRepository().loadState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { loadState ->
                    onLoadStateCollect(loadState, smartRefreshLayout)
                }
        }

    }

    private fun onLoadStateCollect(
        loadState: LoadState?,
        smartRefreshLayout: SmartRefreshLayout
    ) {
        if (loadState?.isRefresh == true && (loadState is LoadState.Success || loadState is LoadState.Error)) {
            // 刷新（成功、失败）
            smartRefreshLayout.finishRefresh(loadState is LoadState.Success)
            // -通知点击刷新完成，并断开链接。
            onTabClickListener?.onTabClickRefreshFinish()
            onTabClickListener = null
        } else if (loadState?.isRefresh == false && (loadState is LoadState.Success || loadState is LoadState.Error)) {
            // 加载（成功、失败）
            if (loadState is LoadState.Success) {
                // 成功
                smartRefreshLayout.finishLoadMore(0, true, loadState.isNoMoreData)
            } else {
                // 失败
                smartRefreshLayout.finishLoadMore(false)
            }
        }
    }

    override fun ViewBinding.initObservers() {
    }

    override fun ViewBinding.onUiStateCollect(uiState: VideoUiState) {
        viewPager2.setDataOrAdapter(uiState.list, 2) { fragmentStateAdapter }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarDarkFont(isDarkFont = isBackgroundBright)
    }

    override fun onTabClickRefresh(listener: OnTabClickRefreshFinishListener) {
        this.onTabClickListener = listener
        // 刷新
        viewModel.refresh()
    }

    companion object {
        internal fun newInstance() = VideoFragment()
    }
}