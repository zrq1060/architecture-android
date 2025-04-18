package com.bytedance.douyin.core.network.datasource

import com.bytedance.core.network.util.toRuleSuccessData
import com.bytedance.douyin.core.network.api.FakeNetworkLoginApi
import com.bytedance.douyin.core.network.datasource.interfaces.NetworkLoginDataSource
import com.bytedance.douyin.core.network.model.NetworkUser
import com.bytedance.douyin.core.network.model.asNetworkUser
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/9/26 上午11:21
 */
@Singleton
class FakeNetworkLoginDataSource @Inject constructor(
    retrofit: Retrofit,
) : NetworkLoginDataSource {
    // 创建Api
    private val loginApi = retrofit.create(FakeNetworkLoginApi::class.java)

    override suspend fun sendLoginPhoneNumberVerificationCode(phoneNumber: String) {
        // 模拟Api请求
        mockApiRequest()
    }

    override suspend fun loginByPhoneNumberAndVerificationCode(
        phoneNumber: String,
        verificationCode: String,
    ): NetworkUser {
        return mockLogin(phoneNumber, verificationCode)
    }

    override suspend fun loginByPhoneNumberAndPassword(
        phoneNumber: String,
        password: String,
    ): NetworkUser {
        return mockLogin(phoneNumber, password)
    }

    override suspend fun loginByEmailAndPassword(email: String, password: String): NetworkUser {
        return mockLogin(email, password)
    }

    override suspend fun sendRetrievePasswordPhoneNumberVerificationCode(phoneNumber: String) {
        // 模拟Api请求
        mockApiRequest()
    }

    override suspend fun verifyRetrievePasswordByPhoneNumberAndVerificationCode(
        phoneNumber: String,
        verificationCode: String,
    ) {
        mockLogin(phoneNumber, verificationCode)
    }

    override suspend fun retrievePasswordNewPassword(
        phoneNumber: String,
        password: String,
    ): NetworkUser {
        return mockLogin(phoneNumber, password)
    }

    private suspend fun mockLogin(phoneNumber: String, password: String): NetworkUser =
        loginApi.login("309324904@qq.com", password).toRuleSuccessData().asNetworkUser()

    private suspend fun mockApiRequest() {
        loginApi.sentences().toRuleSuccessData()
    }
}
