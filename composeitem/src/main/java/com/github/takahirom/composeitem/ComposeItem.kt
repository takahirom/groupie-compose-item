package com.github.takahirom.composeitem

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.xwray.groupie.Item
import kotlin.reflect.KClass

abstract class ComposeItem<T : ComposeItem.ComposeBinding> : Item<ComposeViewHolder<T>> {
  constructor(id: Long) : super(id)
  constructor() : super()

  interface ComposeBinding {
    @Composable
    fun Content()
  }

  override fun createViewHolder(itemView: View): ComposeViewHolder<T> {

    val composeView = itemView as ComposeView
    composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)
    val composeBinding = composeBinding()
    composeView.setContent { composeBinding.Content() }
    return ComposeViewHolder(composeView, composeBinding)
  }

  open fun composeBinding(): T {
    return composeBindingClass().java.newInstance()
  }

  abstract fun composeBindingClass(): KClass<T>

  override fun bind(viewHolder: ComposeViewHolder<T>, position: Int) {
    bind(viewHolder.composeBinding, position)
  }

  abstract fun bind(composeBinding: T, position: Int)

  override fun getViewType(): Int {
    return composeBindingClass().hashCode()
  }

  override fun getLayout() = R.layout.item_compose
}

open class ComposeViewHolder<T : ComposeItem.ComposeBinding>(
  composeView: ComposeView,
  val composeBinding: T
) :
  com.xwray.groupie.GroupieViewHolder(composeView) {
  init {
    composeView.setViewCompositionStrategy(
      ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
    )
  }
}
