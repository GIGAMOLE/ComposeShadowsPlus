package com.gigamole.composeshadowsplus.sample

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

val FontFamilySpaceGrotesk = FontFamily(
    Font(
        resId = R.font.space_grotesk_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    )
)
val FontFamilyOpenSans = FontFamily(
    Font(
        resId = R.font.opensans_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
)

@Composable
fun MainTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color.Black,
            primaryContainer = Color.White,
            onPrimary = Color.White,
            secondary = Color.Black,
            secondaryContainer = Color.White,
            onSecondary = Color.White,
            tertiary = Color.Black,
            tertiaryContainer = Color.White,
            onTertiary = Color.White,
            surface = Color.White,
            onSurface = Color.Black,
            surfaceVariant = Color.LightGray,
            onSurfaceVariant = Color.DarkGray,
            outline = Color.DarkGray,
            background = Color.White,
            onBackground = Color.Black
        ),
        content = content
    )
}