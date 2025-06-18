package com.bytedance.douyin.core.network.datasource

import com.bytedance.douyin.core.network.api.FakeNetworkVideoApi
import com.bytedance.douyin.core.network.datasource.interfaces.NetworkVideoDataSource
import com.bytedance.douyin.core.network.model.NetworkFakeGetHotVideoItemData
import com.bytedance.douyin.core.network.model.NetworkVideo
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 描述：由于没有真正的视频api接口，所以网络请求用此模拟。
 *
 * @author zhangrq
 * createTime 2025/6/18 下午15:49
 */
@Singleton
class FakeNetworkVideoDataSource @Inject constructor(
    retrofit: Retrofit,
) : NetworkVideoDataSource {

    // 创建Api
    private val videoApi = retrofit.create(FakeNetworkVideoApi::class.java)

    override suspend fun getVideos(page: Int, size: Int): List<NetworkVideo> {
        val list = videoApi.getHotVideo((page - 1) * size + 1, size).itemList.map { it.data }
        return list.map { it.asNetworkShop() }
    }

    private fun NetworkFakeGetHotVideoItemData.asNetworkShop(
    ) = NetworkVideo(
        id = id,
        playUrl = playUrl.replaceHttp(),
        description = description,
        authorName = author.name,
    )

    private fun String.replaceHttp(): String = replace("http:", "https:")

}