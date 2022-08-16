package com.vytivskyi.salesmanhelper.model.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class Product(
    @PrimaryKey(autoGenerate = true)
    val productId: Int,
    val title: String,
    val price: Int,
    val number: Int,
    val folderId: Int
    ): Parcelable