@file:OptIn(ExperimentalMaterial3Api::class)

package com.gigamole.composeshadowsplus.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.gigamole.composeshadowsplus.common.ShadowsPlusDefaults
import com.gigamole.composeshadowsplus.elevation.AlphaContentElevationShadow
import com.gigamole.composeshadowsplus.rsblur.RSBlurShadowDefaults
import com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
import com.gigamole.composeshadowsplus.softlayer.SoftLayerShadowContainer
import com.gigamole.composeshadowsplus.softlayer.softLayerShadow
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    MainTheme {
        MainScreenContent()
//        MainScreenDemoContent()
    }
}

@Suppress("unused")
@Composable
fun MainScreenDemoContent() {
    val shape = remember { RectangleShape }
    val infiniteTransition = rememberInfiniteTransition(label = "DemoTransition")

    val angle by infiniteTransition.animateFloat(
        initialValue = 45.0F,
        targetValue = -315.0F,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 3000)
        ),
        label = "Angle"
    )
    val radians by remember(angle) {
        derivedStateOf {
            Math.toRadians(angle.toDouble()).toFloat()
        }
    }
    val shadowColor by animateColorAsState(
        targetValue = when {
            angle <= 0.0F && angle >= -120.0F -> Color.Cyan.copy(alpha = 0.5F)
            angle <= -120.0F && angle >= -230.0F -> Color.Magenta.copy(alpha = 0.3F)
            else -> Color.Black
        },
        animationSpec = tween(durationMillis = 500),
        label = "ShadowColor"
    )
    val shadowRadius by animateDpAsState(
        targetValue = when {
            angle <= 10.0F && angle >= -190.0F -> 64.dp
            else -> 0.dp
        },
        animationSpec = tween(durationMillis = 1000),
        label = "ShadowRadius"
    )
    val shadowRadiusOffset by animateDpAsState(
        targetValue = when {
            angle <= 20.0F && angle >= -230.0F -> 36.dp
            else -> 16.dp
        },
        animationSpec = tween(durationMillis = 1000),
        label = "ShadowRadiusOffset"
    )
    val shadowSpread by animateDpAsState(
        targetValue = when {
            angle <= -20.0F && angle >= -180.0F -> (-6).dp
            else -> 0.dp
        },
        animationSpec = tween(durationMillis = 1000),
        label = "ShadowSpread"
    )
    val shadowOffset by remember(radians) {
        derivedStateOf {
            DpOffset(
                x = shadowRadiusOffset * sin(radians),
                y = shadowRadiusOffset * cos(radians),
            )
        }
    }

    SoftLayerShadowContainer {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .softLayerShadow(
                        radius = shadowRadius,
                        color = shadowColor,
                        shape = shape,
                        offset = shadowOffset,
                        spread = shadowSpread
                    )
                    .background(
                        color = Color.White,
                        shape = shape
                    )
                    .border(
                        width = 2.dp,
                        color = Color.Black
                    )
                    .padding(
                        horizontal = 32.dp,
                        vertical = 24.dp
                    )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(space = 6.dp)) {
                    Text(
                        text = "ComposeShadowsPlus",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamilySpaceGrotesk
                    )
                    Text(
                        text = "Elevate Android Compose UI with custom shadows",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamilyOpenSans
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreenContent() {
    var shadowType by remember { mutableStateOf(ShadowType.RSBlur) }
    var shadowShapeType by remember { mutableStateOf(ShadowShapeType.Rect) }

    var shadowRadius by remember { mutableStateOf(ShadowsPlusDefaults.ShadowRadius) }
    var shadowColor by remember { mutableStateOf(ShadowsPlusDefaults.ShadowColor) }
    var shadowSpread by remember { mutableStateOf(ShadowsPlusDefaults.ShadowSpread) }
    var shadowOffsetX by remember { mutableStateOf(ShadowsPlusDefaults.ShadowOffset.x) }
    var shadowOffsetY by remember { mutableStateOf(ShadowsPlusDefaults.ShadowOffset.y) }
    var rsBlurShadowAlignRadius by remember { mutableStateOf(RSBlurShadowDefaults.RSBlurShadowIsAlignRadius) }
    var isAlphaContentClip by remember { mutableStateOf(false) }
    var alphaContentColor by remember { mutableStateOf(Color.White) }

    var isShadowColorPickerVisible by remember { mutableStateOf(false) }
    var isAlphaContentColorPickerVisible by remember { mutableStateOf(false) }

    val shadowShape by remember(shadowShapeType) {
        derivedStateOf {
            when (shadowShapeType) {
                ShadowShapeType.Rect -> RectangleShape
                ShadowShapeType.Circle -> CircleShape
                ShadowShapeType.Path -> CutCornerShape(size = 42.dp)
            }
        }
    }
    val shadowOffset by remember(shadowOffsetX, shadowOffsetY) {
        derivedStateOf {
            DpOffset(
                x = shadowOffsetX,
                y = shadowOffsetY
            )
        }
    }
    val elevationColor by remember(shadowColor) {
        derivedStateOf {
            shadowColor.copy(alpha = 1.0F)
        }
    }
    val contentColor by remember(
        isAlphaContentClip,
        alphaContentColor
    ) {
        derivedStateOf {
            if (isAlphaContentClip) {
                alphaContentColor
            } else {
                Color.White
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1.0F)
                .clipToBounds(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = isAlphaContentClip,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alpha = 0.35F),
                    painter = painterResource(id = R.drawable.img_alpha),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = shadowType,
                label = "ShadowTypeCrossfade"
            ) {
                when (it) {
                    ShadowType.RSBlur -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(all = 64.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .rsBlurShadow(
                                        radius = shadowRadius,
                                        color = shadowColor,
                                        shape = shadowShape,
                                        spread = shadowSpread,
                                        offset = shadowOffset,
                                        isAlignRadius = rsBlurShadowAlignRadius,
                                        isAlphaContentClip = isAlphaContentClip
                                    )
                                    .background(
                                        color = contentColor,
                                        shape = shadowShape,
                                    )
                            )
                        }
                    }
                    ShadowType.SoftLayer -> {
                        SoftLayerShadowContainer {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(all = 64.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .softLayerShadow(
                                            radius = shadowRadius,
                                            color = shadowColor,
                                            shape = shadowShape,
                                            spread = shadowSpread,
                                            offset = shadowOffset,
                                            isAlphaContentClip = isAlphaContentClip
                                        )
                                        .background(
                                            color = contentColor,
                                            shape = shadowShape
                                        )
                                )
                            }
                        }
                    }
                    ShadowType.Elevation -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Crossfade(
                                modifier = Modifier.fillMaxSize(),
                                targetState = isAlphaContentClip,
                                label = "ElevationShadowIsAlphaContentClip"
                            ) { isAlphaContentClipState ->
                                if (isAlphaContentClipState) {
                                    AlphaContentElevationShadow(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(all = 64.dp),
                                        elevation = shadowRadius,
                                        shape = shadowShape,
                                        ambientColor = shadowColor,
                                        spotColor = shadowColor
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    color = contentColor,
                                                    shape = shadowShape
                                                )
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(all = 64.dp)
                                            .shadow(
                                                elevation = shadowRadius,
                                                shape = shadowShape,
                                                ambientColor = elevationColor,
                                                spotColor = elevationColor
                                            )
                                            .background(
                                                color = contentColor,
                                                shape = shadowShape
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Divider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
                .padding(all = 20.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            Text(
                text = "TYPE:",
                style = MaterialTheme.typography.labelLarge
            )
            TabRow(
                selectedTabIndex = shadowType.ordinal,
                modifier = Modifier.fillMaxWidth(),
                tabs = {
                    ShadowType.values().forEach { enumValue ->
                        Tab(
                            selected = enumValue == shadowType,
                            text = {
                                Text(text = enumValue.name.uppercase())
                            },
                            onClick = {
                                shadowType = enumValue
                            },
                        )
                    }
                }
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "SHAPE:",
                style = MaterialTheme.typography.labelLarge
            )
            TabRow(
                selectedTabIndex = shadowShapeType.ordinal,
                modifier = Modifier.fillMaxWidth(),
                tabs = {
                    ShadowShapeType.values().forEach { enumValue ->
                        Tab(
                            selected = enumValue == shadowShapeType,
                            text = {
                                Text(text = enumValue.name.uppercase())
                            },
                            onClick = {
                                shadowShapeType = enumValue
                            },
                        )
                    }
                }
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "COLOR:",
                style = MaterialTheme.typography.labelLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(intrinsicSize = IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(space = 20.dp)
            ) {
                Button(
                    modifier = Modifier.weight(weight = 1.0F),
                    onClick = {
                        isShadowColorPickerVisible = true
                        isAlphaContentColorPickerVisible = false
                    }
                ) {
                    Text(text = "PICK COLOR")
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(ratio = 1.0F)
                        .background(
                            color = shadowColor,
                            shape = RoundedCornerShape(size = 4.dp)
                        )
                        .border(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(size = 4.dp),
                            width = 1.dp
                        )
                        .clickable {
                            isShadowColorPickerVisible = true
                            isAlphaContentColorPickerVisible = false
                        }
                )
            }

            if (shadowType == ShadowType.RSBlur) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(weight = 1.0F),
                        text = "ALIGN RSBLUR RADIUS:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Switch(
                        checked = rsBlurShadowAlignRadius,
                        onCheckedChange = {
                            rsBlurShadowAlignRadius = it
                        }
                    )
                }
            }

            var rsBlurShadowRadius by remember { mutableStateOf(shadowRadius) }

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "RADIUS:",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = if (shadowType == ShadowType.RSBlur) {
                    rsBlurShadowRadius.value
                } else {
                    shadowRadius.value
                },
                onValueChange = {
                    if (shadowType == ShadowType.RSBlur) {
                        rsBlurShadowRadius = it.dp
                    } else {
                        shadowRadius = it.dp
                    }
                },
                onValueChangeFinished = {
                    if (shadowType == ShadowType.RSBlur) {
                        shadowRadius = rsBlurShadowRadius
                    }
                },
                valueRange = 0.0F..32.0F,
                steps = 32
            )

            if (shadowType != ShadowType.Elevation) {
                var rsBlurShadowSpread by remember { mutableStateOf(shadowSpread) }

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "SPREAD:",
                    style = MaterialTheme.typography.labelLarge
                )
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = if (shadowType == ShadowType.RSBlur) {
                        rsBlurShadowSpread.value
                    } else {
                        shadowSpread.value
                    },
                    onValueChange = {
                        if (shadowType == ShadowType.RSBlur) {
                            rsBlurShadowSpread = it.dp
                        } else {
                            shadowSpread = it.dp
                        }
                    },
                    onValueChangeFinished = {
                        if (shadowType == ShadowType.RSBlur) {
                            shadowSpread = rsBlurShadowSpread
                        }
                    },
                    valueRange = -32.0F..32.0F,
                    steps = 64
                )

                var rsBlurShadowOffsetX by remember { mutableStateOf(shadowOffsetX) }

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "OFFSET X:",
                    style = MaterialTheme.typography.labelLarge
                )
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = if (shadowType == ShadowType.RSBlur) {
                        rsBlurShadowOffsetX.value
                    } else {
                        shadowOffsetX.value
                    },
                    onValueChange = {
                        if (shadowType == ShadowType.RSBlur) {
                            rsBlurShadowOffsetX = it.dp
                        } else {
                            shadowOffsetX = it.dp
                        }
                    },
                    onValueChangeFinished = {
                        if (shadowType == ShadowType.RSBlur) {
                            shadowOffsetX = rsBlurShadowOffsetX
                        }
                    },
                    valueRange = -32.0F..32.0F,
                    steps = 64
                )

                var rsBlurShadowOffsetY by remember { mutableStateOf(shadowOffsetY) }

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "OFFSET Y:",
                    style = MaterialTheme.typography.labelLarge
                )
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = if (shadowType == ShadowType.RSBlur) {
                        rsBlurShadowOffsetY.value
                    } else {
                        shadowOffsetY.value
                    },
                    onValueChange = {
                        if (shadowType == ShadowType.RSBlur) {
                            rsBlurShadowOffsetY = it.dp
                        } else {
                            shadowOffsetY = it.dp
                        }
                    },
                    onValueChangeFinished = {
                        if (shadowType == ShadowType.RSBlur) {
                            shadowOffsetY = rsBlurShadowOffsetY
                        }
                    },
                    valueRange = -32.0F..32.0F,
                    steps = 64
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(weight = 1.0F),
                        text = "IS ALPHA CONTENT CLIP:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Switch(
                        checked = isAlphaContentClip,
                        onCheckedChange = {
                            isAlphaContentClip = it
                        }
                    )
                }
            }

            if (isAlphaContentClip) {
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "ALPHA CONTENT COLOR:",
                    style = MaterialTheme.typography.labelLarge
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(space = 20.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(weight = 1.0F),
                        onClick = {
                            isAlphaContentColorPickerVisible = true
                            isShadowColorPickerVisible = false
                        }
                    ) {
                        Text(text = "PICK COLOR")
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(ratio = 1.0F)
                            .background(
                                color = alphaContentColor,
                                shape = RoundedCornerShape(size = 4.dp)
                            )
                            .border(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(size = 4.dp),
                                width = 1.dp
                            )
                            .clickable {
                                isAlphaContentColorPickerVisible = true
                                isShadowColorPickerVisible = false
                            }
                    )
                }
            }
        }
    }

    if (isShadowColorPickerVisible || isAlphaContentColorPickerVisible) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismissRequest = {
                isShadowColorPickerVisible = false
                isAlphaContentColorPickerVisible = false
            }
        ) {
            ClassicColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 300.dp)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                color = HsvColor.from(
                    if (isShadowColorPickerVisible) {
                        shadowColor
                    } else {
                        alphaContentColor
                    }
                )
            ) {
                if (isShadowColorPickerVisible) {
                    shadowColor = it.toColor()
                } else {
                    alphaContentColor = it.toColor()
                }
            }
        }
    }
}

enum class ShadowType {
    RSBlur,
    SoftLayer,
    Elevation
}

enum class ShadowShapeType {
    Rect,
    Circle,
    Path
}
