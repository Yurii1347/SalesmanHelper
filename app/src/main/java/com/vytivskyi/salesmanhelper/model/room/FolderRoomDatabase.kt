package com.vytivskyi.salesmanhelper.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vytivskyi.salesmanhelper.model.room.dao.FolderDao
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.model.room.entity.Product

@Database(entities = [Folder::class, Product::class], version = 2, exportSchema = false)
abstract class FolderRoomDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao

    companion object {
        @Volatile
        private var INSTANCE: FolderRoomDatabase? = null

        fun getDatabase(context: Context): FolderRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FolderRoomDatabase::class.java,
                    "folder_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}