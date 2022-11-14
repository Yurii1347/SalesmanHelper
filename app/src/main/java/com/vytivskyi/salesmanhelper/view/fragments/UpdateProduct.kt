package com.vytivskyi.salesmanhelper.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vytivskyi.salesmanhelper.databinding.FragmentUpdateBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel

class UpdateProduct : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var productsViewModel: ProductsViewModel

    private val args: UpdateProductArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater)

        productsViewModel =
            ProductsViewModel(this.requireActivity().application, args.product.folderId)

        binding.apply {
            updateTitle.setText(args.product.title)
            updatePrice.setText(args.product.price.toString())
            updateNumber.setText(args.product.number.toString())
            updateBarcode.setText(args.product.barcode)

            updateProduct.setOnClickListener {
                updateProduct()
            }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action = UpdateProductDirections.actionUpdateFragmentToListFragment(
                        args.product.folderId,
                        null
                    )
                    findNavController().navigate(action)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun updateProduct() = with(binding) {
        val title = updateTitle.text
        val price = updatePrice.text
        val number = updateNumber.text
        val barcode = updateBarcode.text

        if (inputCheck(title, price, number)) {
            val updatedProduct = Product(
                productId = args.product.productId,
                title = title.toString(),
                price = price.toString().toInt(),
                number = number.toString().toInt(),
                folderId = args.product.folderId,
                barcode = barcode.toString(),
            )
            productsViewModel.updateProduct(updatedProduct)

            val action =
                UpdateProductDirections.actionUpdateFragmentToListFragment(
                    folderId = args.product.folderId,
                    null,
                )
            binding.root.findNavController().navigate(action)

        } else {
            Toast.makeText(requireContext(), "Wrong data", Toast.LENGTH_SHORT).show()
        }


    }

    private fun inputCheck(name: Editable, price: Editable, number: Editable): Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || number.isEmpty())
    }
}