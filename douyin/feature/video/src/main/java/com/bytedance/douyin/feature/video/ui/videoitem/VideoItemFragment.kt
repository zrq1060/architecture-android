package com.bytedance.douyin.feature.video.ui.videoitem

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_AVAILABLE_COMMANDS_CHANGED
import androidx.media3.common.Player.EVENT_IS_PLAYING_CHANGED
import androidx.media3.common.Player.EVENT_PLAYBACK_STATE_CHANGED
import androidx.media3.common.Player.EVENT_PLAY_WHEN_READY_CHANGED
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_IDLE
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import com.bytedance.douyin.core.architecture.app.views.AppViewsFragment
import com.bytedance.douyin.core.model.VideoItem
import com.bytedance.douyin.feature.video.databinding.DouyinFeatureVideoFragmentVideoItemBinding as ViewBinding
import com.bytedance.douyin.feature.video.ui.videoitem.VideoItemUiState as UiState
import com.bytedance.douyin.feature.video.ui.videoitem.VideoItemViewModel as ViewModel


/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/4/2 下午3:33
 */
class VideoItemFragment : AppViewsFragment<ViewBinding, UiState, ViewModel>() {
    private var player: Player? = null
    private val handler: Handler = Handler(Looper.getMainLooper())

    // 更新进度Runnable
    private var updateProgressAction: Runnable = Runnable { updateProgress() }

    override val viewModel: ViewModel by viewModels()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = ViewBinding.inflate(inflater, container, false)

    override fun ViewBinding.initViews() {
        // 创建Player
        player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
            playerView.player = exoPlayer
            exoPlayer.setMediaItem(MediaItem.fromUri(viewModel.uiState.value.playUrl ?: ""))
            exoPlayer.prepare()
        }
    }

    @OptIn(UnstableApi::class)
    override fun ViewBinding.initListeners() {
        // 默认显示黑屏问题不能通过PlayerView显示隐藏来控制，所以改成了alpha控制。
        playerView.alpha = 0f
        player?.addListener(object : Player.Listener {
            // 第一帧
            override fun onRenderedFirstFrame() {
                playerView.alpha = 1f
            }

            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                if (events.containsAny(
                        EVENT_PLAYBACK_STATE_CHANGED,
                        EVENT_PLAY_WHEN_READY_CHANGED,
                        EVENT_AVAILABLE_COMMANDS_CHANGED
                    )
                ) {
                    // 更新播放按钮
                    updatePlayButton()
                }
                if (events.containsAny(
                        EVENT_PLAYBACK_STATE_CHANGED,
                        EVENT_PLAY_WHEN_READY_CHANGED,
                        EVENT_IS_PLAYING_CHANGED,
                        EVENT_AVAILABLE_COMMANDS_CHANGED
                    )
                ) {
                    // 更新进度
                    updateProgress()
                }
            }
        })

        // 进度改变
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 取消进度
                removeProgressUpdate()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 拖拽结束，跳到此位置。
                player?.seekTo(seekBar?.progress?.toLong() ?: 0)
                // 更新进度
                updateProgress()
            }
        })
        // 播放按钮，点击播放、暂停。
        playPause.setOnClickListener {
            Util.handlePlayPauseButtonAction(player, true)
        }
        // PlayerView，点击播放、暂停。
        playerView.setOnClickListener {
            Util.handlePlayPauseButtonAction(player, true)
        }
    }

    override fun ViewBinding.initObservers() {

    }

    @SuppressLint("SetTextI18n")
    override fun ViewBinding.onUiStateCollect(uiState: UiState) {
        // 设置值
        authorName.text = "@${uiState.authorName}"
        description.text = uiState.description
    }

    override fun onResume() {
        super.onResume()
        // 播放
        play()
    }

    override fun onPause() {
        super.onPause()
        // 保存状态
        viewModel.saveState(player?.currentPosition ?: 0)
        // 暂停
        pause()
        // 取消进度，因为onResume播放时，会收到播放的改变监听，会重新更新进度。
        removeProgressUpdate()
    }

    override fun onStop() {
        super.onStop()
        // 如果滑动特别快，onResume、onPause不执行，onStart、onStop一定执行。兼容onPause没移除的情况。
        removeProgressUpdate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 释放
        release()
    }

    private fun play() = player?.apply {
        repeatMode = ExoPlayer.REPEAT_MODE_ONE
        // 使用ViewModel的位置，支持屏幕旋转、以及onStop的释放。
        seekTo(viewModel.uiState.value.currentPosition ?: 0)
        // 没直接使用播放，而是使用此方法播放，是为了解决列表播放失败时，没调用准备。
        Util.handlePlayButtonAction(player)
    }

    private fun pause() = player?.apply {
        pause()
    }

    private fun release() {
        player?.release()
        player = null
        binding?.playerView?.player = null
    }

    // 更新进度
    private fun updateProgress() {
        val player = player ?: return
        val seekBar = binding?.seekBar ?: return
        // 当前位置
        val position = player.currentPosition
        // 总时间
        val duration = player.duration
        // 这里没换成百分比，而是直接设置的具体时长，其会自动计算出。
        seekBar.max = duration.toInt()
        seekBar.progress = position.toInt()

        // 取消进度
        removeProgressUpdate()
        // 开启进度，只有在播放中，或者不是空闲或者结束，则开启更新。
        val playbackState = player.playbackState
        if (player.isPlaying) {
            startProgressUpdate()
        } else if (playbackState != STATE_ENDED && playbackState != STATE_IDLE) {
            startProgressUpdate()
        }
    }

    private fun startProgressUpdate() {
        if (!isResumed) return // 不可见，页面不开始更新进度。
        handler.postDelayed(updateProgressAction, MAX_UPDATE_INTERVAL_MS)
    }

    private fun removeProgressUpdate() {
        handler.removeCallbacks(updateProgressAction)
    }

    // 更新播放按钮
    @OptIn(UnstableApi::class)
    private fun updatePlayButton() {
        val shouldShowPlayButton = Util.shouldShowPlayButton(player, true)
        // 当前可见，并且要展示播放按钮，才展示播放按钮。
        binding?.playPause?.isVisible = isResumed && shouldShowPlayButton
    }

    companion object {
        const val KEY_ITEM = "item"
        const val MAX_UPDATE_INTERVAL_MS: Long = 1000

        internal fun newInstance(item: VideoItem) = VideoItemFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_ITEM, item)
            }
        }
    }
}