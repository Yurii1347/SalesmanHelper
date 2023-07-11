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
import com.vytivskyi.salesmanhelper.databinding.FragmentAddBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel

class AddProduct : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var productsViewModel: ProductsViewModel

    private val args: AddProductArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater)

        productsViewModel =
            ProductsViewModel(this.requireActivity().application, folderId = args.folderId)

        if (args.barcode != null) {
            binding.addBarcode.text = args.barcode
        }

        binding.addProduct.setOnClickListener {
            insertProduct()
        }

        binding.imageView2.setOnClickListener {
            TODO()
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        AddProductDirections.actionAddFragmentToListFragment(args.folderId, null)
                    findNavController().navigate(action)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun insertProduct() {

        val name = binding.addName.text.toString()
        val price = binding.addPrice.editableText
        val number = binding.addNumber.editableText
        val barcode = binding.addBarcode.text.toString()

        if (inputCheck(name, price, number)) {
            val product = Product(
                0,
                name,
                Integer.parseInt(price.toString()),
                Integer.parseInt(number.toString()),
                args.folderId,
                barcode = barcode
            )
            productsViewModel.addProduct(product)
            Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
            val action =
                AddProductDirections.actionAddFragmentToListFragment(folderId = args.folderId, null)
            binding.root.findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(name: String, price: Editable, number: Editable): Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || number.isEmpty())
    }

}