package com.vytivskyi.salesmanhelper.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.FragmentListBinding
import com.vytivskyi.salesmanhelper.view.adaptors.ProductsAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel


class ListProducts : Fragment() {
    private lateinit var binding: FragmentListBinding

    private lateinit var productsViewModel: ProductsViewModel

    private var myMenu: Menu? = null
    private val productsAdaptor = ProductsAdaptor()
    private val args: ListProductsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(inflater)

        productsViewModel = ProductsViewModel(this.requireActivity().application, args.folderId)
        binding.productRcyclerView.adapter = productsAdaptor

        productsAdaptor.showMenuDelete = ::showMenuDelete
        binding.floatingActionButtonAddProduct.setOnClickListener {
            val action =
                ListProductsDirections.actionListFragmentToAddFragment(
                    folderId = args.folderId,
                    null
                )
            binding.root.findNavController().navigate(action)
        }
        binding.scanNewProduct.setOnClickListener {
            val action =
                ListProductsDirections.actionListOfProductsToAddProductWithCamera()
            binding.root.findNavController().navigate(action)
        }
        initObserver()

        return binding.root
    }

    private fun showMenuDelete(show: Boolean) {
        myMenu?.findItem(R.id.Delete)?.isVisible = show
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                myMenu = menu
                menuInflater.inflate(R.menu.custom_menu, myMenu)
                myMenu?.findItem(R.id.Delete)?.isVisible = false
                myMenu?.findItem(R.id.Edit)?.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when (menuItem.itemId) {
                    R.id.Delete -> {
                        deleteProducts()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action = ListProductsDirections.actionListOfProductsToListFolders2()
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
            productsAdaptor.productList = it.map { products ->
                products.products
            }.flatten()

            productsAdaptor.barcode = args.barcode

            binding.productRcyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun deleteProducts() {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle(R.string.delete)
        dialogBuilder.setMessage(R.string.dialog_product_delete_message)
        dialogBuilder.setPositiveButton(
            R.string.delete
        ) { _, _ ->
            productsAdaptor.deleteSelectedItem(productsViewModel)
            showMenuDelete(false)
        }
        dialogBuilder.setNegativeButton(
            "Cancel"
        ) { _, _ ->
            productsAdaptor.itemSelectedList.clear()
            showMenuDelete(false)
            productsAdaptor.notifyDataSetChanged()
        }
        dialogBuilder.create().show()
    }

}

