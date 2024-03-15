package com.hyphenate.chatdemo.ui.conversation

import android.os.Bundle
import android.view.ViewGroup
import com.hyphenate.chatdemo.R
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.common.extensions.dpToPx
import com.hyphenate.easeui.configs.setAvatarStyle
import com.hyphenate.easeui.feature.conversation.EaseConversationListFragment

class ConversationListFragment: EaseConversationListFragment() {

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding?.titleConversations?.let {
            EaseIM.getConfig()?.avatarConfig?.setAvatarStyle(it.getLogoView())
            it.setLogo(EaseIM.getCurrentUser()?.avatar, com.hyphenate.easeui.R.drawable.ease_default_avatar, 32.dpToPx(mContext))
            val layoutParams = it.getLogoView()?.layoutParams as? ViewGroup.MarginLayoutParams
            layoutParams?.marginStart = 12.dpToPx(mContext)
            it.getTitleView().let { text ->
                text.text = ""
            }
            it.setTitleEndDrawable(R.drawable.conversation_title)
        }
    }
}