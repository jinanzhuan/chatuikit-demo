package com.hyphenate.chatdemo.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import com.hyphenate.chatdemo.R
import com.hyphenate.chatdemo.databinding.DemoActivityFeaturesBinding
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.base.EaseBaseActivity
import com.hyphenate.easeui.widget.EaseSwitchItemView

class FeaturesActivity:EaseBaseActivity<DemoActivityFeaturesBinding>(),
    EaseSwitchItemView.OnCheckedChangeListener {
    override fun getViewBinding(inflater: LayoutInflater): DemoActivityFeaturesBinding {
        return DemoActivityFeaturesBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    fun initView(){
        val enableTranslation = EaseIM.getConfig()?.chatConfig?.enableTranslationMessage ?: false
        val enableThread = EaseIM.getConfig()?.chatConfig?.enableChatThreadMessage ?: false
        val enableReaction = EaseIM.getConfig()?.chatConfig?.enableMessageReaction ?: false

        if (enableTranslation){
            binding.switchItemTranslation.setChecked(true)
        }else{
            binding.switchItemTranslation.setChecked(false)
        }
        binding.switchItemTranslation.setSwitchTarckDrawable(com.hyphenate.easeui.R.drawable.ease_switch_track_selector)
        binding.switchItemTranslation.setSwitchThumbDrawable(com.hyphenate.easeui.R.drawable.ease_switch_thumb_selector)

        if (enableThread){
            binding.switchItemTopic.setChecked(true)
        }else{
            binding.switchItemTopic.setChecked(false)
        }
        binding.switchItemTopic.setSwitchTarckDrawable(com.hyphenate.easeui.R.drawable.ease_switch_track_selector)
        binding.switchItemTopic.setSwitchThumbDrawable(com.hyphenate.easeui.R.drawable.ease_switch_thumb_selector)

        if (enableReaction){
            binding.switchItemReaction.setChecked(true)
        }else{
            binding.switchItemReaction.setChecked(false)
        }
        binding.switchItemReaction.setSwitchTarckDrawable(com.hyphenate.easeui.R.drawable.ease_switch_track_selector)
        binding.switchItemReaction.setSwitchThumbDrawable(com.hyphenate.easeui.R.drawable.ease_switch_thumb_selector)
    }

    fun initListener(){
        binding.let {
            it.titleBar.setNavigationOnClickListener{
                mContext.onBackPressed()
            }
            it.switchItemTranslation.setOnCheckedChangeListener(this)
            it.switchItemTopic.setOnCheckedChangeListener(this)
            it.switchItemReaction.setOnCheckedChangeListener(this)
        }
    }

    override fun onCheckedChanged(buttonView: EaseSwitchItemView?, isChecked: Boolean) {
        when(buttonView?.id){
            R.id.switch_item_translation ->{
                EaseIM.getConfig()?.chatConfig?.enableTranslationMessage = isChecked
            }
            R.id.switch_item_topic ->{
                EaseIM.getConfig()?.chatConfig?.enableChatThreadMessage = isChecked
            }
            R.id.switch_item_reaction -> {
                EaseIM.getConfig()?.chatConfig?.enableMessageReaction = isChecked
            }
            else -> {}
        }
    }
}