package jp.developer.bbee.englishmemory.presentation.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import jp.developer.bbee.englishmemory.domain.model.StudyData
import jp.developer.bbee.englishmemory.presentation.components.icon.BookmarkOrNotIcon
import jp.developer.bbee.englishmemory.presentation.ui.theme.AppTheme

@Composable
fun StudyDataDialog(
    studyData: StudyData,
    onDismiss: () -> Unit,
    onClickBookmark: (Boolean) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "閉じる")
            }
        },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BookmarkOrNotIcon(isBookmarked = studyData.isFavorite) {
                        onClickBookmark(it)
                    }
                    Text(
                        text = "${studyData.english}  [${studyData.wordType}]",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(AppTheme.dimens.medium)
                    )
                }
                Text(
                    text = studyData.translateToJapanese,
                    modifier = Modifier.padding(AppTheme.dimens.medium)
                )
            }
        }
    )
}