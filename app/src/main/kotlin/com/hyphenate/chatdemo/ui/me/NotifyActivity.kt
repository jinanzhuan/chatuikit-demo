package com.hyphenate.chatdemo.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import com.hyphenate.chatdemo.R
import com.hyphenate.chatdemo.common.DemoConstant
import com.hyphenate.chatdemo.databinding.DemoActivityNotifyBinding
import com.hyphenate.easeui.base.EaseBaseActivity
import com.hyphenate.easeui.common.helper.EasePreferenceManager
import com.hyphenate.easeui.widget.EaseSwitchItemView

class NotifyActivity:EaseBaseActivity<DemoActivityNotifyBinding>(),
    EaseSwitchItemView.OnCheckedChangeListener {
    override fun getViewBinding(inflater: LayoutInflater): DemoActivityNotifyBinding? {
        return DemoActivityNotifyBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    private fun initView(){
        initSwitch()
    }

    private fun initSwitch(){
        val isDisturbance = EasePreferenceManager.getInstance().getBoolean(DemoConstant.MSG_NO_DISTURBANCE)
        if (isDisturbance){
            binding.switchItemNotify.setChecked(true)
        }else{
            binding.switchItemNotify.setChecked(false)
        }
        binding.switchItemNotify.setSwitchTarckDrawable(com.hyphenate.easeui.R.drawable.ease_switch_track_selector)
        binding.switchItemNotify.setSwitchThumbDrawable(com.hyphenate.easeui.R.drawable.ease_switch_thumb_selector)
    }

    private fun initListener(){
        binding.let {
            it.titleBar.setNavigationOnClickListener{
                mContext.onBackPressed()
            }
            it.switchItemNotify.setOnCheckedChangeListener(this)
        }
    }

    override fun onCheckedChanged(buttonView: EaseSwitchItemView?, isChecked: Boolean) {
        when(buttonView?.id){
            R.id.switch_item_notify ->{
                EasePreferenceManager.getInstance().putBoolean(DemoConstant.MSG_NO_DISTURBANCE,isChecked)
            }
            else -> {}
        }
    }
}