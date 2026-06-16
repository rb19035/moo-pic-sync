package com.tcgms.moo_pic_sync.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tcgms.moo_pic_sync.model.MediaItem

@Composable
fun SyncScreen(
    mediaItems: List<MediaItem>,
    onSyncClick: () -> Unit,
    onSafeRemoveClick: () -> Unit,
    onConfigClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onSyncClick) {
                        Text("Sync")
                    }
                    Button(onClick = onSafeRemoveClick) {
                        Text("Safe Remove")
                    }
                    Button(onClick = onConfigClick) {
                        Text("Config")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (mediaItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No images or videos found.")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(mediaItems) { item ->
                        MediaThumbnail(item)
                    }
                }
            }
        }
    }
}

@Composable
fun MediaThumbnail(item: MediaItem) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Box {
            AsyncImage(
                model = item.uri,
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            if (item.isVideo) {
                Surface(
                    color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "Video",
                        color = androidx.compose.ui.graphics.Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
