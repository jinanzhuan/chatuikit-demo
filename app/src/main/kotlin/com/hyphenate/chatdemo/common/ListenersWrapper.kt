package com.hyphenate.chatdemo.common

import com.hyphenate.chatdemo.DemoApplication
import com.hyphenate.chatdemo.DemoHelper
import com.hyphenate.chatdemo.login.LoginActivity
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.common.ChatConnectionListener
import com.hyphenate.easeui.common.ChatLog
import com.hyphenate.easeui.common.ChatMessageListener

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

    private val messageListener by lazy { ChatMessageListener { messageList ->
        if (DemoHelper.getInstance().getDataModel().isAppPushSilent()) {
            return@ChatMessageListener
        }
        // do something
        messageList?.forEach { message ->

            if (EaseIM.checkMutedConversationList(message.conversationId())) {
                return@forEach
            }
            if (DemoApplication.getInstance().getLifecycleCallbacks().isFront.not()) {
                DemoHelper.getInstance().getNotifier()?.notify(message)
            }

            // notify new message
            DemoHelper.getInstance().getNotifier()?.vibrateAndPlayTone(message)
        }

    } }

    fun registerListeners() {
        // register connection listener
        EaseIM.addConnectionListener(connectListener)
        EaseIM.addChatMessageListener(messageListener)
    }
}