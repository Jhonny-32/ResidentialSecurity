package com.edifice.residentialsecurity.di.sharedPreferencesDi

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsRepositoryImpl @Inject constructor(private val prefs: SharedPreferences) {
    private val gson = Gson()

    fun save(key: String, objeto: Any) {
        try {
            val json = gson.toJson(objeto)
            prefs.edit().putString(key, json).apply()
        } catch (e: Exception) {
            Log.d("ERROR", "Err: ${e.message}")
        }
    }

    fun getData(key: String): String? {
        return prefs.getString(key, "")
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}