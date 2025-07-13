package com.bytedance.douyin.core.designsystem.startup

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.bytedance.core.common.util.asUnsafe
import com.bytedance.douyin.core.designsystem.util.DynamicColorManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/12/4 上午11:05
 */
class DesignSystemInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        // 设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { viewContext, _ ->
            ClassicsHeader(viewContext)
        }
        // 设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { viewContext, _ ->
            ClassicsFooter(viewContext)
        }
        // 动态颜色
        DynamicColorManager.init(context.asUnsafe<Application>())
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}