package com.bytedance.douyin.shop.core.data.di

import com.bytedance.douyin.shop.core.data.repository.DefaultShopRepository
import com.bytedance.douyin.shop.core.data.repository.interfaces.ShopRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/8/23 上午10:53
 */
@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsShopRepository(repository: DefaultShopRepository): ShopRepository
}