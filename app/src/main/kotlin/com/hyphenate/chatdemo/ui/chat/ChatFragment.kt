package com.hyphenate.chatdemo.ui.chat

import android.os.Bundle
import com.hyphenate.chatdemo.R
import com.hyphenate.chatdemo.callkit.CallKitManager
import com.hyphenate.easeui.feature.chat.EaseChatFragment
import com.hyphenate.easeui.feature.chat.enums.EaseChatType
import com.hyphenate.easeui.feature.thread.EaseChatThreadListActivity

class ChatFragment: EaseChatFragment() {

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding?.titleBar?.inflateMenu(R.menu.demo_chat_menu)
        if (chatType == EaseChatType.SINGLE_CHAT){
            binding?.titleBar?.setMenuIconVisible(R.id.chat_menu_thread,false)
        }
        setMenuListener()
    }

    private fun setMenuListener() {
        binding?.titleBar?.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.chat_menu_video_call -> {
                    showVideoCall()
                    true
                }
                R.id.chat_menu_thread -> {
                    EaseChatThreadListActivity.actionStart(mContext,conversationId)
                    true
                }
                else -> false
            }
        }
    }

    private fun showVideoCall() {
        if (chatType == EaseChatType.SINGLE_CHAT) {
            CallKitManager.showSelectDialog(mContext, conversationId)
        } else {
            CallKitManager.startConferenceCall(mContext, conversationId)
        }
    }

    override fun cancelMultipleSelectStyle() {
        super.cancelMultipleSelectStyle()
        setMenuListener()
    }
}