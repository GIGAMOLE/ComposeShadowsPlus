@file:Suppress("DEPRECATION")

package com.gigamole.composeshadowsplus.rsblur

import android.graphics.Bitmap
import android.graphics.Canvas
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.withSave
import androidx.core.graphics.withTranslation
import com.gigamole.composeshadowsplus.common.ShadowsPlusDefaults
import com.gigamole.composeshadowsplus.common.spreadScale
import kotlin.math.abs
import kotlin.math.pow

/**
 * RSBlur radius chunk size and max radius of [ScriptIntrinsicBlur].
 *
 * @see chunkateRadius
 * @see ScriptIntrinsicBlur.setRadius
 * @author GIGAMOLE
 */
private const val RSBlurRadiusChunkSize = 25.0F

/**
 * RSBlur radius align exponential multiplier.
 *
 * @author GIGAMOLE
 */
private const val RSBlurRadiusAlignMultiplier = 1.33F

/**
 * A [RenderScript]/[ScriptIntrinsicBlur] based shadow [Modifier].
 *
 * Algorithm:
 * 1. Creates a [Bitmap] with a size based on the Composable element [Size], [radius], [spread], and [offset].
 * 2. Applies calculated offset and spread scale on the [Canvas], and draws [Outline] by [shape].
 * 3. Chunkates [radius] if needed and applies [ScriptIntrinsicBlur] to the result [Bitmap].
 * 4. Draws blurred [Bitmap] with calculated offset behind the Composable element.
 *
 * @param radius The shadow radius.
 * @param color The shadow color.
 * @param shape The shadow shape.
 * @param spread The shadow spread.
 * @param offset The shadow offset.
 * @param alignRadius The exponential align radius indicator.
 * @return The applied RSBlurShadow [Modifier].
 * @see chunkateRadius
 * @see applyBlur
 * @author GIGAMOLE
 */
