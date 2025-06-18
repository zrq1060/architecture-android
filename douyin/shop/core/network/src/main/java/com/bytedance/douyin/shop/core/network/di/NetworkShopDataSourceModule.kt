package com.bytedance.douyin.shop.core.network.di

import com.bytedance.douyin.shop.core.network.datasource.FakeNetworkShopDataSource
import com.bytedance.douyin.shop.core.network.datasource.interfaces.NetworkShopDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/11/28 10:48
 */
@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkShopDataSourceModule {

    @Binds
    fun bindsNetworkShopDataSource(impl: FakeNetworkShopDataSource): NetworkShopDataSource
}