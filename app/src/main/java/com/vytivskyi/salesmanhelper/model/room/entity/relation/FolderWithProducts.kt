package com.vytivskyi.salesmanhelper.model.room.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.model.room.entity.Product

data class FolderWithProducts(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "folderId"
    )
    val products: List<Product>
)