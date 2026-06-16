package com.tcgms.moo_pic_sync

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tcgms.moo_pic_sync.data.ConfigManager
import com.tcgms.moo_pic_sync.ui.LoginScreen
import com.tcgms.moo_pic_sync.ui.theme.MoopicsyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val configManager = ConfigManager(this)
        if (!configManager.isConfigured()) {
            startActivity(Intent(this, ConfigActivity::class.java))
            // We don't finish() here yet because the user might cancel and 
            // we'd want them to come back to login or config. 
            // Actually, if it's the first run, they MUST config.
        }

        setContent {
            MoopicsyncTheme {
                // In a real app, we'd check a preference or session here
                LoginScreen {
                    startActivity(Intent(this, SyncActivity::class.java))
                    finish()
                }
            }
        }
    }
}
