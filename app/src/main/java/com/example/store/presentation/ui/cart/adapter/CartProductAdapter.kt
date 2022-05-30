package com.example.store.presentation.ui.cart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.local.model.CartProductEntity
import com.example.store.databinding.ItemCartProductBinding

class CartProductAdapter : ListAdapter<CartProductEntity, CartProductAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartProductBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    private class DiffCallback : DiffUtil.ItemCallback<CartProductEntity>() {

        override fun areItemsTheSame(oldItem: CartProductEntity, newItem: CartProductEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartProductEntity, newItem: CartProductEntity) =
            oldItem == newItem
    }

    class ViewHolder(private var binding: ItemCartProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: CartProductEntity) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = binding.root.context.getString(R.string.price, product.price)
            if (product.discountedPrice >= 0) {
                binding.tvProductPrice.alpha = 0.5f
                binding.tvDiscountedPrice.visibility = View.VISIBLE
                binding.tvDiscountedPrice.text = binding.root.context.getString(R.string.price, product.discountedPrice)
            } else {
                binding.tvDiscountedPrice.visibility = View.GONE
            }
        }
    }
}
