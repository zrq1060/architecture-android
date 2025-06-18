package com.bytedance.douyin.core.data.di

import com.bytedance.douyin.core.data.repository.DefaultHomeRepository
import com.bytedance.douyin.core.data.repository.DefaultHomeTabsSortRepository
import com.bytedance.douyin.core.data.repository.DefaultLoginRepository
import com.bytedance.douyin.core.data.repository.DefaultMainRepository
import com.bytedance.douyin.core.data.repository.DefaultVideoRepository
import com.bytedance.douyin.core.data.repository.interfaces.HomeRepository
import com.bytedance.douyin.core.data.repository.interfaces.HomeTabsSortRepository
import com.bytedance.douyin.core.data.repository.interfaces.LoginRepository
import com.bytedance.douyin.core.data.repository.interfaces.MainRepository
import com.bytedance.douyin.core.data.repository.interfaces.VideoRepository
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
    fun bindsMainRepository(repository: DefaultMainRepository): MainRepository

    @Binds
    fun bindsHomeRepository(repository: DefaultHomeRepository): HomeRepository

    @Binds
    fun bindsHomeTabsSortRepository(repository: DefaultHomeTabsSortRepository): HomeTabsSortRepository

    @Binds
    fun bindsLoginRepository(repository: DefaultLoginRepository): LoginRepository

    @Binds
    fun bindsVideoRepository(repository: DefaultVideoRepository): VideoRepository
}