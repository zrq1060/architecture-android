package com.bytedance.douyin.core.data.repository

import com.bytedance.douyin.core.data.model.asExternalModel
import com.bytedance.douyin.core.data.repository.interfaces.VideoRepository
import com.bytedance.douyin.core.model.Video
import com.bytedance.douyin.core.network.datasource.interfaces.NetworkVideoDataSource
import javax.inject.Inject

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/6/18 下午15:49
 */
class DefaultVideoRepository @Inject constructor(
    private val network: NetworkVideoDataSource,
) : VideoRepository {

    override suspend fun getVideo(page: Int, size: Int): List<Video> {
        return network.getVideos(page, size).map { it.asExternalModel() }
    }
}