package com.vytivskyi.salesmanhelper.model.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.vytivskyi.salesmanhelper.model.room.dao.FolderDao
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.model.room.entity.relation.FolderWithProducts

class FolderRepository(private val folderDao: FolderDao) {

    val allFolders: LiveData<List<FolderWithProducts>> = folderDao.getAllFoldersWithProducts()
    val allProducts: LiveData<List<Product>> = folderDao.getProducts()

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
    suspend fun deleteAllProducts(product: Product) {
        folderDao.deleteProduct(product)
    }

}