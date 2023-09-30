package jp.developer.bbee.englishmemory.presentation.screen.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.developer.bbee.englishmemory.domain.model.TranslateData

@Composable
fun TopScreen(
    viewModel: TopViewModel = hiltViewModel(),
) {
    val DEBUG = false

    val state = viewModel.state.value
    val dataList = viewModel.state.value.translateData

    Box(modifier = Modifier.fillMaxSize()) {
        if (DEBUG) {
            DebugView(state = state, dataList = dataList, viewModel = viewModel)
        } else {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                        .background(Color.Yellow),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        // 仮置き
                        imageVector = Icons.Default.Face,
                        modifier = Modifier.size(1280.dp),
                        contentDescription = "Provisional image",
                        contentScale = ContentScale.Fit
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Start",
                            fontSize = 48.sp,
                        )
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Setting",
                            fontSize = 48.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DebugView(
    state: TopState,
    dataList: List<TranslateData>?,
    viewModel: TopViewModel,
) {
    when {
        (dataList != null) -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(onClick = { viewModel.save() }) {
                        Text(text = "Save")
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Button(onClick = { viewModel.load() }) {
                        Text(text = "Load")
                    }
                }
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(dataList) { data ->
                        Text(text = data.english + " : " + data.translateToJapanese)
                    }
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
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}