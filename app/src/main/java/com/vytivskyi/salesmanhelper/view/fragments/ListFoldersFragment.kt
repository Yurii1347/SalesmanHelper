package com.vytivskyi.salesmanhelper.view.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.DialogAddFolderBinding
import com.vytivskyi.salesmanhelper.databinding.DialogEditFolderBinding
import com.vytivskyi.salesmanhelper.databinding.FragmentListFoldersBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.model.room.entity.relation.FolderWithProducts
import com.vytivskyi.salesmanhelper.view.adaptors.FolderAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel


class ListFoldersFragment : Fragment() {

    private lateinit var binding: FragmentListFoldersBinding

    private lateinit var folderViewModel: FolderViewModel
    private lateinit var mAdaptor: FolderAdaptor

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListFoldersBinding.inflate(inflater)

        folderViewModel = FolderViewModel(this.requireActivity().application)

        mAdaptor = FolderAdaptor()
        mAdaptor.mItemClickListener = ::onClickFolder
        mAdaptor.onOptionsPopup = ::showPopupMenu

        observerOfFolders()

        binding.btAddFolder.setOnClickListener {
            addFolder()
        }

        binding.imageView2.setOnClickListener {

            val action = ListFoldersFragmentDirections.actionListFoldersFragmentToBarcodeSacanner()
            findNavController().navigate(action)

        }

        binding.searchByWrite.setOnClickListener {
            val action =
                ListFoldersFragmentDirections.actionListFoldersToSearchAllProductsFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun observerOfFolders() {
        folderViewModel.allFolders.observe(viewLifecycleOwner) { folders ->
            folders.let {
                mAdaptor.folders = it
                binding.recyclerView.adapter = mAdaptor
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.moveTaskToBack(true)
                    activity?.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun addFolder() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val bindingDialog = DialogAddFolderBinding.inflate(layoutInflater)

        dialogBuilder.setView(bindingDialog.root)

        bindingDialog.apply {
            dialogBuilder.setTitle(R.string.create)
            dialogBuilder.setMessage(R.string.hint_create_folder_message)
            dialogBuilder.setPositiveButton(
                R.string.create
            ) { _, _ ->
                if (dialogFolderEdText.text.isNotEmpty()) {
                    val folder = Folder(0, dialogFolderEdText.text.toString())
                    folderViewModel.addFolder(folder)
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.hint_create_folder_delete_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dialogBuilder.setNegativeButton(
                "Cancel"
            ) { dialog, which ->
            }
            dialogBuilder.create().show()
        }
    }

    private fun onClickFolder(folder: FolderWithProducts) {
        val action =
            ListFoldersFragmentDirections.actionListFoldersFragmentToListFragment(
                folder.folder.folderId,
                null
            )
        findNavController().navigate(action)
    }

    private fun deleteFolder(
        folder: FolderWithProducts
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle(R.string.delete)
        dialogBuilder.setMessage(R.string.dialog_folder_delete_message)
        dialogBuilder.setPositiveButton(
            R.string.delete
        ) { _, _ ->
            folderViewModel.deleteFolder(folder)
        }
        dialogBuilder.setNegativeButton(
            "Cancel"
        ) { _, _ -> }
        dialogBuilder.create().show()
    }

    private fun editFolder(folder: FolderWithProducts) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val bindingDialog = DialogEditFolderBinding.inflate(LayoutInflater.from(context))

        dialogBuilder.setView(bindingDialog.root)

        bindingDialog.apply {
            dialogBuilder.setTitle(R.string.change)
            dialogBuilder.setMessage(R.string.hint_create_folder_message)
            bindingDialog.editFolderTitle.setText(folder.folder.folderName)
            dialogBuilder.setPositiveButton(
                R.string.change
            ) { _, _ ->
                if (editFolderTitle.text.isNotEmpty()) {
                    val newFolder =
                        Folder(folderId = folder.folder.folderId, editFolderTitle.text.toString())
                    folderViewModel.updateFolder(newFolder)
                } else {
                    Toast.makeText(
                        context,
                        R.string.hint_create_folder_delete_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            dialogBuilder.setNegativeButton(
                "Cancel"
            ) { _, _ ->
            }
            dialogBuilder.create().show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showPopupMenu(folder: FolderWithProducts, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.custom_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.Edit -> {
                    editFolder(folder)
                    true
                }
                R.id.Delete -> {
                    deleteFolder(folder)
                    true
                }
                else -> true
            }
        }
        popupMenu.setForceShowIcon(true)
        popupMenu.show()
    }
}