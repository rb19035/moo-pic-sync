package com.tcgms.moopicsync

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tcgms.moopicsync.data.ConfigManager
import com.tcgms.moopicsync.ui.ConfigScreen
import com.tcgms.moopicsync.ui.theme.MoopicsyncTheme

class ConfigActivity : ComponentActivity() {
    private lateinit var configManager: ConfigManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        configManager = ConfigManager(this)

        setContent {
            MoopicsyncTheme {
                ConfigScreen(
                    initialData = configManager.getConfig(),
                    onSave = { url, port, email, apiKey ->
                        val wasConfigured = configManager.isConfigured()
                        configManager.saveConfig(url, port, email, apiKey)
                        Toast.makeText(this, "Configuration Saved", Toast.LENGTH_SHORT).show()
                        
                        if (!wasConfigured) {
                            startActivity(Intent(this, SyncActivity::class.java))
                        }
                        finish()
                    },
                    onCancel = {
                        finish()
                    }
                )
            }
        }
    }
}
