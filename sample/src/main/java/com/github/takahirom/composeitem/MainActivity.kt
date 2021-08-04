package com.github.takahirom.composeitem

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.BindableItem
import com.github.takahirom.composeitem.databinding.ActivityMainBinding
import com.github.takahirom.composeitem.databinding.ItemHeaderBinding
import com.github.takahirom.composeitem.ui.theme.ComposeingroupieTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

val uiStateFlow = MutableStateFlow(listOf("this", "is", "a", "compose", "item", "!!!!"))

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val groupAdapter = GroupAdapter<com.xwray.groupie.GroupieViewHolder>()
    val binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.recyclerView.adapter = groupAdapter

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        uiStateFlow
          .collect {
            val items = listOf(
              HeaderItem("This is BindableItem"),
            ) + it.map { TextComposeItem(it) }
            groupAdapter.update(items)
          }
      }
    }
  }
}

class HeaderItem(private val text: String) : BindableItem<ItemHeaderBinding>() {
  override fun getLayout() = R.layout.item_header

  override fun bind(viewBinding: ItemHeaderBinding, position: Int) {
    viewBinding.textView.text = text
  }

  override fun initializeViewBinding(view: View): ItemHeaderBinding {
    return ItemHeaderBinding.bind(view)
  }
}

class TextComposeItem(val text: String) : ComposeItem() {
  @Composable
  override fun Content(position: Int) {
    Text(text = text)
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  ComposeingroupieTheme {
    TextComposeItem(text = "Hello!!!!!").Content(0)
  }
}