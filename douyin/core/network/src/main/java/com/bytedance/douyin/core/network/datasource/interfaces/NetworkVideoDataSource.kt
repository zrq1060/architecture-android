package com.bytedance.douyin.core.network.datasource.interfaces

import com.bytedance.douyin.core.network.model.NetworkVideo

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/6/18 下午15:49
 */
interface NetworkVideoDataSource {
    suspend fun getVideos(page: Int, size: Int): List<NetworkVideo>
}