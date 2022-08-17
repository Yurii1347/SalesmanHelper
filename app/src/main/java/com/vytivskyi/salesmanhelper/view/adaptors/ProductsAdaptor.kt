package com.vytivskyi.salesmanhelper.view.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vytivskyi.salesmanhelper.R
import com.vytivskyi.salesmanhelper.databinding.RecyclerViewProductsBinding
import com.vytivskyi.salesmanhelper.model.room.entity.Product
import com.vytivskyi.salesmanhelper.view.fragments.ListProductsDirections

class ProductsAdaptor : RecyclerView.Adapter<ProductsAdaptor.ViewHolder>() {
    var mainL: List<Product> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = RecyclerViewProductsBinding.bind(item)

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
        holder.bind(mainL[position], position)

        holder.itemView.setOnClickListener {
            val action = ListProductsDirections.actionListFragmentToUpdateFragment(mainL[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return mainL.size
    }
}