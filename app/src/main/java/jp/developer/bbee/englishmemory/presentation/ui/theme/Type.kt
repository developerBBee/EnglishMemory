package jp.developer.bbee.englishmemory.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val phoneTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val tabletTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 64.sp, //32.sp,
        lineHeight = 80.sp, //40.sp,
    ),
    headlineMedium = TextStyle(
        fontSize = 56.sp, //28.sp,
        lineHeight = 72.sp, //36.sp,
    ),
    headlineSmall = TextStyle(
        fontSize = 48.sp, //24.sp,
        lineHeight = 64.sp, //32.sp,
    ),
    titleLarge = TextStyle(
        fontSize = 44.sp, //22.sp,
        lineHeight = 56.sp, //28.sp,
    ),
    titleMedium = TextStyle(
        fontSize = 32.sp, //16.sp,
        lineHeight = 48.sp, //24.sp,
    ),
    titleSmall = TextStyle(
        fontSize = 28.sp, //14.sp,
        lineHeight = 40.sp, //20.sp,
    ),
    bodyLarge = TextStyle(
        fontSize = 32.sp, //16.sp,
        lineHeight = 48.sp, //24.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 28.sp, //14.sp,
        lineHeight = 40.sp, //20.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 24.sp, //12.sp,
        lineHeight = 32.sp, //16.sp,
    ),
    labelLarge = TextStyle(
        fontSize = 28.sp, //14.sp,
        lineHeight = 40.sp, //20.sp,
    ),
    labelMedium = TextStyle(
        fontSize = 24.sp, //12.sp,
        lineHeight = 32.sp, //16.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 22.sp, //11.sp,
        lineHeight = 32.sp, //16.sp,
    ),
)