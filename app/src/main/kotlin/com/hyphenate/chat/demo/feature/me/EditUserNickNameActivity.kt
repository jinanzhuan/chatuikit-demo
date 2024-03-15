package com.hyphenate.chat.demo.feature.me

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.hyphenate.chat.demo.R
import com.hyphenate.chat.demo.databinding.DemoActivityMeInformationEditBinding
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.base.EaseBaseActivity
import com.hyphenate.easeui.common.bus.EaseFlowBus
import com.hyphenate.easeui.model.EaseEvent
import com.hyphenate.easeui.model.EaseProfile

class EditUserNickNameActivity:EaseBaseActivity<DemoActivityMeInformationEditBinding>() {
    private var selfProfile:EaseProfile? = null
    private var newName:String = ""

    companion object{
        private const val RESULT_REFRESH = "isRefresh"
    }

    override fun getViewBinding(inflater: LayoutInflater): DemoActivityMeInformationEditBinding {
       return DemoActivityMeInformationEditBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selfProfile = EaseIM.getCurrentUser()
        binding.run {
            titleBar.setTitle(getString(R.string.main_about_me_information_edit_nick_name))
            selfProfile?.let {
                etName.setText(it.name)
            }
        }
        binding.inputNameCount.text = resources.getString(R.string.main_about_me_information_change_name_count
            ,selfProfile?.name?.length ?: 0)

        initListener()
    }

    private fun initListener(){
        binding.titleBar.setNavigationOnClickListener { mContext.onBackPressed() }
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val length = s.toString().trim().length
                if (length == 0){
                    binding.inputNameCount.text =
                        resources.getString(R.string.main_about_me_information_change_name_count, 0)
                }else{
                    binding.inputNameCount.text =
                        resources.getString(R.string.main_about_me_information_change_name_count, length)

                }
                updateSaveView(binding.etName.text.length)
            }
        })
        binding.titleBar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                com.hyphenate.easeui.R.id.action_save -> {
                    updateUserInfo()
                }

                else -> {}
            }
            true
        }
    }

    private fun updateSaveView(length: Int){
        binding.titleBar.setMenuTitleColor(
            ContextCompat.getColor(mContext,
            if (length != 0) com.hyphenate.easeui.R.color.ease_color_primary
            else com.hyphenate.easeui.R.color.ease_color_on_background_high))
    }

    private fun updateUserInfo(){
        newName = binding.etName.text.trim().toString()
        selfProfile = EaseIM.getCurrentUser()
        if (newName.isNotEmpty()){
            selfProfile?.name = newName
            selfProfile?.let { EaseIM.updateCurrentUser(it) }
            val resultIntent = Intent()
            resultIntent.putExtra(RESULT_REFRESH, true)
            setResult(RESULT_OK,resultIntent)

            EaseFlowBus.with<EaseEvent>(EaseEvent.EVENT.UPDATE.name).post(lifecycleScope,
                EaseEvent(EaseEvent.EVENT.UPDATE.name, EaseEvent.TYPE.CONTACT))

        }
        finish()
    }

}