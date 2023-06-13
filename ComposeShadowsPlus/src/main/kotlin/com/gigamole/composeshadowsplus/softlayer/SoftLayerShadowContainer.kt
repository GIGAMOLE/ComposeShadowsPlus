package com.gigamole.composeshadowsplus.softlayer

import android.os.Build
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Wraps [content] into [AndroidView] with layer type [View.LAYER_TYPE_SOFTWARE] to render shadows for Android devices with API < 28(P).
 *
 * For the best experience, use this container as a root [Composable] element.
 *
 * @param content The [Composable] content with soft layer type support.
 * @see softLayerShadow
 * @author GIGAMOLE
 */
@Composable
fun SoftLayerShadowContainer(content: @Composable () -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        AndroidView(
            factory = { context ->
                ComposeView(context).apply {
                    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                    setContent(content)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        content()
    }
}