package com.hyphenate.chatdemo.common

import android.content.Context
import java.util.Locale

object LanguageUtil {
    fun changeLanguage(context: Context,languageCode:String){
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        context.resources.updateConfiguration(configuration,context.resources.displayMetrics)
    }
}