package com.hyphenate.chatdemo

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.hyphenate.chatdemo.base.UserActivityLifecycleCallbacks
import com.hyphenate.chatdemo.common.DemoConstant
import com.hyphenate.chatdemo.common.LanguageUtil
import com.hyphenate.easeui.common.helper.EasePreferenceManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class DemoApplication: Application() {
    private val mLifecycleCallbacks = UserActivityLifecycleCallbacks()
    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks()

        DemoHelper.getInstance().init(this)
        initSDK()

        // Call this method after EaseIM#init
        val isBlack = EasePreferenceManager.getInstance().getBoolean(DemoConstant.IS_BLACK_THEME)
        AppCompatDelegate.setDefaultNightMode(if (isBlack) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initSDK() {
        if (DemoHelper.getInstance().getDataModel().isAgreeAgreement()) {
            DemoHelper.getInstance().initSDK()
        }
    }

    private fun registerActivityLifecycleCallbacks() {
        this.registerActivityLifecycleCallbacks(mLifecycleCallbacks)
    }

    fun getLifecycleCallbacks(): UserActivityLifecycleCallbacks {
        return mLifecycleCallbacks
    }

    /**
     * Set default settings for SmartRefreshLayout
     */

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context)
        }
    }

    companion object {
        private lateinit var instance: DemoApplication
        fun getInstance(): DemoApplication {
            return instance
        }
    }

}