package com.tcgms.moopicsync

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.tcgms.moopicsync.model.MediaItem
import com.tcgms.moopicsync.ui.SyncScreen
import com.tcgms.moopicsync.ui.theme.MoopicsyncTheme

class SyncActivity : ComponentActivity() {

    private var mediaItemsState = mutableStateListOf<MediaItem>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            loadMedia()
        } else {
            Toast.makeText(this, "Permissions required to show media", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        checkAndRequestPermissions()

        setContent {
            MoopicsyncTheme {
                SyncScreen(
                    mediaItems = mediaItemsState,
                    onSyncClick = { performSync() },
                    onSafeRemoveClick = { performSafeRemove() },
                    onConfigClick = { 
                        startActivity(Intent(this, ConfigActivity::class.java))
                    }
                )
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            loadMedia()
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    private fun loadMedia() {
        mediaItemsState.clear()
        mediaItemsState.addAll(fetchMedia())
    }

    private fun fetchMedia(): List<MediaItem> {
        val items = mutableListOf<MediaItem>()
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.MIME_TYPE
        )

        // Query Images
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                items.add(MediaItem(id, uri, cursor.getString(nameCol), cursor.getLong(sizeCol), cursor.getString(mimeCol), false))
            }
        }

        // Query Videos
        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                items.add(MediaItem(id, uri, cursor.getString(nameCol), cursor.getLong(sizeCol), cursor.getString(mimeCol), true))
            }
        }
        return items
    }

    private fun performSync() {
        Toast.makeText(this, "Syncing ${mediaItemsState.size} items...", Toast.LENGTH_SHORT).show()
        // REST API call to be implemented
    }

    private fun performSafeRemove() {
        Toast.makeText(this, "Checking server for safe removal...", Toast.LENGTH_SHORT).show()
        // REST API call to be implemented
    }
}
