package jp.developer.bbee.englishmemory.presentation.components.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jp.developer.bbee.englishmemory.R

@Composable
fun BookmarkOrNotIcon(
    isBookmarked: Boolean,
    iconSize: Int = 36,
    onClickBookmark: (Boolean) -> Unit,
) {
    val icon = if (isBookmarked) {
        rememberVectorPainter(Icons.Filled.Star)
    } else {
        painterResource(id = R.drawable.ic_outline_star_24)
    }

    val iconColor = if (isBookmarked) {
        Color.Yellow
    } else {
        Color.Gray
    }

    IconButton(onClick = { onClickBookmark(!isBookmarked) }) {
        Icon(
            painter = icon,
            contentDescription = stringResource(id = R.string.icon_bookmark),
            tint = iconColor,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}