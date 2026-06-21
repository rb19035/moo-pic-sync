package com.tcgms.moopicsync

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.tcgms.moopicsync.data.ConfigManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configManager = ConfigManager(this)
        if (configManager.isConfigured()) {
            startActivity(Intent(this, SyncActivity::class.java))
        } else {
            startActivity(Intent(this, ConfigActivity::class.java))
        }
        finish()
    }
}
