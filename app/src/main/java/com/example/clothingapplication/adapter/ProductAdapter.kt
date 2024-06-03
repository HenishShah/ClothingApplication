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
import com.example.clothingapplication.databinding.ItemProductListBinding
import com.example.clothingapplication.interfaces.OnProductItemClickInterface
import com.example.clothingapplication.model.ProductModel

class ProductAdapter(private val onProductItemClickInterface: OnProductItemClickInterface): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {
        val binding = ItemProductListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        val productItem = productDiffer.currentList[position]

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
        }

        holder.itemView.setOnClickListener {
            onProductItemClickInterface.onItemClicked(productItem)
        }
    }

    override fun getItemCount(): Int {
        return productDiffer.currentList.size
    }

    inner class ProductViewHolder(val binding: ItemProductListBinding):
            RecyclerView.ViewHolder(binding.root){}

    private val differCallback = object : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }
    }

    val productDiffer = AsyncListDiffer(this, differCallback)

}