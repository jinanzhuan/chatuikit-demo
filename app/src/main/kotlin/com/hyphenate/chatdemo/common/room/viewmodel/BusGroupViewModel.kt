package com.hyphenate.chatdemo.common.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyphenate.chatdemo.common.room.dao.DemoGroupDao
import com.hyphenate.chatdemo.common.room.entity.DemoGroup

class BusGroupViewModel(private val groupDao: DemoGroupDao): ViewModel() {

    /**
     * Query all groups from the database.
     * @return All groups in the database.
     */
    fun getAllGroups() = groupDao.getAll()

    /**
     * Query group by id from the database.
     * @param id The id of the group.
     * @return The group with the specified id.
     */
    fun getGroupById(id: String) = groupDao.getGroupById(id)

    /**
     * Query groups by ids from the database.
     * @param ids The ids of the groups.
     * @return The groups with the specified ids.
     */
    fun getGroupsByIds(ids: List<String>) = groupDao.getGroupsByIds(ids)

    /**
     * Query groups by name from the database.
     * @param name The name of the group.
     * @return The groups with the specified name.
     */
    fun getGroupsByName(name: String?) = groupDao.getGroupsByName(name)

    /**
     * Insert a group into the database.
     * @param group The group to be inserted.
     */
    fun insertGroup(group: DemoGroup) = groupDao.insertGroup(group)

    /**
     * Insert a list of groups into the database.
     * @param groups The list of groups to be inserted.
     */
    fun insertGroups(groups: List<DemoGroup>) = groupDao.insertGroups(groups)

    /**
     * Update a group in the database.
     * @param id The id of the group.
     * @param name The name of the group.
     * @param avatar The avatar of the group.
     */
    fun updateGroup(id: String, name: String, avatar: String) = groupDao.updateGroup(id, name, avatar)

    /**
     * Update a group in the database.
     * @param group The group to be updated.
     */
    fun updateGroup(group: DemoGroup) = groupDao.updateGroup(group)

    /**
     * Update a list of groups in the database.
     * @param groups The list of groups to be updated.
     */
    fun updateGroups(groups: List<DemoGroup>) = groupDao.updateGroups(groups)

    /**
     * Update the name of a group in the database.
     * @param id The id of the group.
     * @param name The name of the group.
     */
    fun updateGroupName(id: String, name: String) = groupDao.updateGroupName(id, name)

    /**
     * Update the avatar of a group in the database.
     * @param id The id of the group.
     * @param avatar The avatar of the group.
     */
    fun updateGroupAvatar(id: String, avatar: String) = groupDao.updateGroupAvatar(id, avatar)

    /**
     * Delete a group from the database.
     * @param group The group to be deleted.
     */
    fun deleteGroup(group: DemoGroup) = groupDao.deleteGroup(group)

    /**
     * Delete a list of groups from the database.
     * @param groups The list of groups to be deleted.
     */
    fun deleteGroups(groups: List<DemoGroup>) = groupDao.deleteGroups(groups)

    /**
     * Delete a group by id from the database.
     * @param id The id of the group to be deleted.
     */
    fun deleteGroupById(id: String) = groupDao.deleteGroupById(id)

    /**
     * Delete a list of groups by ids from the database.
     * @param ids The ids of the groups to be deleted.
     */
    fun deleteGroupsByIds(ids: List<String>) = groupDao.deleteGroupsByIds(ids)

    /**
     * Delete all groups from the database.
     */
    fun deleteAllGroups() = groupDao.deleteAllGroups()

}

class BusGroupViewModelFactory(private val groupDao: DemoGroupDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusGroupViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusGroupViewModel(groupDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}