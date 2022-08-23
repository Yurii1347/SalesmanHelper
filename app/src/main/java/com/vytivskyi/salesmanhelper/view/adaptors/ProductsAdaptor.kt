package com.vytivskyi.salesmanhelper.view.adaptors

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.RecyclerViewProductsBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.view.fragments.ListProductsDirections
import com.vytivskyi.salesmanhelper.viewmodel.ProductsViewModel

class ProductsAdaptor : RecyclerView.Adapter<ProductsAdaptor.ViewHolder>() {
    var productList: List<Product> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var barcode: String? = null

    var showMenuDelete: (Boolean) -> Unit = {}

    private var isEnable = false
    val itemSelectedList: MutableSet<Int> = mutableSetOf()

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = RecyclerViewProductsBinding.bind(item)

        fun bind(products: Product, position: Int) = with(binding) {
            recyclerProductsPosition.text = position.plus(1).toString()
            recyclerProductsTitle.text = products.title
            recyclerProductsPrice.text = products.price.toString()
            recyclerProductsNumber.text = products.number.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productList[position]
        holder.binding.checkMark.visibility = View.GONE

        holder.bind(productList[position], position)

        if (itemSelectedList.isEmpty()) {
            holder.binding.recyclerProductsPosition.visibility = View.VISIBLE
            isEnable = false
        }

        holder.binding.productLayout.setOnLongClickListener() {
            selectItem(holder, item, position)
            true
        }

        holder.binding.productLayout.setOnClickListener {
            if (itemSelectedList.contains(position)) {
                itemSelectedList.remove(position)
                holder.binding.checkMark.visibility = View.GONE
                holder.binding.recyclerProductsPosition.visibility = View.VISIBLE
                item.selector = false
                if (itemSelectedList.isEmpty()) {
                    showMenuDelete(false)
                    isEnable = false
                }
            } else if (isEnable) {
                selectItem(holder, item, position)
            } else {
                val action =
                    ListProductsDirections.actionListFragmentToUpdateFragment(productList[position])
                holder.itemView.findNavController().navigate(action)
            }
        }

        barcode?.let {
            if (item.barcode == it) {
                holder.binding.recyclerProductsTitle.setBackgroundColor(Color.GREEN)
            }
            else holder.binding.recyclerProductsTitle.setBackgroundColor(Color.WHITE)
        }
    }

    private fun selectItem(holder: ProductsAdaptor.ViewHolder, item: Product, position: Int) {
        isEnable = true
        itemSelectedList.add(position)
        item.selector = true
        holder.binding.recyclerProductsPosition.visibility = View.GONE
        holder.binding.checkMark.visibility = View.VISIBLE
        showMenuDelete(true)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun deleteSelectedItem(productsViewModel: ProductsViewModel) {
        if (itemSelectedList.isNotEmpty()) {
            for (i in itemSelectedList) {
                productsViewModel.deleteProduct(productList[i])
            }
            isEnable = false
            itemSelectedList.clear()
        }
        notifyDataSetChanged()
    }
}