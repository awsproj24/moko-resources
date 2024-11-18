/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.preference.PreferenceManager
import java.util.Locale

object LocaleHandler {

    const val LOCALE_PREF_TAG = "pref:locale"

    fun updateLocale(newBase: Context): Context {
        val currentLocale = PreferenceManager.getDefaultSharedPreferences(newBase)
            .getString(LOCALE_PREF_TAG, Locale.getDefault().language)

        val newLocale = Locale(currentLocale)
        Locale.setDefault(newLocale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val configuration: Configuration = newBase.resources.configuration
            val newLocales = LocaleList(newLocale)
            configuration.setLocales(newLocales)
            newBase.createConfigurationContext(configuration)
        } else {
            newBase.resources.configuration.locale = newLocale
            newBase.resources.updateConfiguration(newBase.resources.configuration, newBase.resources.displayMetrics)
            newBase
        }
    }
}
