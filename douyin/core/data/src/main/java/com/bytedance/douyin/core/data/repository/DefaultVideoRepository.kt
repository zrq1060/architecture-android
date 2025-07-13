package com.bytedance.douyin.core.data.repository

import com.bytedance.douyin.core.data.model.asExternalModel
import com.bytedance.douyin.core.data.repository.interfaces.VideoRepository
import com.bytedance.douyin.core.data.repository.refreshloadmore.PageKeyedMemoryRefreshLoadMoreRepository
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
) : PageKeyedMemoryRefreshLoadMoreRepository<Video>(), VideoRepository {

    override suspend fun getVideo(page: Int, size: Int): List<Video> {
        return network.getVideos(page, size).map { it.asExternalModel() }
    }

    override suspend fun getListDataByKey(key: Int, pageSize: Int): List<Video> {
        return getVideo(key, pageSize)
    }
}