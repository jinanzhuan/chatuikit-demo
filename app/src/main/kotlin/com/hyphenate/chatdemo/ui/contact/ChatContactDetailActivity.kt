package com.hyphenate.chatdemo.ui.contact

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.hyphenate.chatdemo.R
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.hyphenate.chatdemo.callkit.CallKitManager
import com.hyphenate.chatdemo.viewmodel.ProfileInfoViewModel
import com.hyphenate.easeui.common.extensions.showToast
import com.hyphenate.easeui.feature.contact.EaseContactDetailsActivity
import com.hyphenate.easeui.model.EaseMenuItem
import com.hyphenate.easeui.widget.EaseArrowItemView


class ChatContactDetailActivity:EaseContactDetailsActivity() {
    private lateinit var model: ProfileInfoViewModel
    private val remarkItem: EaseArrowItemView by lazy { findViewById(R.id.item_remark) }

    companion object {
        private const val REQUEST_UPDATE_REMARK = 100
        private const val RESULT_UPDATE_REMARK = "result_update_remark"
    }

    private val launcherToUpdateRemark: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onActivityResult(result, REQUEST_UPDATE_REMARK) }

    override fun initView() {
        super.initView()
        model = ViewModelProvider(this)[ProfileInfoViewModel::class.java]
        user?.let {
            val remark = model.fetchLocalUserRemark(it.userId)
            remarkItem.setContent(remark)
        }
    }

    override fun initListener() {
        super.initListener()
        remarkItem.setOnClickListener{
            user?.let {
                launcherToUpdateRemark.launch(ChatContactRemarkActivity.createIntent(mContext,it.userId))
            }
        }
        binding.tvNumber
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

    private fun onActivityResult(result: ActivityResult, requestCode: Int) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            when (requestCode) {
                REQUEST_UPDATE_REMARK ->{
                    data?.let {
                        if (it.hasExtra(RESULT_UPDATE_REMARK)){
                            remarkItem.setContent(it.getStringExtra(RESULT_UPDATE_REMARK))
                        }
                    }
                }
                else -> {}
            }
        }
    }

    override fun onPrimaryClipChanged() {
        super.onPrimaryClipChanged()
        mContext.showToast(R.string.system_copy_success)
    }
}