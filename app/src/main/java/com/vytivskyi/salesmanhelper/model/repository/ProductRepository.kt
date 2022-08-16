package com.vytivskyi.salesmanhelper.model.repository

import androidx.lifecycle.LiveData
import com.vytivskyi.salesmanhelper.model.room.dao.FolderDao
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.model.room.entity.relation.FolderWithProducts

class ProductRepository(private val folderDao: FolderDao, folderId: Int) {

    val allProductsFromFolder: LiveData<MutableList<FolderWithProducts>> =
        folderDao.getFolderWithProducts(folderId)

    suspend fun addProduct(product: Product) {
        folderDao.addProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        folderDao.deleteProduct(product)
    }

    suspend fun editProduct(product: Product) {
        folderDao.updateProduct(product)
    }
}