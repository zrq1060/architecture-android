package com.bytedance.douyin.core.network.model.base

import com.bytedance.core.network.model.BaseNetworkModel
import kotlinx.serialization.Serializable

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/9/26 上午11:30
 */
@Serializable
data class ApiOpenBaseNetworkModel<T>(val code: Int, val message: String, val data: T? = null) :
    BaseNetworkModel<T> {
    override fun isRuleSuccess() = code == 200

    override fun code() = code
    override fun message() = message
    override fun data() = data
}