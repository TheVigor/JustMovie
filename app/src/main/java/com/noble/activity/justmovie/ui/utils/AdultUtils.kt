package com.noble.activity.justmovie.ui.utils

import android.content.Context
import android.preference.PreferenceManager
import com.noble.activity.justmovie.data.local.SharedPrefs.KEY_ADULT

object AdultUtil {

    fun includeAdult(context: Context): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(KEY_ADULT, true)
    }
}