package com.bytedance.douyin.core.network.api

import com.bytedance.douyin.core.network.model.NetworkFakeGetHotVideo
import com.bytedance.douyin.core.network.util.BASE_URL_KAI_YAN_APP
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/6/18 下午15:49
 */
interface FakeNetworkVideoApi {

    /**
     * 热门视频
     * 文档：https://www.free-api.com/doc/516
     */
    @GET("$BASE_URL_KAI_YAN_APP/api/v4/discovery/hot")
    suspend fun getHotVideo(
        @Query("start") start: Int,
        @Query("num") num: Int,
    ): NetworkFakeGetHotVideo
}