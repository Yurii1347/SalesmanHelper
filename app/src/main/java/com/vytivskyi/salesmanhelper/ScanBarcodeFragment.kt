package com.vytivskyi.salesmanhelper

import android.Manifest.permission.CAMERA
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.common.Barcode
import com.vytivskyi.salesmanhelper.databinding.FragmentScanBarcodeBinding
import com.vytivskyi.salesmanhelper.model.BarcodeAnalyzer
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

typealias BarcodeListener = (barcode: String?) -> Unit

class ScanBarcodeFragment : Fragment() {

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private lateinit var binding: FragmentScanBarcodeBinding

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var folderViewModel: FolderViewModel
    private lateinit var barcode: String
    private var product: List<Product>? = null

    private var processingBarcode = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBarcodeBinding.inflate(inflater)

        folderViewModel = FolderViewModel(requireActivity().application)
        folderViewModel.allProducts.observe(viewLifecycleOwner) {

            this.product = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (allPermissionsGranted()) {

            startCamera()

        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onResume() {
        super.onResume()
        processingBarcode.set(false)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(
                    binding.fragmentScanBarcodePreviewView.surfaceProvider
                )
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { result ->
                        if (processingBarcode.compareAndSet(false, true)) {
                            barcode = result ?: " "
                            openProductWithBarcode(product)
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                Log.e("PreviewUseCase", "Binding failed! :(", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        cameraExecutor.shutdown()
        super.onDestroy()
    }

    private fun openProductWithBarcode(products: List<Product>?) {

        val product = products?.firstOrNull {
            it.barcode == barcode &&
                    findNavController().currentDestination?.id == R.id.barcodeSacanner

        }
        Log.d("tag", "$product")
        if (product != null) {
            val action =
                ScanBarcodeFragmentDirections.actionBarcodeSacannerToListOfProducts(
                    product.folderId,
                    barcode
                )
            findNavController().navigate(action)
        } else {
            openFoldersForAddingNewProduct()
        }
    }

    private fun openFoldersForAddingNewProduct() {
        if (findNavController().currentDestination?.id == R.id.barcodeSacanner) {
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            dialogBuilder
                .setTitle(R.string.create)
                .setMessage(R.string.dialog_create_new_product_from_scanner)
                .setPositiveButton(
                    R.string.create,
                    DialogInterface.OnClickListener { _, _ ->
                        if (findNavController().currentDestination?.id == R.id.barcodeSacanner) {
                            val action =
                                ScanBarcodeFragmentDirections.actionBarcodeSacannerToChooseFolderForProduct(
                                    barcode
                                )
                            findNavController().navigate(action)
                        }
                    })
                .setNegativeButton(

                    "Cancel",
                    DialogInterface.OnClickListener { dialog, which ->
                        if (findNavController().currentDestination?.id == R.id.barcodeSacanner) {
                            val action =
                                ScanBarcodeFragmentDirections.actionBarcodeSacannerToListFoldersFragment()
                            findNavController().navigate(action)
                        }
                    })
            dialogBuilder.create().show()

        }
    }
}




