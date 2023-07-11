package com.vytivskyi.salesmanhelper.view.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.FragmentSearchAllProductsBinding
import com.vytivskyi.salesmanhelper.view.adaptors.ProductsAdaptor
import com.vytivskyi.salesmanhelper.viewmodel.FolderViewModel
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel

class SearchAllProductsFragment : Fragment() {
    private lateinit var binding: FragmentSearchAllProductsBinding
    private lateinit var folderViewModel: FolderViewModel
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var allProducts: List<String?>

    private val productsAdaptor = ProductsAdaptor()
    private var myMenu: Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchAllProductsBinding.inflate(inflater)

        folderViewModel = FolderViewModel(this.requireActivity().application)
        productsViewModel = ProductsViewModel(this.requireActivity().application, 0)
        binding.allProductsRecycler.adapter = productsAdaptor

        productsAdaptor.showMenuDelete = ::showMenuDelete
        productsAdaptor.searchedElement = ::searchedElement

        initObserver()

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }
        })
        return binding.root
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

    private fun initObserver() {
        folderViewModel.allProducts.observe(this@SearchAllProductsFragment as LifecycleOwner) {
            productsAdaptor.productList = it
            allProducts = it.map { it.title }
        }
        binding.allProductsRecycler.layoutManager = LinearLayoutManager(context)
    }

    private fun showMenuDelete(show: Boolean) {
        myMenu?.findItem(R.id.Delete)?.isVisible = show
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

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        folderViewModel.searchDatabase(searchQuery)
            .observe(this@SearchAllProductsFragment as LifecycleOwner) {
                it.let {
                    productsAdaptor.productList = it
                }
            }
    }

    private fun searchedElement(position: Int) {
        binding.allProductsRecycler.scrollToPosition(position)
    }

}

