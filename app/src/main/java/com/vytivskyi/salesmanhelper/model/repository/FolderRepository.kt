package com.vytivskyi.salesmanhelper.model.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.vytivskyi.salesmanhelper.model.room.dao.FolderDao
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.model.room.entity.Product

class FolderRepository(private val folderDao: FolderDao) {

    val allFolders: LiveData<MutableList<Folder>> = folderDao.getFolders()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addFolder(folder: Folder){
        folderDao.addFolder(folder)
    }
    suspend fun deleteFolder(folder: Folder){
        folderDao.deleteFolder(folder)
    }
    suspend fun updateFolder(folder: Folder){
        folderDao.updateFolder(folder)
    }
}