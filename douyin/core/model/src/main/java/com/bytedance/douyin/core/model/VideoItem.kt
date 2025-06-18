package com.bytedance.douyin.core.model

import com.bytedance.core.model.BaseFragmentStateDiffItem
import java.io.Serializable

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/3/1 下午2:23
 */
data class VideoItem(
    val id: Long,
    // 播放地址
    val playUrl: String? = null,
    // 描述
    val description: String? = null,
    // 作者名
    val authorName: String? = null,
) : BaseFragmentStateDiffItem , Serializable {
    override fun getPrimaryKey() = id

    override fun getItemId() = id
}