package com.vytivskyi.salesmanhelper.view.adaptors

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.RecyclerViewItemBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel

class FolderAdaptor(
    private val folder: MutableList<Folder>,
    private val showMenuDelete: (Boolean) -> Unit,
    private val showMenuEdit: (Boolean) -> Unit,
) :
    RecyclerView.Adapter<FolderAdaptor.FolderViewHolder>() {

  var mItemClickListener: (folder: Folder) -> Unit = {}

    private var isEnable = false
    private val itemSelectedList = mutableListOf<Int>()

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerViewItemBinding.bind(itemView)

        fun bind(text: String?) = with(binding) {
            recyclerViewNameFolder.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val item = folder[position]
        holder.binding.checkMark.visibility = View.GONE
        holder.bind(item.folderName)

        holder.binding.imageView.setOnLongClickListener() {
            showMenuEdit(true)
            selectItem(holder, item, position)
            true
        }
        holder.binding.imageView.setOnClickListener {
            showMenuEdit(false)
            if (itemSelectedList.contains(position)) {
                itemSelectedList.remove(position)
                holder.binding.checkMark.visibility = View.GONE
                item.selector = false
                if (itemSelectedList.isEmpty()) {
                    showMenuDelete(false)
                    showMenuEdit(false)
                    isEnable = false
                }
                if (itemSelectedList.size == 1) {
                    showMenuEdit(true)
                    mItemClickListener(Folder(item.folderId, item.folderName, item.selector))
                }
            } else if (isEnable) {
                selectItem(holder, item, position)
            } else {
                mItemClickListener(Folder(item.folderId, item.folderName, item.selector))
            }
        }
    }

    private fun selectItem(holder: FolderAdaptor.FolderViewHolder, item: Folder, position: Int) {
        isEnable = true
        itemSelectedList.add(position)
        item.selector = true
        holder.binding.checkMark.visibility = View.VISIBLE
        showMenuDelete(true)
    }

    override fun getItemCount() = folder.size

    fun deleteSelectedItem(folderViewModel: FolderViewModel) {
        if (itemSelectedList.isNotEmpty()) {
            for (i in itemSelectedList.indices) {
                folderViewModel.deleteFolder(folder[itemSelectedList[i]])
            }
            isEnable = false
            itemSelectedList.clear()
        }
        notifyDataSetChanged()
    }

//    private fun setFolderName() {
//        if (itemSelectedList.isNotEmpty()) {
//            val folder = folder[itemSelectedList.first()]
//            mSetFolder(folder)
//            isEnable = false
//            itemSelectedList.clear()
//        }
//    }

}
