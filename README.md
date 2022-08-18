# groupie-compose-item

You can treat Compose as a regular Groupie item!

This library is very simple and small. You can check the [source code](https://github.com/takahirom/groupie-compose-item/blob/main/composeitem/src/main/java/com/github/takahirom/composeitem/ComposeItem.kt) based
on [Integrating Compose with your existing UI](https://developer.android.com/jetpack/compose/interop/compose-in-existing-ui#compose-recyclerview).

```kotlin
class TextComposeItem(val text: String) :
  ComposeItem<TextComposeItem2.Binding>(text.hashCode().toLong()) {
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
```

# How to use

```kotlin
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

```kotlin
dependencies {
  implementation 'com.github.takahirom.groupie-compose-item:composeitem:0.1.0'
}
```
