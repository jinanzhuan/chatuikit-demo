package com.hyphenate.chatdemo.ui.contact

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hyphenate.chatdemo.R
import com.hyphenate.chatdemo.ui.me.EditUserNicknameActivity
import com.hyphenate.chatdemo.viewmodel.ProfileInfoViewModel
import com.hyphenate.easeui.common.ChatLog
import com.hyphenate.easeui.common.extensions.catchChatException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatContactRemarkActivity: EditUserNicknameActivity() {
    private var model: ProfileInfoViewModel? = null
    private var targetUserId:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        targetUserId = intent.getStringExtra(KEY_USER_ID)
        super.onCreate(savedInstanceState)
    }
    override fun initTitle() {
        model = ViewModelProvider(this)[ProfileInfoViewModel::class.java]
        binding.run {
            titleBar.setTitle(getString(R.string.demo_contact_edit_remark))
            etName.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(128))
            targetUserId?.let {
                val remark = model?.fetchLocalUserRemark(it)
                etName.setText(remark)
                inputNameCount.text = resources.getString(R.string.demo_contact_remark_count,remark?.length?:0)
            }
        }
    }

    override fun initListener() {
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
                        resources.getString(R.string.demo_contact_remark_count, 0)
                }else{
                    binding.inputNameCount.text =
                        resources.getString(R.string.demo_contact_remark_count, length)

                }
                updateSaveView(binding.etName.text.length)
            }
        })
        binding.titleBar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                com.hyphenate.easeui.R.id.action_save -> {
                    setRemark()
                }
                else -> {}
            }
            true
        }
    }

    private fun setRemark(){
        targetUserId?.let {
            lifecycleScope.launch {
                var remark = binding.etName.text.toString()
                if (remark.isNullOrEmpty()) {
                    remark = ""
                }
                model?.setUserRemark(it,remark)
                    ?.onStart { showLoading(true) }
                    ?.onCompletion { dismissLoading() }
                    ?.catchChatException { e ->
                        ChatLog.e("TAG", "setRemark fail error message = " + e.description)
                    }?.
                    stateIn(lifecycleScope, SharingStarted.WhileSubscribed(5000), -1)?.
                    collect {
                        if (it != -1) {
                            val resultIntent = Intent()
                            resultIntent.putExtra(RESULT_UPDATE_REMARK, remark)
                            setResult(RESULT_OK,resultIntent)
                            finish()
                        }
                    }
            }
        }

    }

    companion object {
        private const val TAG = "ChatContactRemarkActivity"
        private const val KEY_USER_ID = "key_user_id"
        private const val RESULT_UPDATE_REMARK = "result_update_remark"

        fun createIntent(
            context: Context,
            userId: String
        ): Intent {
            val intent = Intent(context, ChatContactRemarkActivity::class.java)
            intent.putExtra(KEY_USER_ID, userId)
            return intent
        }
    }
}