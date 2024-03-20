package com.hyphenate.chatdemo.ui.me

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.hyphenate.chatdemo.R
import com.hyphenate.chatdemo.databinding.DemoActivityAboutBinding
import com.hyphenate.easeui.base.EaseBaseActivity

class AboutActivity:EaseBaseActivity<DemoActivityAboutBinding>(), View.OnClickListener {
    override fun getViewBinding(inflater: LayoutInflater): DemoActivityAboutBinding? {
        return DemoActivityAboutBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    private fun initView(){
        binding.let {
            it.tvVersion.text = getString(R.string.about_version,"1.1.0")
            it.tvKitVersion.text = getString(R.string.about_uikit_version,"1.1.0")
        }
    }

    private fun initListener(){
        binding.let {
            it.titleBar.setNavigationOnClickListener{
                mContext.onBackPressed()
            }
            it.arrowItemOfficialWebsite.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.arrow_item_official_website -> {
                startActivity(Intent(this@AboutActivity,WebViewActivity::class.java))
            }
            else -> {}
        }
    }
}