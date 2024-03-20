package com.hyphenate.chatdemo.ui.contact

import android.widget.TextView
import com.hyphenate.chatdemo.R
import com.hyphenate.easeui.feature.chat.activities.EaseChatActivity
import com.hyphenate.easeui.feature.chat.enums.EaseChatType
import com.hyphenate.easeui.feature.contact.EaseContactDetailsActivity
import com.hyphenate.easeui.model.EaseMenuItem
import com.hyphenate.easeui.widget.EaseArrowItemView


class ChatContactDetailActivity: EaseContactDetailsActivity() {

    private val remarkItem: EaseArrowItemView by lazy { findViewById(R.id.item_remark) }
    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        remarkItem.setOnClickListener{

        }
    }

    override fun onMenuItemClick(item: EaseMenuItem?, position: Int): Boolean {
        item?.let {
            when(item.menuId){
                com.hyphenate.easeui.R.id.extend_item_message -> {
                    user?.userId?.let { conversationId ->
                        EaseChatActivity.actionStart(mContext,
                            conversationId, EaseChatType.SINGLE_CHAT)
                    }
                }
                com.hyphenate.easeui.R.id.extend_item_audio_call -> {

                }
                com.hyphenate.easeui.R.id.extend_item_video_call -> {

                }
                else -> {}
            }
            return true
        }
        return false
    }
}