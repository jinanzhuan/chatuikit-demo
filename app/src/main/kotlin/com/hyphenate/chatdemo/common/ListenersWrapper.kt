package com.hyphenate.chatdemo.common

import com.hyphenate.chatdemo.DemoApplication
import com.hyphenate.chatdemo.login.LoginActivity
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.common.ChatConnectionListener
import com.hyphenate.easeui.common.ChatLog

object ListenersWrapper {

    private val connectListener by lazy {
        object : ChatConnectionListener {
            override fun onConnected() {
                // do something
            }

            override fun onDisconnected(errorCode: Int) {

            }

            override fun onLogout(errorCode: Int, info: String?) {
                super.onLogout(errorCode, info)
                ChatLog.e("app","onLogout: $errorCode")
                DemoApplication.getInstance().getLifecycleCallbacks().activityList.forEach {
                    it.finish()
                }
                LoginActivity.startAction(DemoApplication.getInstance())
            }
        }
    }

    fun registerListeners() {
        // register connection listener
        EaseIM.addConnectionListener(connectListener)
    }
}