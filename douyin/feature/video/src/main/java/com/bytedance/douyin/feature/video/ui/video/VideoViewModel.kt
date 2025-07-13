package com.bytedance.douyin.feature.video.ui.video

import com.bytedance.douyin.core.architecture.app.AppViewModel
import com.bytedance.douyin.core.data.repository.interfaces.VideoRepository
import com.bytedance.douyin.core.data.repository.refreshloadmore.interfaces.RefreshRepositoryOwner
import com.bytedance.douyin.core.model.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.bytedance.douyin.feature.video.ui.video.VideoUiState as UiState

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/12/24 11:10
 */
@HiltViewModel
class VideoViewModel @Inject constructor(private val videoRepository: VideoRepository) :
    AppViewModel<UiState>(), RefreshRepositoryOwner {

    override fun onRefreshRepository() = videoRepository

    override val uiStateInitialValue = UiState()

    override val uiStateFlow: Flow<UiState> = videoRepository.result.map { list ->
        UiState(list?.map { item ->
            VideoItem(
                item.id?.toLong() ?: 0,
                item.playUrl,
                item.description,
                item.authorName
            )
        })
    }

    // 刷新
    fun refresh() {
        videoRepository.refresh()
    }

    // 加载
    fun load() {
        videoRepository.load()
    }
}

// Video-UiState
data class VideoUiState(
    val list: List<VideoItem>? = null,
)