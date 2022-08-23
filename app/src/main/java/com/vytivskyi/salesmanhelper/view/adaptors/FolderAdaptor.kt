package com.vytivskyi.salesmanhelper.view.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.RecyclerViewItemBinding
import com.vytivskyi.salesmanhelper.model.room.entity.relation.FolderWithProducts

class FolderAdaptor:
    RecyclerView.Adapter<FolderAdaptor.FolderViewHolder>() {

    var folders: List<FolderWithProducts> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mItemClickListener: (folder: FolderWithProducts) -> Unit = {}
    var mFolderClickListener: (folder: FolderWithProducts) -> Unit = {}
    var onOptionsPopup: (folder: FolderWithProducts, view: View) -> Unit = { _, _ ->}

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RecyclerViewItemBinding.bind(itemView)

        fun bind(text: String?) = with(binding) {
            folderName.text = text
        }

        init {
            itemView.setOnClickListener {
                mItemClickListener(folders[adapterPosition])
                mFolderClickListener(folders[adapterPosition])
            }

            binding.folderMenu.setOnClickListener {
                onOptionsPopup(folders[adapterPosition], it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val item = folders[position]
        holder.bind(item.folder.folderName)
    }

    override fun getItemCount() = folders.size

}



