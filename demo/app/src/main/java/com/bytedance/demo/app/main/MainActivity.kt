package com.bytedance.demo.app.main

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.bytedance.douyin.core.architecture.app.views.AppViewsActivity
import dagger.hilt.android.AndroidEntryPoint
// 设置as别名，一般都是设置这几个。
// 使用别名后，此类的模板，下面的不需要改了，只需要改上面as这里即可。
import com.bytedance.demo.app.main.MainUiState as UiState
import com.bytedance.demo.app.main.MainViewModel as ViewModel
import com.bytedance.demo.databinding.ActivityMainBinding as ViewBinding

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2025/3/24 上午11:02
 */
@AndroidEntryPoint
class MainActivity : AppViewsActivity<ViewBinding, UiState, ViewModel>() {
    // 在父类AppViewsActivity中，可用反射实现（reflectViewModels()），省略此实现。
    override val viewModel: ViewModel by viewModels()

    // 在父类AppViewsActivity中，可用反射实现（reflectInflateViewBinding()），省略此实现。
    override fun inflateViewBinding(inflater: LayoutInflater) = ViewBinding.inflate(inflater)

    // 初始化View（可以在里面直接拿到页面布局控件）
    override fun ViewBinding.initViews() {
        // 设置TextView控件
        content.textSize = 50f
//        content.setTextColor(Color.BLACK)
    }

    // 初始化Listener（可以在里面直接拿到页面布局控件）
    override fun ViewBinding.initListeners() {
        // 设置TextView点击
        content.setOnClickListener {
            // 展示Toast，此Toast和页面的生命周期绑定，此页面关闭，Toast关闭。
            viewModel.showMessage("Long Toast", isShort = false)
        }
    }

    // 初始化Observer（可以在里面直接拿到页面布局控件），用于观察（收集）ViewModel内的暴露的属性值。
    override fun ViewBinding.initObservers() {
    }

    // 收集UiState的值（可以在里面直接拿到页面布局控件），用于设置当前页面的数据。
    override fun ViewBinding.onUiStateCollect(uiState: UiState) {
        // 设置TextView的值
        content.text = uiState.tabs?.joinToString()
    }
}