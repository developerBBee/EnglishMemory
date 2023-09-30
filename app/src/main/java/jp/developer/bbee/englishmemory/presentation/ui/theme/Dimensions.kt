package jp.developer.bbee.englishmemory.presentation.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val small: Dp,
    val smallMedium: Dp,
    val medium: Dp,
    val mediumLarge: Dp,
    val large: Dp,
)

val phoneDimensions = Dimensions(
    small = 8.dp,
    smallMedium = 12.dp,
    medium = 16.dp,
    mediumLarge = 24.dp,
    large = 32.dp,
)

val tabletDimensions = Dimensions(
    small = 10.dp,
    smallMedium = 16.dp,
    medium = 22.dp,
    mediumLarge = 32.dp,
    large = 42.dp,
)

