package com.vytivskyi.salesmanhelper.view.fragments

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.DialogAddFolderBinding
import com.vytivskyi.salesmanhelper.databinding.FragmentListFoldersBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Folder
import com.vytivskyi.salesmanhelper.view.adaptors.FolderAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel


class ListFolders : Fragment() {

    private lateinit var binding: FragmentListFoldersBinding

    private lateinit var folderViewModel: FolderViewModel
    private lateinit var mAdaptor: FolderAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListFoldersBinding.inflate(inflater)

        folderViewModel = FolderViewModel(this.requireActivity().application)
        mAdaptor = FolderAdaptor(requireContext(), folderViewModel)
        mAdaptor.mItemClickListener = ::onClickFolder

        observerOfFolders()

        binding.btAddFolder.setOnClickListener {
            addFolder()
        }

        binding.imageView2.setOnClickListener {

            val action = ListFoldersDirections.actionListFoldersFragmentToBarcodeSacanner()
            findNavController().navigate(action)

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun observerOfFolders() {
        folderViewModel.allFolders.observe(viewLifecycleOwner) { folders ->
            // Update the cached copy of the words in the adapter.
            folders.let {

                mAdaptor.folder = it
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

    private fun onClickFolder(folder: Folder) {
        val action =
            ListFoldersDirections.actionListFoldersFragmentToListFragment(folder.folderId)
        findNavController().navigate(action)
    }

    @androidx.camera.core.ExperimentalGetImage
    private class YourImageAnalyzer : ImageAnalysis.Analyzer {

        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // Pass image to an ML Kit Vision API
                // ...
            }
        }
    }

}