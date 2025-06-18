package com.bytedance.douyin.core.data.model

import com.bytedance.douyin.core.model.Video
import com.bytedance.douyin.core.network.model.NetworkVideo

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/6/18 下午15:49
 */
// 视频，网络类->外部类。
fun NetworkVideo.asExternalModel() = Video(
    id,
    playUrl,
    description,
    authorName,
)