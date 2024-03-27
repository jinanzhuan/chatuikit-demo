package com.hyphenate.chatdemo.ui.contact

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hyphenate.chatdemo.DemoHelper
import com.hyphenate.chatdemo.common.DemoConstant
import com.hyphenate.chatdemo.common.room.entity.parse
import com.hyphenate.chatdemo.common.room.extensions.parseToDbBean
import com.hyphenate.chatdemo.viewmodel.ProfileInfoViewModel
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.common.ChatClient
import com.hyphenate.easeui.common.ChatLog
import com.hyphenate.easeui.common.ChatUserInfoType
import com.hyphenate.easeui.common.bus.EaseFlowBus
import com.hyphenate.easeui.common.extensions.catchChatException
import com.hyphenate.easeui.feature.invitation.EaseNewRequestsActivity
import com.hyphenate.easeui.model.EaseEvent
import kotlinx.coroutines.launch

class ChatNewRequestsActivity:EaseNewRequestsActivity() {
    private lateinit var model: ProfileInfoViewModel
    override fun initData(){
        super.initData()
        model = ViewModelProvider(this)[ProfileInfoViewModel::class.java]
    }

    override fun agreeInviteSuccess(userId: String) {
        super.agreeInviteSuccess(userId)

        lifecycleScope.launch {
            model.fetchUserInfoAttribute(listOf(userId), listOf(ChatUserInfoType.NICKNAME, ChatUserInfoType.AVATAR_URL))
                .catchChatException {
                    ChatLog.e("ChatNewRequestsActivity", "fetchUserInfoAttribute error: ${it.description}")
                }
                .collect {
                    for (value in it.values) {
                        Log.e("apex","agreeInviteSuccess ${value.userId} - ${value.nickname}")
                    }
                    it[userId]?.parseToDbBean()?.let {u->
                        u.parse().apply {
                            remark = ChatClient.getInstance().contactManager().fetchContactFromLocal(id)?.remark ?:""
                            EaseIM.updateUsersInfo(mutableListOf(this))
                            DemoHelper.getInstance().getDataModel().insertUser(this)
                        }
                        notifyUpdateRemarkEvent(userId)
                    }
                }
        }
    }

    private fun notifyUpdateRemarkEvent(userId:String) {
        DemoHelper.getInstance().getDataModel().updateUserCache(userId)
        refreshData()
        EaseFlowBus.with<EaseEvent>(EaseEvent.EVENT.UPDATE + EaseEvent.TYPE.CONTACT + DemoConstant.EVENT_UPDATE_USER_SUFFIX)
            .post(lifecycleScope, EaseEvent(DemoConstant.EVENT_UPDATE_USER_SUFFIX, EaseEvent.TYPE.CONTACT, userId))
    }
}