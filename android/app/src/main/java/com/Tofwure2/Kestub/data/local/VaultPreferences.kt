package com.Tofwure2.Kestub.data.local

import android.content.Context
import android.content.SharedPreferences

class VaultPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("mint_vault_prefs", Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putBool(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun readString(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun writeString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
