package com.vytivskyi.salesmanhelper.view.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vytivskyi.salesmanhelper.Constances
import com.vytivskyi.salesmanhelper.databinding.FragmentListBinding
import com.vytivskyi.salesmanhelper.view.adaptors.ProductsAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel
import kotlin.properties.Delegates


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding

    private lateinit var mProductsViewModel: ProductsViewModel
    private var folderId by Delegates.notNull<Int>()

    private val productsAdaptor = ProductsAdaptor()
    private val args: ListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        folderId = try {
            args.folderId
        }
        catch (e: Exception) {
            arguments?.getInt(Constances.FOLDER_NAME_KEY) ?: 0
        }

        binding = FragmentListBinding.inflate(inflater)

        mProductsViewModel = ProductsViewModel(this.requireActivity().application, 0)
        binding.productRcyclerView.adapter = productsAdaptor

        binding.floatingActionButtonAddProduct.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToAddFragment(folderId = 0)
            binding.root.findNavController().navigate(action)
            Log.d("lol", "${arguments?.getString(Constances.FOLDER_NAME_KEY)}")
        }
        initObserver()
        return binding.root
    }

    private fun initObserver(){
        mProductsViewModel.allProductsFromFolder.observe(this@ListFragment as LifecycleOwner) {
            productsAdaptor.mainL = it.map { products ->
                products.products
            }.flatten()

            binding.productRcyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

}