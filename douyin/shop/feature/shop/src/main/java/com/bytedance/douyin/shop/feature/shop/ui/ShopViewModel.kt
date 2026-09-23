package com.bytedance.douyin.shop.feature.shop.ui

import com.bytedance.core.common.util.asUnsafe
import com.bytedance.core.model.BaseDiffItem
import com.bytedance.douyin.core.architecture.app.AppViewModel
import com.bytedance.douyin.core.architecture.util.requestAsyncErrorHint
import com.bytedance.douyin.core.architecture.util.requestAsyncShowAllState
import com.bytedance.douyin.core.data.repository.refreshloadmore.interfaces.RefreshRepositoryOwner
import com.bytedance.douyin.shop.core.data.repository.interfaces.ShopRepository
import com.bytedance.douyin.shop.core.model.Shop
import com.bytedance.douyin.shop.core.model.ShopButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.bytedance.douyin.shop.feature.shop.ui.ShopUiState as UiState

/**
 * 描述：
 *
 * @author zhangrq
 * createTime 2024/9/5 下午5:35
 */
@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
) : AppViewModel<UiState>(), RefreshRepositoryOwner {

    override fun onRefreshRepository() = shopRepository

    override val uiStateInitialValue: UiState = UiState()

    // UiState的Flow
    override val uiStateFlow: Flow<UiState> = shopRepository.result.map { listData ->
        val map = listData?.map { item ->
            // 顶部按钮数据及其点击
            val buttons = item.buttons?.map { shopButton ->
                ShopButtonUiState(shopButton = shopButton, addItem = {
                    requestAsyncShowAllState {
                        shopRepository.addButtonItem(shopButton.id)
                    }
                }, deleteItem = {
                    requestAsyncShowAllState {
                        shopRepository.deleteButtonItem(shopButton.id)
                    }
                }, updateItem = {
                    requestAsyncShowAllState {
                        shopRepository.updateButtonItemTitle(
                            shopButton.id,
                            getUpdateTitle(shopButton.title ?: "")
                        )
                    }
                })
            }
            // 其它Item数据及其点击
            ShopItemUiState(type = item.type, buttons = buttons, shop = item, addItem = {
                requestAsyncErrorHint {
                    shopRepository.addItem(item.id ?: 0)
                }
            }, deleteItem = {
                requestAsyncShowAllState {
                    shopRepository.deleteItem(item.id ?: 0)
                }
            }, updateItem = {
                requestAsyncErrorHint {
                    shopRepository.updateItemTitle(item.id ?: 0, getUpdateTitle(item.title ?: ""))
                }
            })
        }
        UiState(items = map)
    }

    fun refresh(finish: () -> Unit) {
        shopRepository.refresh(finish)
    }

    private fun getUpdateTitle(title: String) =
        if (title.length > 500) title else "${title}${title}"
}

data class ShopUiState(
    val items: List<ShopItemUiState>? = null,
)

data class ShopItemUiState(
    // 1：按钮，2：商品，3：活动
    val type: Int,
    val buttons: List<ShopButtonUiState>?, // type为1用
    val shop: Shop?, // type为非1用

    // item的点击
    val addItem: (() -> Unit),
    val deleteItem: (() -> Unit),
    val updateItem: (() -> Unit),
) : BaseDiffItem {
    override fun getPrimaryKey() = shop?.id ?: 0 // type为非1用id，否则则为按钮列表用默认0。

    override fun <T> areContentsTheSame(newItem: T): Boolean {
        val itemUiState = newItem.asUnsafe<ShopItemUiState>()
        // 类型为1为按钮，比较buttons按钮列表ShopButtonUiState除点击之外的其他的属性。类型为其他，则比较shop内容。
        return if (type == 1) (buttons?.map { it.shopButton } == itemUiState.buttons?.map { it.shopButton }) else shop == itemUiState.shop
    }
}

data class ShopButtonUiState(
    val shopButton: ShopButton,
    // button的点击
    val addItem: (() -> Unit),
    val deleteItem: (() -> Unit),
    val updateItem: (() -> Unit),
) : BaseDiffItem {

    override fun getPrimaryKey() = shopButton.id.toString()

    override fun <T> areContentsTheSame(newItem: T): Boolean {
        val itemUiState = newItem.asUnsafe<ShopButtonUiState>()
        return shopButton == itemUiState.shopButton
    }
}