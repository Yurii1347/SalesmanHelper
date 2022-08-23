package com.vytivskyi.salesmanhelper

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.vytivskyi.salesmanhelper.databinding.DialogAddFolderBinding
import com.vytivskyi.salesmanhelper.databinding.FragmentChooseFolderForProductBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.view.adaptors.FolderAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel

class ChooseFolderForProduct : Fragment() {

    private val args: ChooseFolderForProductArgs by navArgs()

    private lateinit var binding: FragmentChooseFolderForProductBinding

    private lateinit var folderViewModel: FolderViewModel
    private lateinit var mAdaptor: FolderAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChooseFolderForProductBinding.inflate(inflater)

        folderViewModel = FolderViewModel(this.requireActivity().application)
        mAdaptor = FolderAdaptor(requireContext(), folderViewModel)
        mAdaptor.mFolderClickListener =::onClickOnFolder

        observerFolders()

        binding.addFolder.setOnClickListener{
            addFolder()
        }

        return binding.root
    }

    private fun observerFolders() {
        folderViewModel.allFolders.observe(viewLifecycleOwner) { folders ->
            // Update the cached copy of the words in the adapter.
            folders.let {
                mAdaptor.folder = it
                binding.chooseFolderRecycler.adapter = mAdaptor
                binding.chooseFolderRecycler.layoutManager = GridLayoutManager(requireContext(), 1)
            }
        }
    }

    private fun onClickOnFolder(folder: Folder) {
        val action =
            ChooseFolderForProductDirections.actionChooseFolderForProductToAddFragment(folder.folderId, args.barcode)
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
                })
            dialogBuilder.create().show()
        }
    }

}