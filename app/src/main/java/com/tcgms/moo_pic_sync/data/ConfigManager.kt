package com.tcgms.moo_pic_sync.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class ConfigManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "moo_config_secure",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveConfig(url: String, port: String, user: String, pass: String) {
        sharedPreferences.edit().apply {
            putString("server_url", url)
            putString("server_port", port)
            putString("username", user)
            putString("password", pass)
            apply()
        }
    }

    fun getConfig(): ConfigData {
        return ConfigData(
            url = sharedPreferences.getString("server_url", "") ?: "",
            port = sharedPreferences.getString("server_port", "8080") ?: "8080",
            username = sharedPreferences.getString("username", "") ?: "",
            password = sharedPreferences.getString("password", "") ?: ""
        )
    }

    fun isConfigured(): Boolean {
        val config = getConfig()
        return config.url.isNotBlank() && config.username.isNotBlank() && config.password.isNotBlank()
    }

    data class ConfigData(
        val url: String,
        val port: String,
        val username: String,
        val password: String
    )
}
