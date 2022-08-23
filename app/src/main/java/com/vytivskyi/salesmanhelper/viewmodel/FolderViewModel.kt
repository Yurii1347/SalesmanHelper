package com.vytivskyi.salesmanhelper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vytivskyi.salesmanhelper.model.repository.FolderRepository
import com.vytivskyi.salesmanhelper.model.room.FolderRoomDatabase
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.model.room.entity.relation.FolderWithProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FolderViewModel(application: Application) : AndroidViewModel(application) {

    val allFolders: LiveData<List<FolderWithProducts>>
    val allProducts: LiveData<List<Product>>

    private val repository: FolderRepository

    init {
        val folderDao = FolderRoomDatabase.getDatabase(application.applicationContext).folderDao()
        repository = FolderRepository(folderDao)

        allFolders = repository.allFolders
        allProducts = repository.allProducts
    }

    fun addFolder(folder: Folder) = viewModelScope.launch(Dispatchers.IO) {
        repository.addFolder(folder)
    }

    fun deleteFolder(folder: FolderWithProducts) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFolder(folder.folder)
        folder.products.forEach {
            deleteAppProducts(it)
        }
    }

    fun updateFolder(folder: Folder) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateFolder(folder)
    }

    private fun deleteAppProducts(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllProducts(product)
    }


}