package com.example.store.presentation.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.databinding.ItemProductBinding
import com.example.store.presentation.model.FullProduct

class ProductsAdapter(val onClickListener: OnClickListener) :
    ListAdapter<FullProduct, ProductsAdapter.ProductViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<FullProduct>() {
        override fun areItemsTheSame(oldItem: FullProduct, newItem: FullProduct): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FullProduct, newItem: FullProduct): Boolean {
            return oldItem.code == newItem.code
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(product)
        }
    }

    class ProductViewHolder(private var binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: FullProduct) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = binding.root.context.getString(R.string.price, product.price)
            binding.tvPromotion.text = product.discountLabel
        }
    }
    class OnClickListener(val clickListener: (product: FullProduct) -> Unit) {
        fun onClick(product: FullProduct) = clickListener(product)
    }
}
