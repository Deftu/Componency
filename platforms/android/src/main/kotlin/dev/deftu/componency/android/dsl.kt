package dev.deftu.componency.android

import android.app.Activity
import dev.deftu.componency.android.platform.AndroidPlatform
import dev.deftu.componency.components.Component

public fun Activity.setContent(builder: AndroidPlatform.() -> Component<*, *>) {
    val view = ComponencyView(this, null).apply {
        platform = AndroidPlatform(this, context = this@setContent)
        setContent {
            platform.builder()
        }
    }

    setContentView(view)
}
