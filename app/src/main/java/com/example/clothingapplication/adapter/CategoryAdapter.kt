package com.example.clothingapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.clothingapplication.databinding.ItemCategoryListBinding
import com.example.clothingapplication.interfaces.OnCategoryItemClickInterface
import com.example.clothingapplication.model.CategoryModel
import com.example.clothingapplication.utils.hide
import com.example.clothingapplication.utils.show

class CategoryAdapter(private val onCategoryItemClickInterface: OnCategoryItemClickInterface): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        val binding = ItemCategoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        val categoryItem = categoryDiffer.currentList[position]

        holder.binding.apply {
            if (categoryItem.isSelected){
                cvSelectedCategory.show()
                cvUnselectedCategory.hide()

                tvSelectedCategory.text = categoryItem.category
            } else {
                cvSelectedCategory.hide()
                cvUnselectedCategory.show()

                tvUnselectedCategory.text = categoryItem.category
            }
        }

        holder.itemView.setOnClickListener {
            onCategoryItemClickInterface.onItemClicked(categoryItem)
        }
    }

    override fun getItemCount(): Int {
        return categoryDiffer.currentList.size
    }

    inner class CategoryViewHolder(val binding: ItemCategoryListBinding):
            RecyclerView.ViewHolder(binding.root){}

    private val differCallback = object : DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem
        }
    }

    val categoryDiffer = AsyncListDiffer(this, differCallback)

}