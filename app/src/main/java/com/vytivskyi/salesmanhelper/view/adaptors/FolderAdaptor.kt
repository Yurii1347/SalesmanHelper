package com.vytivskyi.salesmanhelper.view.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import androidx.navigation.navArgument
import androidx.recyclerview.widget.RecyclerView
import com.vytivskyi.salesmanhelper.ListFoldersFragmentDirections
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.RecyclerViewItemBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel

class FolderAdaptor(private val context: Context, private val mViewModel: FolderViewModel):
    RecyclerView.Adapter<FolderAdaptor.FolderViewHolder>() {

    var folder: List<Folder> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var mItemClickListener: (folder: Folder) -> Unit = {}

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RecyclerViewItemBinding.bind(itemView)

        fun bind(text: String?) = with(binding) {
            folderName.text = text
        }

        init {
            binding.folderMenu.setOnClickListener {
                popupMenu(it)
            }
        }

        private fun popupMenu(view: View?) {
            val popupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.custom_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
//                    R.id.Edit -> {}
                    R.id.Delete -> {
                        Toast.makeText(context, "lol", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }
            }

            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val item = folder[position]
        holder.bind(item.folderName)

    }

    override fun getItemCount() = folder.size

    private fun editFolder(folder: Folder) {
        val alertDialog = AlertDialog.Builder(context)

    }

}


