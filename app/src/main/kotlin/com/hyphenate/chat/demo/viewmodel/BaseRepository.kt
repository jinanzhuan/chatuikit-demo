package com.hyphenate.chat.demo.viewmodel

import android.content.Context
import com.hyphenate.chat.demo.DemoApplication

open class BaseRepository {
    fun getContext(): Context {
        return DemoApplication.getInstance()
    }
}