package com.github.takahirom.composeitem

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.xwray.groupie.Item

abstract class ComposeItem : Item<ComposeViewHolder> {
  constructor(id: Long) : super(id)
  constructor() : super()

  override fun createViewHolder(itemView: View): ComposeViewHolder {
    return ComposeViewHolder(itemView as ComposeView)
  }

  override fun unbind(viewHolder: ComposeViewHolder) {
    super.unbind(viewHolder)
    viewHolder.disposeComposition()
  }

  override fun bind(viewHolder: ComposeViewHolder, position: Int) {
    viewHolder.bind { Content(position) }
  }

  @Composable
  abstract fun Content(position: Int)

  override fun getLayout() = R.layout.item_compose
}

open class ComposeViewHolder(protected val composeView: ComposeView) :
  com.xwray.groupie.GroupieViewHolder(composeView) {
  init {
    composeView.setViewCompositionStrategy(
      ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
    )
  }

  fun bind(content: @Composable () -> Unit) {
    composeView.setContent {
      content()
    }
  }

  fun disposeComposition() {
    composeView.disposeComposition()
  }
}
