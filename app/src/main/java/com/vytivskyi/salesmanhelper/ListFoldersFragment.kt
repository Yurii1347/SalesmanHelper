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
import androidx.recyclerview.widget.GridLayoutManager
import com.vytivskyi.salesmanhelper.databinding.DialogAddFolderBinding
import com.vytivskyi.salesmanhelper.databinding.FragmentListFoldersBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.view.adaptors.FolderAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel


class ListFoldersFragment : Fragment() {

    private lateinit var binding: FragmentListFoldersBinding

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
    }

    private fun observerOfFolders() {
        folderViewModel.allFolders.observe(viewLifecycleOwner) { folders ->
            // Update the cached copy of the words in the adapter.
            folders.let {
                mAdaptor = FolderAdaptor(requireContext(), folderViewModel )
                mAdaptor.folder = it
                binding.recyclerView.adapter = mAdaptor
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
            }
        }
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



}