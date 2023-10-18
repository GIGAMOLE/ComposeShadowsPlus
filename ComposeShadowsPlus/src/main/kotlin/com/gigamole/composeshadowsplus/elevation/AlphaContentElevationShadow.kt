package com.gigamole.composeshadowsplus.elevation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.gigamole.composeshadowsplus.common.ShadowsPlusDefaults
import com.gigamole.composeshadowsplus.common.clipShadowByPath

/**
 * The container which clips the elevation shadow by the [shape] below and wraps the [content] (for alpha/transparency usage) above.
 *
 * @param modifier The wrapper Modifier.
 * @param elevation The shadow elevation.
 * @param shape The shadow shape.
 * @param clip When active, the shadow content drawing clips to the shape.
 * @param ambientColor The shadow ambient color.
 * @param spotColor The shadow spot color.
 * @param content The content over the clipped shadow.
 */
@Composable
fun AlphaContentElevationShadow(
    modifier: Modifier = Modifier,
    elevation: Dp = ShadowsPlusDefaults.ShadowRadius,
    shape: Shape = ShadowsPlusDefaults.ShadowShape,
    clip: Boolean = elevation > 0.dp,
    ambientColor: Color = ElevationShadowDefaults.ShadowColor,
    spotColor: Color = ElevationShadowDefaults.ShadowColor,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var outerSize by remember { mutableStateOf(DpSize.Zero) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .requiredSize(size = outerSize)
                .drawWithCache {
                    val shapePath = Path()
                    var lastSize: Size? = null

                    fun updatePathIfNeeded() {
                        if (size != lastSize) {
                            shapePath.reset()
                            shapePath.addOutline(
                                outline = shape.createOutline(
                                    size = size,
                                    layoutDirection = LayoutDirection.Rtl,
                                    density = this
                                )
                            )

                            lastSize = size
                        }
                    }

                    onDrawWithContent {
                        updatePathIfNeeded()
                        clipShadowByPath(
                            path = shapePath,
                            block = {
                                this@onDrawWithContent.drawContent()
                            }
                        )
                    }
                }
                .shadow(
                    elevation = elevation,
                    shape = shape,
                    clip = clip,
                    ambientColor = ambientColor,
                    spotColor = spotColor
                )
        )
        Box(
            modifier = Modifier
                .wrapContentSize(align = Alignment.Center)
                .onPlaced {
                    outerSize = with(density) {
                        it.size
                            .toSize()
                            .toDpSize()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}