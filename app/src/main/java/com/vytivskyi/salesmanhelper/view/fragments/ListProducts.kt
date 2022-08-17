package com.vytivskyi.salesmanhelper.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vytivskyi.salesmanhelper.databinding.FragmentListBinding
import com.vytivskyi.salesmanhelper.view.adaptors.ProductsAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel


class ListProducts : Fragment() {
    private lateinit var binding: FragmentListBinding

    private lateinit var productsViewModel: ProductsViewModel

    private val productsAdaptor = ProductsAdaptor()
    private val args: ListProductsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(inflater)

        productsViewModel = ProductsViewModel(this.requireActivity().application, args.folderId)
        binding.productRcyclerView.adapter = productsAdaptor

        binding.floatingActionButtonAddProduct.setOnClickListener {
            Log.d("kek", "${args.folderId}")
            val action =
                ListProductsDirections.actionListFragmentToAddFragment(folderId = args.folderId)
            binding.root.findNavController().navigate(action)

        }
        initObserver()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action = ListProductsDirections.actionListOfProductsToListFoldersFragment()
                    findNavController().navigate(action)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun initObserver() {
        productsViewModel.allProductsFromFolder.observe(this@ListProducts as LifecycleOwner) {
            productsAdaptor.mainL = it.map { products ->
                products.products
            }.flatten()

            binding.productRcyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

}