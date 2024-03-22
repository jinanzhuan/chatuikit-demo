package com.hyphenate.chatdemo.common.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyphenate.chatdemo.common.extensions.MD5
import com.hyphenate.chatdemo.common.room.dao.DemoGroupDao
import com.hyphenate.chatdemo.common.room.dao.DemoUserDao
import com.hyphenate.chatdemo.common.room.entity.DemoGroup
import com.hyphenate.chatdemo.common.room.entity.DemoUser

@Database(entities = [DemoUser::class, DemoGroup::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    /**
     * Get the user data access object.
     */
    abstract fun userDao(): DemoUserDao

    /**
     * Get the group data access object.
     */
    abstract fun groupDao(): DemoGroupDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 以下数据库升级设置，为升级数据库将清掉之前的数据，如果要保留数据，慎重采用此种方式
        // 可以采用addMigrations()的方式，进行数据库的升级
        fun getDatabase(context: Context, userId: String): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val dbName = userId.MD5()
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        dbName
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}