package com.vytivskyi.salesmanhelper.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.model.room.entity.relation.FolderWithProducts

@Dao
interface FolderDao {
    @Query("SELECT * FROM folder")
    fun getFolders(): LiveData<List<Folder>>

    @Query("SELECT * FROM product")
    fun getProducts(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFolder(folder: Folder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: Product)

    @Delete
    suspend fun deleteFolder(folder: Folder)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateFolder(folder: Folder)

    @Update
    suspend fun updateProduct(product: Product)

    @Transaction
    @Query("SELECT * FROM folder WHERE folderId = :folderId")
    fun getFolderWithProducts(folderId: Int): LiveData<List<FolderWithProducts>>

    @Query("SELECT * FROM folder")
    fun getAllFoldersWithProducts(): LiveData<List<FolderWithProducts>>
}