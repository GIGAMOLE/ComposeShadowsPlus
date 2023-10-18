package com.gigamole.composeshadowsplus.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.gigamole.composeshadowsplus.rsblur.RSBlurShadowDefaults
import com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
import com.gigamole.composeshadowsplus.softlayer.softLayerShadow

/**
 * Calculates shadow spread scale.
 *
 * @param spread The raw spread.
 * @param size The X or Y side.
 * @return The shadow spread scale.
 * @see com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
 * @see com.gigamole.composeshadowsplus.softlayer.softLayerShadow
 * @author GIGAMOLE
 */
internal fun spreadScale(
    spread: Float,
    size: Float
): Float = 1.0F + ((spread / size) * 2.0F)

/**
 * A utility [Modifier] to apply ComposeShadowsPlus by [ShadowsPlusType].
 *
 * @param type The shadow type.
 * @param radius The shadow radius.
 * @param color The shadow color.
 * @param shape The shadow shape.
 * @param spread The shadow spread.
 * @param offset The shadow offset.
 * @param isAlignRadius The exponential align radius indicator.
 * @return The applied ComposeShadowsPlus [Modifier].
 * @see com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
 * @see com.gigamole.composeshadowsplus.softlayer.softLayerShadow
 * @author GIGAMOLE
 */
fun Modifier.shadowsPlus(
    type: ShadowsPlusType = ShadowsPlusType.RSBlur,
    radius: Dp = ShadowsPlusDefaults.ShadowRadius,
    color: Color = ShadowsPlusDefaults.ShadowColor,
    shape: Shape = ShadowsPlusDefaults.ShadowShape,
    spread: Dp = ShadowsPlusDefaults.ShadowSpread,
    offset: DpOffset = ShadowsPlusDefaults.ShadowOffset,
    isAlignRadius: Boolean = RSBlurShadowDefaults.RSBlurShadowIsAlignRadius,
    isAlphaContentClip: Boolean = ShadowsPlusDefaults.IsAlphaContentClip
): Modifier = this.then(
    when (type) {
        ShadowsPlusType.RSBlur -> {
            Modifier.rsBlurShadow(
                radius = radius,
                color = color,
                shape = shape,
                spread = spread,
                offset = offset,
                isAlignRadius = isAlignRadius,
                isAlphaContentClip = isAlphaContentClip
            )
        }
        ShadowsPlusType.SoftLayer -> {
            Modifier.softLayerShadow(
                radius = radius,
                color = color,
                shape = shape,
                spread = spread,
                offset = offset,
                isAlphaContentClip = isAlphaContentClip
            )
        }
    }
)

/**
 * Clips the shadow by its shape [path].
 *
 * @param path The shape path.
 * @param block The draw block after the clip.
 */
fun DrawScope.clipShadowByPath(
    path: Path,
    block: DrawScope.() -> Unit
) {
    clipPath(
        path = path,
        clipOp = ClipOp.Difference,
        block = block
    )
}