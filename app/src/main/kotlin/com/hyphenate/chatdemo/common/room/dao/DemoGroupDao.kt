package com.hyphenate.chatdemo.common.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hyphenate.chatdemo.common.room.entity.DemoGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface DemoGroupDao {

    // Query all groups
    @Query("SELECT * FROM DemoGroup")
    fun getAll(): Flow<List<DemoGroup>>

    // Query group by id
    @Query("SELECT * FROM DemoGroup WHERE id = :id")
    fun getGroupById(id: String): Flow<DemoGroup>

    // Query groups by ids
    @Query("SELECT * FROM DemoGroup WHERE id IN (:ids)")
    fun getGroupsByIds(ids: List<String>): Flow<List<DemoGroup>>

    // Query groups by name
    @Query("SELECT * FROM DemoGroup WHERE name LIKE :name")
    fun getGroupsByName(name: String?): Flow<List<DemoGroup>>

    // Insert group
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroup(group: DemoGroup)

    // Insert group list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroups(groups: List<DemoGroup>)

    // Update group
    @Query("UPDATE DemoGroup SET name = :name, avatar = :avatar WHERE id = :id")
    fun updateGroup(id: String, name: String, avatar: String)

    // Update group by DemoGroup
    @Update
    fun updateGroup(group: DemoGroup)

    // Update group list
    @Update
    fun updateGroups(groups: List<DemoGroup>)

    // Update group name
    @Query("UPDATE DemoGroup SET name = :name WHERE id = :id")
    fun updateGroupName(id: String, name: String)

    // Update group avatar
    @Query("UPDATE DemoGroup SET avatar = :avatar WHERE id = :id")
    fun updateGroupAvatar(id: String, avatar: String)

    // Delete group
    @Delete
    fun deleteGroup(group: DemoGroup)

    // Delete group by id
    @Query("DELETE FROM DemoGroup WHERE id = :id")
    fun deleteGroupById(id: String)

    // Delete group list
    @Delete
    fun deleteGroups(groups: List<DemoGroup>)

    // Delete by group id list
    @Query("DELETE FROM DemoGroup WHERE id IN (:ids)")
    fun deleteGroupsByIds(ids: List<String>)

    // Delete all groups
    @Query("DELETE FROM DemoGroup")
    fun deleteAllGroups()
}