package com.bytedance.douyin.feature.video.ui.videoitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bytedance.douyin.core.architecture.app.views.AppViewsEmptyViewModelFragment
import com.bytedance.douyin.core.model.VideoItem
import com.bytedance.douyin.feature.video.databinding.DouyinFeatureVideoFragmentVideoItemBinding as ViewBinding

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/4/2 下午3:33
 */
class VideoItemFragment : AppViewsEmptyViewModelFragment<ViewBinding>() {
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = ViewBinding.inflate(inflater, container, false)

    override fun ViewBinding.initViews() {
        val videoItem = arguments?.getSerializable(KEY_ITEM) as VideoItem
        title.text = videoItem.authorName
    }

    override fun ViewBinding.initListeners() {

    }

    override fun ViewBinding.initObservers() {

    }

    companion object {
        private const val KEY_ITEM = "item"

        internal fun newInstance(item: VideoItem) = VideoItemFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_ITEM, item)
            }
        }
    }
}