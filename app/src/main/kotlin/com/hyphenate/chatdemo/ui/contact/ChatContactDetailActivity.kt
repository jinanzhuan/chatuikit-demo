package com.hyphenate.chatdemo.ui.contact

import com.hyphenate.chatdemo.R
import androidx.core.content.ContextCompat
import com.hyphenate.chatdemo.callkit.CallKitManager
import com.hyphenate.easeui.feature.contact.EaseContactDetailsActivity
import com.hyphenate.easeui.model.EaseMenuItem
import com.hyphenate.easeui.widget.EaseArrowItemView


class ChatContactDetailActivity:EaseContactDetailsActivity() {

    private val remarkItem: EaseArrowItemView by lazy { findViewById(R.id.item_remark) }

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        remarkItem.setOnClickListener{

        }
    }

    override fun getDetailItem(): MutableList<EaseMenuItem>? {
        val list = super.getDetailItem()
        val audioItem = EaseMenuItem(
            title = getString(R.string.detail_item_audio),
            resourceId = R.drawable.ease_phone_pick,
            menuId = R.id.contact_item_audio_call,
            titleColor = ContextCompat.getColor(this, com.hyphenate.easeui.R.color.ease_color_primary),
            order = 2
        )

        val videoItem = EaseMenuItem(
            title = getString(R.string.detail_item_video),
            resourceId = R.drawable.ease_video_camera,
            menuId = R.id.contact_item_video_call,
            titleColor = ContextCompat.getColor(this, com.hyphenate.easeui.R.color.ease_color_primary),
            order = 3
        )
        list?.add(audioItem)
        list?.add(videoItem)
        return list
    }

    override fun onMenuItemClick(item: EaseMenuItem?, position: Int): Boolean {
        item?.let {
            when(item.menuId){
                R.id.contact_item_audio_call -> {
                    CallKitManager.startSingleAudioCall(user?.userId)
                    return true
                }
                R.id.contact_item_video_call -> {
                    CallKitManager.startSingleVideoCall(user?.userId)
                    return true
                }
                else -> {
                    return super.onMenuItemClick(item, position)
                }
            }
        }
        return false
    }
}