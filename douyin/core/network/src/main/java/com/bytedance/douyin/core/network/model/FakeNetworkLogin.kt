package com.bytedance.douyin.core.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2026/9/14 10:33
 */
@Serializable
data class FakeNetworkLoginRequest(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class FakeNetworkLoginResponse(
    val profile: FakeNetworkLoginProfile,
    val token: String
)

@Serializable
data class FakeNetworkLoginProfile(
    @SerialName("account_id")
    val id: Int,
    @SerialName("email")
    val account: String,
    @SerialName("created_at")
    val createdAt: String,
)

fun FakeNetworkLoginResponse.asNetworkUser() = NetworkUser(
    id = profile.id,
    token = token,
    account = profile.account,
    createdAt = profile.createdAt
)