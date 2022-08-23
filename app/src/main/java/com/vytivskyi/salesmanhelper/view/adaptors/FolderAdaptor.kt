package com.vytivskyi.salesmanhelper.view.adaptors

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.DialogEditFolderBinding
import com.vytivskyi.salesmanhelper.databinding.RecyclerViewItemBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel

class FolderAdaptor(
    private val context: Context,
    private val folderViewModel: FolderViewModel,
) :
    RecyclerView.Adapter<FolderAdaptor.FolderViewHolder>() {

    var folder: List<Folder> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var product: List<Product> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mItemClickListener: (folder: Folder) -> Unit = {}
    var mFolderClickListener: (folder: Folder) -> Unit = {}

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RecyclerViewItemBinding.bind(itemView)

        fun bind(text: String?) = with(binding) {
            folderName.text = text
        }

        init {
            itemView.setOnClickListener {
                mItemClickListener(folder[adapterPosition])
                mFolderClickListener(folder[adapterPosition])

            }

            binding.folderMenu.setOnClickListener {
                popupMenu(it)
            }
        }

        private fun popupMenu(view: View?) {
            val popupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.custom_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Edit -> {
                        editFolder(folder[adapterPosition], folderViewModel)
                        true
                    }
                    R.id.Delete -> {
                        deleteFolder(folder[adapterPosition], folderViewModel)
                        deleteAllProductsFromFolder(folder[adapterPosition].folderId)
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

    private fun editFolder(folder: Folder, viewModel: FolderViewModel) {
        val dialogBuilder = AlertDialog.Builder(context)
        val bindingDialog = DialogEditFolderBinding.inflate(LayoutInflater.from(context))

        dialogBuilder.setView(bindingDialog.root)

        bindingDialog.apply {
            dialogBuilder.setTitle(R.string.change)
            dialogBuilder.setMessage(R.string.create_folder_message)
            bindingDialog.editFolderTitle.setText(folder.folderName)
            dialogBuilder.setPositiveButton(
                R.string.change,
                DialogInterface.OnClickListener { _, _ ->
                    if (editFolderTitle.text.isNotEmpty()) {
                        val newFolder =
                            Folder(folderId = folder.folderId, editFolderTitle.text.toString())
                        viewModel.updateFolder(newFolder)
                    } else {
                        Toast.makeText(
                            context,
                            R.string.create_folder_delete_toast,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            dialogBuilder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                })
            dialogBuilder.create().show()
        }

    }

    private fun deleteFolder(
        folder: Folder,
        folderViewModel: FolderViewModel,
    ) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle(R.string.delete)
        dialogBuilder.setMessage(R.string.dialog_folder_delete_message)
        dialogBuilder.setPositiveButton(
            R.string.delete,
            DialogInterface.OnClickListener { _, _ ->
                folderViewModel.deleteFolder(folder)

            })
        dialogBuilder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which ->
            })
        dialogBuilder.create().show()
    }

    private fun deleteAllProductsFromFolder(
        folderId: Int
    ) {

        product.forEach {
            if (it.folderId == folderId) {
                folderViewModel.deleteAppProducts(it)
            }
        }

    }
}



