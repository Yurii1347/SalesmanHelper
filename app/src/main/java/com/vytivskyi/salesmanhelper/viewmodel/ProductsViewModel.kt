package com.vytivskyi.salesmanhelper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vytivskyi.salesmanhelper.model.repository.ProductRepository
import com.vytivskyi.salesmanhelper.model.room.FolderRoomDatabase
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.model.room.entity.relation.FolderWithProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel(application: Application, folderId: Int) :
    AndroidViewModel(application) {

    val allProductsFromFolder: LiveData<List<FolderWithProducts>>

    private val repository: ProductRepository

    init {
        val folderDao = FolderRoomDatabase.getDatabase(application.applicationContext).folderDao()
        repository = ProductRepository(folderDao, folderId)

        allProductsFromFolder = repository.allProductsFromFolder
    }

    fun addProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.addProduct(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.editProduct(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteProduct(product)
    }

}