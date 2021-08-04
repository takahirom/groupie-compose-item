# groupie-compose-item

You can treat Compose as a regular Groupie item!

This library is very simple and small. You can check source the [code](https://github.com/takahirom/groupie-compose-item/blob/main/composeitem/src/main/java/com/github/takahirom/composeitem/ComposeItem.kt).

```kotlin
class TextComposeItem(val text: String) : ComposeItem() {
  @Composable
  override fun Content(position: Int) {
    Text(text = text)
  }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
  ComposeinGroupieTheme {
    TextComposeItem(text = "Hello!!!!!").Content(0)
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
