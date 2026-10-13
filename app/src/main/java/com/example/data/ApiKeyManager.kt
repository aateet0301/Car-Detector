package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig

class ApiKeyManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("autolens_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CUSTOM_API_KEY = "custom_gemini_api_key"
        const val DEFAULT_FALLBACK_KEY = "AIzaSyC06wsO_dxSvSi17lY1uZ7swX4q2_ZYZVE"
    }

    fun getCustomApiKey(): String {
        return prefs.getString(KEY_CUSTOM_API_KEY, "") ?: ""
    }

    fun setCustomApiKey(key: String) {
        prefs.edit().putString(KEY_CUSTOM_API_KEY, key.trim()).apply()
    }

    fun clearCustomApiKey() {
        prefs.edit().remove(KEY_CUSTOM_API_KEY).apply()
    }

    fun getEffectiveApiKey(): String {
        val customKey = getCustomApiKey()
        if (customKey.isNotBlank()) {
            return customKey
        }
        val buildKey = BuildConfig.GEMINI_API_KEY
        if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") {
            return buildKey
        }
        return DEFAULT_FALLBACK_KEY
    }

    fun hasValidApiKey(): Boolean {
        return getEffectiveApiKey().isNotBlank()
    }

    fun getMaskedKey(): String {
        val key = getEffectiveApiKey()
        if (key.isBlank()) return "Not Configured"
        if (key.length <= 8) return "••••••••"
        return "${key.take(6)}••••${key.takeLast(4)}"
    }
}
