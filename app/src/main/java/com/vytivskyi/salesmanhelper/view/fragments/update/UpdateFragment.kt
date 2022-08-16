package com.vytivskyi.salesmanhelper.view.fragments.update

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.vytivskyi.salesmanhelper.databinding.FragmentUpdateBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var mProductsViewModel: ProductsViewModel

    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater)

        mProductsViewModel =
            ProductsViewModel(this.requireActivity().application, args.product.folderId)

        binding.apply {
            updateTitle.setText(args.product.title)
            updatePrice.setText(args.product.price.toString())
            updateNumber.setText(args.product.number.toString())

            updateProduct.setOnClickListener {
                updateProduct()
            }
        }

        return binding.root
    }

    private fun updateProduct() = with(binding) {
        val title = updateTitle.text
        val price = updatePrice.text
        val number = updateNumber.text

        if (inputCheck(title, price, number)) {
            val updatedProduct = Product(
                productId = args.product.productId,
                title = title.toString(),
                price = price.toString().toInt(),
                number = number.toString().toInt(),
                folderId = args.product.folderId
            )
            mProductsViewModel.updateProduct(updatedProduct)

            val action =
                UpdateFragmentDirections.actionUpdateFragmentToListFragment(folderId = args.product.folderId)
            binding.root.findNavController().navigate(action)

        } else {
            Toast.makeText(requireContext(), "Wrong data", Toast.LENGTH_SHORT).show()
        }


    }

    private fun inputCheck(name: Editable, price: Editable, number: Editable): Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(price) && number.isEmpty())
    }
}