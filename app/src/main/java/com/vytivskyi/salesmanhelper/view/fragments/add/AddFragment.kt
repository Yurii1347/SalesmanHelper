package com.vytivskyi.salesmanhelper.view.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.FragmentAddBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.view.fragments.list.ListFragmentDirections
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel
import kotlin.properties.Delegates


class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var mProductsViewModel: ProductsViewModel

    private var folderId by Delegates.notNull<Int>()

    private val args: AddFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater)
        folderId = args.folderId

        mProductsViewModel = ProductsViewModel(this.requireActivity().application, folderId = args.folderId)
        binding.addProduct.setOnClickListener{
            insertProduct()
        }

        return binding.root
    }

    private fun insertProduct() {
        val name = binding.addName.text.toString()
        val price = binding.addPrice.text
        val number = binding.addNumber.text

        if (inputCheck(name, price, number)) {
            val product = Product(0, name, Integer.parseInt(price.toString()), Integer.parseInt(number.toString()), 0)
            mProductsViewModel.addProduct(product)
            Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
            val action = AddFragmentDirections.actionAddFragmentToListFragment(folderId = folderId )
            binding.root.findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

        }
    }

    private fun inputCheck(name: String, price: Editable, number: Editable):Boolean{
            return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(price) && number.isEmpty())
    }

}