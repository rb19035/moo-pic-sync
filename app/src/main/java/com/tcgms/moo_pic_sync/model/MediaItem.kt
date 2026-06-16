package com.tcgms.moo_pic_sync.model

import android.net.Uri

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
    val isVideo: Boolean
)
