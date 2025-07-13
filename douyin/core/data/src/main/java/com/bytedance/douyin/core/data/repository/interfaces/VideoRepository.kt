package com.bytedance.douyin.core.data.repository.interfaces

import com.bytedance.douyin.core.data.repository.refreshloadmore.interfaces.RefreshLoadMoreRepository
import com.bytedance.douyin.core.model.Video

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/6/18 下午15:49
 */
interface VideoRepository : RefreshLoadMoreRepository<Video> {

    suspend fun getVideo(page: Int, size: Int): List<Video>

}