package com.vytivskyi.salesmanhelper.model.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class Folder(
    @PrimaryKey(autoGenerate = true)
    val folderId: Int,
    val folderName: String,
    var selector: Boolean = false
): Parcelable
