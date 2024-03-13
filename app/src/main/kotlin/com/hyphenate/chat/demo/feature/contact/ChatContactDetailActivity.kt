package com.hyphenate.chat.demo.feature.contact

import com.hyphenate.easeui.R
import com.hyphenate.easeui.feature.chat.activities.EaseChatActivity
import com.hyphenate.easeui.feature.chat.enums.EaseChatType
import com.hyphenate.easeui.feature.contact.EaseContactDetailsActivity
import com.hyphenate.easeui.model.EaseMenuItem

class ChatContactDetailActivity:EaseContactDetailsActivity() {

    override fun onMenuItemClick(item: EaseMenuItem?, position: Int): Boolean {
        item?.let {
            when(item.menuId){
                R.id.extend_item_message -> {
                    user?.userId?.let { conversationId ->
                        EaseChatActivity.actionStart(mContext,
                            conversationId, EaseChatType.SINGLE_CHAT)
                    }
                }
                R.id.extend_item_audio_call -> {

                }
                R.id.extend_item_video_call -> {

                }
                else -> {}
            }
            return true
        }
        return false
    }
}