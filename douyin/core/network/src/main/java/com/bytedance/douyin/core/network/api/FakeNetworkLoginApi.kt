package com.bytedance.douyin.core.network.api

import com.bytedance.douyin.core.network.model.FakeNetworkLoginRequest
import com.bytedance.douyin.core.network.model.FakeNetworkLoginResponse
import com.bytedance.douyin.core.network.model.FakeNetworkSentences
import com.bytedance.douyin.core.network.model.base.ApiOpenBaseNetworkModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/9/26 上午11:29
 */
interface FakeNetworkLoginApi {
    /**
     * 登录
     */
    @POST("api/auth/login")
    suspend fun login(
        @Body request: FakeNetworkLoginRequest
    ): ApiOpenBaseNetworkModel<FakeNetworkLoginResponse>

    /**
     * 一言名句
     */
    @GET("api/tools/famous-sentence")
    suspend fun sentences(): ApiOpenBaseNetworkModel<FakeNetworkSentences>
}