package com.hyphenate.chatdemo.ui.group

import com.hyphenate.easeui.R
import com.hyphenate.easeui.feature.chat.activities.EaseChatActivity
import com.hyphenate.easeui.feature.chat.enums.EaseChatType
import com.hyphenate.easeui.feature.group.EaseGroupDetailActivity
import com.hyphenate.easeui.feature.search.EaseSearchActivity
import com.hyphenate.easeui.feature.search.EaseSearchType
import com.hyphenate.easeui.model.EaseMenuItem

class ChatGroupDetailActivity :EaseGroupDetailActivity(){

    override fun onMenuItemClick(item: EaseMenuItem?, position: Int): Boolean {
        item?.let {menu->
            when(menu.menuId){
                R.id.extend_item_message -> {
                    groupId?.let {
                        EaseChatActivity.actionStart(mContext, it, EaseChatType.GROUP_CHAT)
                    }
                }
                R.id.extend_item_audio_call -> {

                }
                R.id.extend_item_video_call -> {

                }
                R.id.extend_item_search -> {
                    mContext.startActivity(EaseSearchActivity.createIntent(
                            mContext, EaseSearchType.MESSAGE, groupId
                        )
                    )
                }
                else -> {}
            }
            return true
        }
        return false
    }
}