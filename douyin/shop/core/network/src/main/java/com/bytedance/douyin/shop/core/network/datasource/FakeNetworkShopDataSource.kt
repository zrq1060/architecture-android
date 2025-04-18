package com.bytedance.douyin.shop.core.network.datasource

import com.bytedance.core.network.util.toRuleSuccessData
import com.bytedance.douyin.shop.core.network.api.FakeNetworkShopApi
import com.bytedance.douyin.shop.core.network.datasource.interfaces.NetworkShopDataSource
import com.bytedance.douyin.shop.core.network.model.NetworkFakeGetHotVideoItemData
import com.bytedance.douyin.shop.core.network.model.NetworkShop
import com.bytedance.douyin.shop.core.network.model.NetworkShopButton
import retrofit2.Retrofit
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 描述：由于没有真正的商城api接口，所以网络请求用此模拟。
 *
 * @author zhangrq
 * createTime 2024/9/26 上午11:21
 */
@Singleton
class FakeNetworkShopDataSource @Inject constructor(
    retrofit: Retrofit,
) : NetworkShopDataSource {

    // 创建Api
    private val shopApi = retrofit.create(FakeNetworkShopApi::class.java)

    override suspend fun getShops(page: Int, size: Int): List<NetworkShop> {
        // 获取网络数据
        val list = shopApi.getHotVideo((page - 1) * size + 1, size).itemList.map { it.data }
        // 维护模拟结果
        val items = mutableListOf<NetworkShop>()
        // 增加顶部Buttons
        addTopButtons(page, items)
        // 增加非顶部Buttons
        items.addAll(list.map { it.asNetworkShop() })
        return items
    }

    override suspend fun addItem(): NetworkShop {
        // 模拟Api请求
        val first = shopApi.getHotVideo(1, 1).itemList.map { it.data }.first()
        return first.asNetworkShop(Random().nextInt(10000))
    }

    override suspend fun deleteItem(id: Int) {
        // 模拟Api请求
        mockApiRequest()
    }

    override suspend fun updateItemTitle(id: Int, title: String) {
        // 模拟Api请求
        mockApiRequest()
    }

    override suspend fun addButtonItem(): NetworkShopButton {
        // 模拟Api请求
        mockApiRequest()
        return fakeShopButton(Random().nextInt(10000), "Add-Item")
    }

    override suspend fun deleteButtonItem(id: Int) {
        // 模拟Api请求
        mockApiRequest()
    }

    override suspend fun updateButtonItemTitle(id: Int, title: String) {
        // 模拟Api请求
        mockApiRequest()
    }

    private suspend fun mockApiRequest() {
        shopApi.getTime().toRuleSuccessData()
    }

    private fun addTopButtons(
        page: Int,
        items: MutableList<NetworkShop>,
    ) {
        if (page == 1) {
            // 只有第一页增加
            val buttonTitles = listOf(
                "我的订单", "充值中心", "购物消息", "抖音超市", "退款/售后", "好评价", "逛全球"
            )
            val buttons = mutableListOf<NetworkShopButton>()

            for (i in 0..20) {
                val title = if (i < buttonTitles.size) buttonTitles[i] else "Item$i"
                buttons.add(fakeShopButton(i, title))
            }
            // add buttons
            items.add(NetworkShop(type = 1, buttons = buttons))
        }
    }

    private fun NetworkFakeGetHotVideoItemData.asNetworkShop(
        id: Int = this.id,
    ) = NetworkShop(
        if (id % 4 == 2) 3 else 2,
        null,
        id,
        // 替换，解决此app不支持明文问题。
        cover.feed.replaceHttp(),
        cover.blurred.replaceHttp(),
        title,
        id / 100.0,
        id.toString()
    )

    private fun fakeShopButton(id: Int, title: String) = NetworkShopButton(
        id,
        title,
        ""
    )

    private fun String.replaceHttp(): String = replace("http:", "https:")

}