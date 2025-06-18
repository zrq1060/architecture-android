package com.bytedance.douyin.core.model


/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/6/18 下午15:49
 */
data class Video(
    // ID
    val id: Int? = null,
    // 播放地址
    val playUrl: String? = null,
    // 描述
    val description: String? = null,
    // 作者名
    val authorName: String? = null,
)