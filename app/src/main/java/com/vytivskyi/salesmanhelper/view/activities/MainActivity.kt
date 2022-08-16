package com.vytivskyi.salesmanhelper.view.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.vytivskyi.salesmanhelper.Constances.FOLDER_NAME_KEY
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.ActivityMainBinding
import com.vytivskyi.salesmanhelper.databinding.DialogAddFolderBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.view.adaptors.FolderAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//
//    private var mainMenu: Menu? = null
//
//    private lateinit var folderViewModel: FolderViewModel
//    lateinit var mAdaptor: FolderAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



//        addMenuProvider(object: MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.custom_menu, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                return true
//            }
//
//        })
//
//        folderViewModel = FolderViewModel(this.application)
//
////        folderViewModel.allFolders.observe(this) { folders ->
////            // Update the cached copy of the words in the adapter.
////            folders.let {
////                mAdaptor = FolderAdaptor(
////                    it, this@MainActivity,
////                    { show -> showDeleteMenu(show) },
////                    { show -> showEditMenu(show) })
////                binding.recyclerView.adapter = mAdaptor
////                binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
////            }
////        }
//
////        binding.addFolder.setOnClickListener {
////            addFolder()
////        }
//
//
//    }
//
////    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////        mainMenu = menu
////        menuInflater.inflate(R.menu.custom_menu, mainMenu)
////        showDeleteMenu(false)
////        showEditMenu(false)
////        return super.onCreateOptionsMenu(menu)
////    }
////
////    private fun showDeleteMenu(show: Boolean) {
////        mainMenu?.findItem(R.id.Delete)?.isVisible = show
////    }
////
////    private fun showEditMenu(show: Boolean) {
////        mainMenu?.findItem(R.id.Edit)?.isVisible = show
////    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.Delete -> {
//                deleteFolder()
//            }
//            R.id.Edit -> {
//                mAdaptor.mSetFolderName = ::updateFolder
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//
////    override fun onClick(folderName: String) {
////        val i = Intent(this, ProductsActivity::class.java)
////        i.putExtra(FOLDER_NAME_KEY, folderName)
////        startActivity(i)
////    }
//
////    override fun onPause() {
////        super.onPause()
////        showDeleteMenu(false)
////        showEditMenu(false)
////    }
//
//    private fun hideKeyboard() {
//        val view = this.currentFocus
//        if (view != null) {
//            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }
//
//    private fun deleteFolder() {
//        val dialog = AlertDialog.Builder(this)
//        dialog
//            .setTitle(R.string.delete)
//            .setMessage(R.string.dialog_folder_delete_message)
//            .setPositiveButton(R.string.ok) { _, _ ->
//                mAdaptor.deleteSelectedItem(folderViewModel)
//                showDeleteMenu(false)
//                showEditMenu(false)
//            }
//            .setNegativeButton(R.string.cancel) { _, _ ->
//                onBackPressedDispatcher
//            }.show()
//    }
//
//
//
//    private fun addFolder() {
//        val dialogBuilder = AlertDialog.Builder(this@MainActivity)
//        val bindingDialog = DialogAddFolderBinding.inflate(layoutInflater)
//
//        dialogBuilder.setView(bindingDialog.root)
//
//        bindingDialog.apply {
//            dialogBuilder.setTitle(R.string.create)
//            dialogBuilder.setMessage(R.string.create_folder_message)
//            dialogBuilder.setPositiveButton(
//                R.string.create,
//                DialogInterface.OnClickListener { _, _ ->
//                    if (dialogFolderEdText.text.isNotEmpty()) {
//                        val folder = Folder(dialogFolderEdText.text.toString())
//                        folderViewModel.addFolder(folder)
//                        showEditMenu(false)
//                        showDeleteMenu(false)
//                        hideKeyboard()
//                    } else {
//                        Toast.makeText(
//                            this@MainActivity,
//                            R.string.create_folder_delete_toast,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//            dialogBuilder.setNegativeButton(
//                "Cancel",
//                DialogInterface.OnClickListener { dialog, which ->
//                    hideKeyboard()
//                })
//            dialogBuilder.create().show()
//        }
//    }
//
//     private fun updateFolder(folder: Folder) {
//        val dialogBuilder = AlertDialog.Builder(this@MainActivity)
//        val bindingDialog = DialogAddFolderBinding.inflate(layoutInflater)
//
//        dialogBuilder.setView(bindingDialog.root)
//
//         bindingDialog.dialogFolderEdText.setText(folder.folderName)
//
//        bindingDialog.apply {
//            dialogBuilder.setTitle(R.string.create)
//            dialogBuilder.setMessage(R.string.create_folder_message)
//            dialogBuilder.setPositiveButton(
//                R.string.create,
//                DialogInterface.OnClickListener { _, _ ->
//                    if (dialogFolderEdText.text.isNotEmpty()) {
//                        val newFolder: Folder = Folder(dialogFolderEdText.text.toString())
//                        folderViewModel.updateFolder(newFolder)
//                        showEditMenu(false)
//                        showDeleteMenu(false)
//                        hideKeyboard()
//                    } else {
//                        Toast.makeText(
//                            this@MainActivity,
//                            R.string.create_folder_delete_toast,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//            dialogBuilder.setNegativeButton(
//                "Cancel",
//                DialogInterface.OnClickListener { dialog, which ->
//                    hideKeyboard()
//                })
//            dialogBuilder.create().show()
//        }
//    }
    }
}