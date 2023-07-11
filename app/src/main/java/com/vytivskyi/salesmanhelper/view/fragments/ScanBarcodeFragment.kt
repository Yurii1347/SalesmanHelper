package com.vytivskyi.salesmanhelper.view.fragments

import android.Manifest.permission.CAMERA
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.navigation.fragment.findNavController
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.FragmentScanBarcodeBinding
import com.vytivskyi.salesmanhelper.model.BarcodeAnalyzer
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


class ScanBarcodeFragment : androidx.fragment.app.Fragment() {

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private lateinit var binding: FragmentScanBarcodeBinding

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var folderViewModel: FolderViewModel
    private lateinit var barcode: String
    private var product: List<Product>? = null
    private var camera: Camera? = null

    private var processingBarcode = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.ivFlashlight.setOnClickListener {
            turnOnTheFlashlight()
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
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { result ->
                        for (i in product?.map { it.barcode }!!) {
                            if (i == result) {
                                barcode = result ?: " "
                                openProductWithBarcode(product)
                                break
                            } else barcode = result ?: " "
                        }
                        if (barcode == " " || barcode.isDigitsOnly()) {
                            cameraExecutor.shutdown()
                            openFoldersForAddingNewProduct()
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis).also {
                    camera = it
                }
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
                    findNavController().currentDestination?.id == R.id.ScanBarcodeFragment
        }
        if (product != null) {
            val action =
                ScanBarcodeFragmentDirections.actionScanBarcodeFragmentToListOfProducts(
                    product.folderId,
                    barcode
                )
            findNavController().navigate(action)
        }
    }

    private fun openFoldersForAddingNewProduct() {
        if (findNavController().currentDestination?.id == R.id.ScanBarcodeFragment) {
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            dialogBuilder
                .setTitle(R.string.create)
                .setMessage(R.string.dialog_create_new_product_from_scanner)
                .setPositiveButton(
                    R.string.create,
                    DialogInterface.OnClickListener { _, _ ->
                        if (findNavController().currentDestination?.id == R.id.ScanBarcodeFragment) {
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
                        if (findNavController().currentDestination?.id == R.id.ScanBarcodeFragment) {
                            val action =
                                ScanBarcodeFragmentDirections.actionBarcodeSacannerToListFoldersFragment()
                            findNavController().navigate(action)
                        }
                    })
            dialogBuilder.create().show()

        }
    }

    private fun turnOnTheFlashlight() {
        camera?.cameraControl?.enableTorch(true)
    }
}




