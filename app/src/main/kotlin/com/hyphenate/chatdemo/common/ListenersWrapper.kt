package com.hyphenate.chatdemo.common

import com.hyphenate.chatdemo.DemoApplication
import com.hyphenate.chatdemo.DemoHelper
import com.hyphenate.chatdemo.ui.login.LoginActivity
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.common.ChatGroup
import com.hyphenate.easeui.common.ChatLog
import com.hyphenate.easeui.common.ChatMessage
import com.hyphenate.easeui.common.bus.EaseFlowBus
import com.hyphenate.easeui.common.extensions.ioScope
import com.hyphenate.easeui.common.impl.ValueCallbackImpl
import com.hyphenate.easeui.interfaces.EaseConnectionListener
import com.hyphenate.easeui.interfaces.EaseMessageListener
import com.hyphenate.easeui.model.EaseEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ListenersWrapper {
    private var isLoadGroupList = false

    private val connectListener by lazy {
        object : EaseConnectionListener() {
            override fun onConnected() {
                // do something
                CoroutineScope(Dispatchers.IO).launch {
                    val groups = ChatClient.getInstance().groupManager().allGroups
                    if (isLoadGroupList.not() && groups.isEmpty()) {
                        ChatClient.getInstance().groupManager().asyncGetJoinedGroupsFromServer(ValueCallbackImpl<List<ChatGroup>>(onSuccess = {
                            isLoadGroupList = true
                            if (it.isEmpty().not()) {
                                EaseFlowBus.with<EaseEvent>(EaseEvent.EVENT.UPDATE.name)
                                    .post(DemoHelper.getInstance().context.ioScope(),
                                        EaseEvent(EaseEvent.EVENT.UPDATE.name, EaseEvent.TYPE.GROUP))
                            }
                        }, onError = {_,_ ->

                        }))
                    }
                }

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

    private val messageListener by lazy { object : EaseMessageListener(){
        override fun onMessageReceived(messages: MutableList<ChatMessage>?) {
            super.onMessageReceived(messages)
            if (DemoHelper.getInstance().getDataModel().isAppPushSilent()) {
                return
            }
            // do something
            messages?.forEach { message ->

                if (EaseIM.checkMutedConversationList(message.conversationId())) {
                    return@forEach
                }
                if (DemoApplication.getInstance().getLifecycleCallbacks().isFront.not()) {
                    DemoHelper.getInstance().getNotifier()?.notify(message)
                }
                // notify new message
                DemoHelper.getInstance().getNotifier()?.vibrateAndPlayTone(message)
            }
        }
    } }

    fun registerListeners() {
        // register connection listener
        EaseIM.addConnectionListener(connectListener)
        EaseIM.addChatMessageListener(messageListener)
    }
}