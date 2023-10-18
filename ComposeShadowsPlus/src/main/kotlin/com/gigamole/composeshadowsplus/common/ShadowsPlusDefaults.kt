package com.gigamole.composeshadowsplus.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * ComposeShadowsPlus default values.
 *
 * @see shadowsPlus
 * @see com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
 * @see com.gigamole.composeshadowsplus.softlayer.softLayerShadow
 * @author GIGAMOLE
 */
object ShadowsPlusDefaults {

    /** Default shadow radius. */
    val ShadowRadius = 8.dp

    /** Default shadow color. */
    val ShadowColor = Color.Black.copy(alpha = 0.23F)

    /** Default shadow shape. */
    val ShadowShape = RectangleShape

    /** Default shadow spread. */
    val ShadowSpread = 0.dp

    /** Default shadow offset. */
    val ShadowOffset = DpOffset(
        x = 0.dp,
        y = 2.dp
    )

    /** Default indicator for alpha content clip. */
    const val IsAlphaContentClip = false
}