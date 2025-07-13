package com.bytedance.douyin.feature.video.ui.videoitem

import androidx.lifecycle.SavedStateHandle
import com.bytedance.douyin.core.architecture.app.AppViewModel
import com.bytedance.douyin.core.model.VideoItem
import com.bytedance.douyin.feature.video.ui.videoitem.VideoItemFragment.Companion.KEY_ITEM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.bytedance.douyin.feature.video.ui.videoitem.VideoItemUiState as UiState

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/12/24 11:10
 */
@HiltViewModel
class VideoItemViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    AppViewModel<UiState>() {
    // 获取传入数据
    private val videoItem: VideoItem = savedStateHandle[KEY_ITEM]!!

    // 当前时间，以支持在屏幕旋转后还原。
    private var currentPosition = MutableStateFlow(0L)

    override val uiStateInitialValue =
        UiState(videoItem.id, videoItem.playUrl, videoItem.description, videoItem.authorName)

    override val uiStateFlow: Flow<UiState> = currentPosition.map {
        // 当前时间修改，其他的没变。
        uiStateInitialValue.copy(currentPosition = it)
    }

    // 保存状态
    fun saveState(currentPosition: Long) {
        this.currentPosition.value = currentPosition
    }
}

// VideoItem-UiState
data class VideoItemUiState(
    val id: Long,
    // 播放地址
    val playUrl: String? = null,
    // 描述
    val description: String? = null,
    // 作者名
    val authorName: String? = null,
    // 播放位置
    val currentPosition: Long? = null,
)