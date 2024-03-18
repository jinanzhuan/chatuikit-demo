package com.hyphenate.chatdemo.callkit

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hyphenate.easeui.base.EaseBaseRecyclerViewAdapter
import com.hyphenate.easeui.databinding.EaseLayoutGroupSelectContactBinding
import com.hyphenate.easeui.feature.search.interfaces.OnContactSelectListener
import com.hyphenate.easeui.model.EaseUser

class ConferenceInviteAdapter(private val groupId: String?): EaseBaseRecyclerViewAdapter<EaseUser>() {
    private var selectedListener: OnContactSelectListener? = null
    private var selectedMember:MutableList<String> = mutableListOf()

    override fun getViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<EaseUser> {
        return ConferenceMemberSelectViewHolder(groupId,
                EaseLayoutGroupSelectContactBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false)
            )
    }

    override fun onBindViewHolder(holder: ViewHolder<EaseUser>, position: Int) {
        if (holder is ConferenceMemberSelectViewHolder){
            holder.setSelectedMembers(selectedMember)
            holder.setCheckBoxSelectListener(selectedListener)
        }
        super.onBindViewHolder(holder, position)
    }

    fun setSelectedMembers(selectedMember:MutableList<String>){
        this.selectedMember = selectedMember
        notifyDataSetChanged()
    }

    /**
     * Set the listener for the checkbox selection.
     */
    fun setCheckBoxSelectListener(listener: OnContactSelectListener){
        this.selectedListener = listener
        notifyDataSetChanged()
    }

}