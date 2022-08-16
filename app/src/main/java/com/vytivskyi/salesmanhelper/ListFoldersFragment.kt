package com.vytivskyi.salesmanhelper

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vytivskyi.salesmanhelper.databinding.DialogAddFolderBinding
import com.vytivskyi.salesmanhelper.databinding.FragmentListFoldersBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.view.adaptors.FolderAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel


class ListFoldersFragment : Fragment() {

    private lateinit var binding: FragmentListFoldersBinding

    private var mainMenu: Menu? = null
    private lateinit var folderViewModel: FolderViewModel
    lateinit var mAdaptor: FolderAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListFoldersBinding.inflate(inflater)

        folderViewModel = FolderViewModel(this.requireActivity().application)

        observerOfFolders()

        binding.addFolder.setOnClickListener {
            addFolder()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                mainMenu = menu
                menuInflater.inflate(R.menu.custom_menu, mainMenu)
                showDeleteMenu(false)
                showEditMenu(false)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.Delete -> deleteFolder()
                    R.id.Edit -> mAdaptor.mItemClickListener = ::editFolder
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observerOfFolders() {
        folderViewModel.allFolders.observe(viewLifecycleOwner) { folders ->
            // Update the cached copy of the words in the adapter.
            folders.let {
                mAdaptor = FolderAdaptor(
                    it,
                    { show -> showDeleteMenu(show) },
                    { show -> showEditMenu(show) })
                binding.recyclerView.adapter = mAdaptor
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            }
        }
    }

    private fun showDeleteMenu(show: Boolean) {
        mainMenu?.findItem(R.id.Delete)?.isVisible = show
    }

    private fun showEditMenu(show: Boolean) {
        mainMenu?.findItem(R.id.Edit)?.isVisible = show
    }

    private fun deleteFolder() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog
            .setTitle(R.string.delete)
            .setMessage(R.string.dialog_folder_delete_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                mAdaptor.deleteSelectedItem(folderViewModel)
                showDeleteMenu(false)
                showEditMenu(false)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                dialog.setCancelable(true)
            }.show()
    }

    private fun editFolder(folder: Folder) {
        val action =
            ListFoldersFragmentDirections.actionListFoldersFragmentToEditFolderFragment(folder)
        findNavController().navigate(action)
    }

    private fun addFolder() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val bindingDialog = DialogAddFolderBinding.inflate(layoutInflater)

        dialogBuilder.setView(bindingDialog.root)

        bindingDialog.apply {
            dialogBuilder.setTitle(R.string.create)
            dialogBuilder.setMessage(R.string.create_folder_message)
            dialogBuilder.setPositiveButton(
                R.string.create,
                DialogInterface.OnClickListener { _, _ ->
                    if (dialogFolderEdText.text.isNotEmpty()) {
                        val folder = Folder(0, dialogFolderEdText.text.toString())
                        folderViewModel.addFolder(folder)
                        showEditMenu(false)
                        showDeleteMenu(false)
//                        hideKeyboard()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            R.string.create_folder_delete_toast,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            dialogBuilder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog, which ->
//                    hideKeyboard()
                })
            dialogBuilder.create().show()
        }
    }

//    private fun hideKeyboard() {
//        val view = this@ListFoldersFragment.view.currentFocus
//        if (view != null) {
//            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }


}