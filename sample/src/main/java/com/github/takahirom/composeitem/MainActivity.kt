package com.github.takahirom.composeitem

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.takahirom.composeitem.sample.R
import com.github.takahirom.composeitem.sample.databinding.ActivityMainBinding
import com.github.takahirom.composeitem.sample.databinding.ItemHeaderBinding
import com.github.takahirom.composeitem.ui.theme.ComposeingroupieTheme
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

val uiStateFlow = MutableStateFlow<List<String>>(listOf("1"))

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val groupAdapter = GroupAdapter<com.xwray.groupie.GroupieViewHolder>()
    val binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.recyclerView.adapter = groupAdapter


    lifecycleScope.launch {
      while (true) {
        val value = uiStateFlow.value
        uiStateFlow.value = value + (value.last().toInt() + 1).toString()
        delay(1000)
      }
    }

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        uiStateFlow
          .collect {
            val items = mutableListOf<Group>()
            items += listOf(
              HeaderItem("This is BindableItem"),
            )
            items += it.map { TextComposeItem(it) }
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

class TextComposeItem(val text: String) :
  ComposeItem<TextComposeItem.Binding>(text.hashCode().toLong()) {
  class Binding : ComposeBinding {
    var text by mutableStateOf("")

    @Composable
    override fun Content() {
      val firstText = remember { text }
      LaunchedEffect(key1 = text) {
        println("createTimeText:$firstText current:$text")
      }
      DisposableEffect(key1 = Unit) {
        onDispose {
          println("dispose:$text")
        }
      }
      Item(text)
    }
  }

  override fun composeBinding(): Binding {
    return Binding()
  }

  override fun bind(composeBinding: Binding, position: Int) {
    composeBinding.text = text
  }

  override fun hasSameContentAs(other: Item<*>): Boolean {
    return (other as? TextComposeItem)?.text == text
  }
}

@Composable
fun Item(text: String) {
  Text(text = text, style = MaterialTheme.typography.h1)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  ComposeingroupieTheme {
    val textComposeItem = TextComposeItem(text = "Hello!!!!!")
    val composeBinding = textComposeItem.composeBinding()
    textComposeItem.bind(composeBinding, 0)
    composeBinding.Content()
  }
}