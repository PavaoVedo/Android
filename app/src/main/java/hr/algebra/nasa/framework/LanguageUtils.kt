package hr.algebra.nasa.framework

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.Locale

private const val PREFS_NAME = "app_prefs"
private const val KEY_LANGUAGE = "key_language"

fun Context.getCurrentLanguage(): String {
    val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
}

fun Context.setCurrentLanguage(lang: String) {
    val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit {
        putString(KEY_LANGUAGE, lang)
    }
}
