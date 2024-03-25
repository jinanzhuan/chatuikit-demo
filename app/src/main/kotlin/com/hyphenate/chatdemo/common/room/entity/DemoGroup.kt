package com.hyphenate.chatdemo.common.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hyphenate.easeui.model.EaseGroupProfile

@Entity
data class DemoGroup(
    @PrimaryKey val id: String,
    val name: String?,
    val avatar: String?
)

/**
 * Convert the group data to the profile data.
 */
internal fun DemoGroup.parse() = EaseGroupProfile(id, name, avatar)
