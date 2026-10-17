package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig

class ApiKeyManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cars360_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CUSTOM_API_KEY = "custom_gemini_api_key"
        // Known revoked/public keys that must not be used as live credentials
        private const val REVOKED_KEY = "AIzaSyC06wsO_dxSvSi17lY1uZ7swX4q2_ZYZVE"
        private const val PLACEHOLDER_KEY = "YOUR_GEMINI_API_KEY"
        private const val DEFAULT_KEY = "MY_GEMINI_API_KEY"
    }

    fun getCustomApiKey(): String {
        val raw = prefs.getString(KEY_CUSTOM_API_KEY, "") ?: ""
        return cleanKey(raw)
    }

    fun setCustomApiKey(key: String) {
        val sanitized = cleanKey(key)
        prefs.edit().putString(KEY_CUSTOM_API_KEY, sanitized).apply()
    }

    fun clearCustomApiKey() {
        prefs.edit().remove(KEY_CUSTOM_API_KEY).apply()
    }

    private fun cleanKey(key: String?): String {
        if (key.isNullOrBlank()) return ""
        return key.trim()
            .removeSurrounding("\"")
            .removeSurrounding("'")
            .filter { !it.isWhitespace() }
    }

    fun getEffectiveApiKey(): String {
        // 1. User-entered key in the in-app Gemini Key dialog
        val customKey = getCustomApiKey()
        if (customKey.isNotBlank() && customKey != REVOKED_KEY && customKey != PLACEHOLDER_KEY && customKey != DEFAULT_KEY) {
            return customKey
        }

        // 2. BuildConfig key injected from AI Studio Secrets panel (.env)
        val buildKey = cleanKey(BuildConfig.GEMINI_API_KEY)
        if (buildKey.isNotBlank() && buildKey != DEFAULT_KEY && buildKey != PLACEHOLDER_KEY && buildKey != REVOKED_KEY) {
            return buildKey
        }

        // 3. Process environment variable if running in local container or test runner
        val envKey = cleanKey(try { System.getenv("GEMINI_API_KEY") } catch (_: Exception) { null })
        if (envKey.isNotBlank() && envKey != DEFAULT_KEY && envKey != PLACEHOLDER_KEY && envKey != REVOKED_KEY) {
            return envKey
        }

        return ""
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
