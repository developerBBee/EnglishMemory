package jp.developer.bbee.englishmemory.presentation.screen.top

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TopScreen(
    viewModel: TopViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val dto = viewModel.state.value.translateData
    val dataList = dto?.toTransLateDataList() ?: emptyList()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            (state.translateData != null) -> {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(dataList) { data ->
                        Text(text = data.english + " : " + data.translateToJapanese)
                    }
                }
            }
            !state.error.isNullOrBlank() -> {
                TextField(
                    value = state.error,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodyLarge,
                )
            }
            else -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}