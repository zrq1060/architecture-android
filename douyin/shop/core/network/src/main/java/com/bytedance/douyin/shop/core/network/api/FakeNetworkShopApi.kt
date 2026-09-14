package com.bytedance.douyin.shop.core.network.api

import com.bytedance.douyin.core.network.model.base.ApiOpenBaseNetworkModel
import com.bytedance.douyin.core.network.util.BASE_URL_KAI_YAN_APP
import com.bytedance.douyin.shop.core.network.model.NetworkFakeGetHotVideo
import com.bytedance.douyin.shop.core.network.model.NetworkFakeGetTime
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/9/26 上午11:29
 */
interface FakeNetworkShopApi {

    /**
     * 热门视频
     * 文档：https://www.free-api.com/doc/516
     */
    @GET("$BASE_URL_KAI_YAN_APP/api/v4/discovery/hot")
    suspend fun getHotVideo(
        @Query("start") start: Int,
        @Query("num") num: Int,
    ): NetworkFakeGetHotVideo

    /**
     * 获取时间
     */
    @GET("api/tools/timestamp")
    suspend fun getTime(): ApiOpenBaseNetworkModel<NetworkFakeGetTime>
}