fun Modifier.rsBlurShadow(
    radius: Dp = ShadowsPlusDefaults.ShadowRadius,
    color: Color = ShadowsPlusDefaults.ShadowColor,
    shape: Shape = ShadowsPlusDefaults.ShadowShape,
    spread: Dp = ShadowsPlusDefaults.ShadowSpread,
    offset: DpOffset = ShadowsPlusDefaults.ShadowOffset,
    alignRadius: Boolean = RSBlurShadowDefaults.RSBlurShadowAlignRadius
): Modifier = composed {
    val context = LocalContext.current
    val density = LocalDensity.current

    val radiusPx: Float
    val offsetPx: Offset
    val spreadPx: Float

    with(density) {
        radiusPx = radius.toPx().let {
            if (alignRadius) it.pow(x = RSBlurRadiusAlignMultiplier) else it
        }
        offsetPx = Offset(
            x = offset.x.toPx(),
            y = offset.y.toPx(),
        )
        spreadPx = spread.toPx()
    }

    var size by remember { mutableStateOf(Size.Zero) }

    // Calculates input Bitmap size.
    val halfBlurSize = radiusPx + spreadPx
    val blurSize = halfBlurSize * 2.0F
    val blurWidth = blurSize + abs(offsetPx.x)
    val blurHeight = blurSize + abs(offsetPx.y)
    val bitmapWidth = size.width + blurWidth
    val bitmapHeight = size.height + blurHeight

    val bitmap by remember(
        bitmapWidth,
        bitmapHeight,
        color,
        shape
    ) {
        derivedStateOf {
            val inputBitmap = if (bitmapWidth > 0 && bitmapHeight > 0) {
                try {
                    Bitmap.createBitmap(
                        bitmapWidth.toInt(),
                        bitmapHeight.toInt(),
                        Bitmap.Config.ARGB_8888
                    )
                } catch (e: Exception) {
                    return@derivedStateOf null
                }
            } else {
                return@derivedStateOf null
            }

            Canvas(inputBitmap).also { canvas ->
                val paint = Paint().asFrameworkPaint().apply {
                    this.color = color.toArgb()

                    isDither = true
                    isAntiAlias = true
                }

                // Applies offset translation.
                canvas.withTranslation(
                    x = halfBlurSize,
                    y = halfBlurSize
                ) {
                    canvas.withSave {
                        // Applies a spread scale if needed.
                        if (spreadPx != 0.0F) {
                            canvas.scale(
                                spreadScale(
                                    spread = spreadPx,
                                    size = size.width
                                ),
                                spreadScale(
                                    spread = spreadPx,
                                    size = size.height
                                ),
                                size.center.x,
                                size.center.y
                            )
                        }

                        // Draws outline by shape.
                        shape.createOutline(
                            size = size,
                            layoutDirection = LayoutDirection.Rtl,
                            density = density
                        ).let { outline ->
                            when (outline) {
                                is Outline.Rectangle -> {
                                    canvas.drawRect(
                                        outline.rect.toAndroidRectF(),
                                        paint
                                    )
                                }
                                is Outline.Rounded -> {
                                    drawPath(
                                        Path().apply { addRoundRect(outline.roundRect) }.asAndroidPath(),
                                        paint
                                    )
                                }
                                is Outline.Generic -> {
                                    canvas.drawPath(
                                        outline.path.asAndroidPath(),
                                        paint
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Prepares RenderScript and ScriptIntrinsicBlur.
            val renderScript = RenderScript.create(context)
            val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(
                renderScript,
                Element.U8_4(renderScript)
            )
            // Chunkates radius if needed.
            val radiusChunks = chunkateRadius(radius = radiusPx)
            // Iterates radius chunks if needed and apply blur.
            val bitmap = radiusChunks.fold(inputBitmap) { blurBitmap, radius ->
                applyBlur(
                    renderScript = renderScript,
                    scriptIntrinsicBlur = scriptIntrinsicBlur,
                    bitmap = blurBitmap,
                    radius = radius
                )
            }

            renderScript.destroy()

            bitmap
        }
    }

    Modifier
        .onPlaced {
            // Captures Composable element size.
            size = it.size.toSize()
        }
        .drawBehind {
            // Draws blurred Bitmap on the background.
            drawIntoCanvas { canvas ->
                bitmap?.let {
                    canvas.nativeCanvas.drawBitmap(
                        it,
                        -halfBlurSize + offsetPx.x,
                        -halfBlurSize + offsetPx.y,
                        null
                    )
                }
            }
        }
}

/**
 * Applies blur effect to the input [bitmap] using the specified [radius].
 *
 * @param renderScript The RenderScript.
 * @param scriptIntrinsicBlur The ScriptIntrinsicBlur.
 * @param bitmap The input Bitmap.
 * @param radius The blur radius.
 * @return The blurred Bitmap.
 * @author GIGAMOLE
 */
private fun applyBlur(
    renderScript: RenderScript,
    scriptIntrinsicBlur: ScriptIntrinsicBlur,
    bitmap: Bitmap,
    radius: Float
): Bitmap {
    val outputBitmap = Bitmap.createBitmap(bitmap)
    val allocationIn = Allocation.createFromBitmap(renderScript, bitmap)
    val allocationOut = Allocation.createFromBitmap(renderScript, outputBitmap)

    scriptIntrinsicBlur.setRadius(radius)
    scriptIntrinsicBlur.setInput(allocationIn)
    scriptIntrinsicBlur.forEach(allocationOut)

    allocationOut.copyTo(outputBitmap)

    return outputBitmap
}

/**
 * Divides the given [radius] into smaller chunks and returns a list of those chunks.
 *
 * @param radius The radius value.
 * @return A list of smaller radius chunks.
 * @see RSBlurRadiusChunkSize
 * @author GIGAMOLE
 */
private fun chunkateRadius(radius: Float): List<Float> {
    val radiusChunks = mutableListOf<Float>()
    var remainingRadius = radius

    while (remainingRadius > 0) {
        val chunk = remainingRadius.coerceAtMost(RSBlurRadiusChunkSize)

        radiusChunks.add(chunk)

        remainingRadius -= chunk
    }

    return radiusChunks
}