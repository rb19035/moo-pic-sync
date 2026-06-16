package com.tcgms.moopicsync

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
                    onSave = { url, port, user, pass ->
                        configManager.saveConfig(url, port, user, pass)
                        Toast.makeText(this, "Configuration Saved", Toast.LENGTH_SHORT).show()
                        // TODO: REST API call to authenticate
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
