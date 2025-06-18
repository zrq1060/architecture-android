package com.bytedance.douyin.feature.video.ui.video

import androidx.fragment.app.Fragment
import com.bytedance.douyin.core.architecture.app.views.adapter.AppFragmentStateAdapter
import com.bytedance.douyin.core.model.VideoItem
import com.bytedance.douyin.feature.video.ui.videoitem.VideoItemFragment

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/4/1 上午9:15
 */
class VideoFragmentStateAdapter(fragment: Fragment) :
    AppFragmentStateAdapter<VideoItem>(fragment, false) {

    override fun createFragment(position: Int, item: VideoItem): Fragment {
        return VideoItemFragment.newInstance(item)
    }
}