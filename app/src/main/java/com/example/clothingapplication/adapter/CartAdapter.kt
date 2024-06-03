package com.example.clothingapplication.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.clothingapplication.databinding.ItemCartListBinding
import com.example.clothingapplication.interfaces.OnCartItemClickInterface
import com.example.clothingapplication.model.ProductModel

class CartAdapter(private val onCartItemClickInterface: OnCartItemClickInterface): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdapter.CartViewHolder {
        val binding = ItemCartListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        val productItem = cartDiffer.currentList[position]

        holder.binding.apply {
            tvProductTitle.text = "${productItem.title}"
            tvProductPrice.text = "$${productItem.price}"

            Glide.with(holder.itemView.context)
                .load("${productItem.image}")
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).into(sivProductImage)

            linDelete.setOnClickListener {
                onCartItemClickInterface.onDeleteItemClicked(productItem)
            }
        }

        holder.itemView.setOnClickListener {
            onCartItemClickInterface.onItemClicked(productItem)
        }
    }

    override fun getItemCount(): Int {
        return cartDiffer.currentList.size
    }

    inner class CartViewHolder(val binding: ItemCartListBinding):
            RecyclerView.ViewHolder(binding.root){}

    private val differCallback = object : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }
    }

    val cartDiffer = AsyncListDiffer(this, differCallback)

}