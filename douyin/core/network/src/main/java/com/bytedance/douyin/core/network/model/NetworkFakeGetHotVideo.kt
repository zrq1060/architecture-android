package com.bytedance.douyin.core.network.model

import kotlinx.serialization.Serializable

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/6/18 下午15:49
 */
@Serializable
data class NetworkFakeGetHotVideo(
    val itemList: List<NetworkFakeGetHotVideoItem> = listOf(),
)

@Serializable
data class NetworkFakeGetHotVideoItem(
    val `data`: NetworkFakeGetHotVideoItemData = NetworkFakeGetHotVideoItemData(),
)

@Serializable
data class NetworkFakeGetHotVideoItemData(
    val author: NetworkFakeGetHotVideoItemDataAuthor = NetworkFakeGetHotVideoItemDataAuthor(),
    val id: Int = 0,
    // 播放地址
    val playUrl: String = "",
    // 描述
    val description: String = "",
)

@Serializable
data class NetworkFakeGetHotVideoItemDataAuthor(
    // 作者名
    val name: String = "",
)