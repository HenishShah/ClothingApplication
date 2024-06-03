package com.example.clothingapplication.screens

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.example.clothingapplication.adapter.CategoryAdapter
import com.example.clothingapplication.adapter.ProductAdapter
import com.example.clothingapplication.databinding.ActivityMainBinding
import com.example.clothingapplication.interfaces.OnCategoryItemClickInterface
import com.example.clothingapplication.interfaces.OnProductItemClickInterface
import com.example.clothingapplication.model.CategoryModel
import com.example.clothingapplication.model.ProductModel
import com.example.clothingapplication.utils.DataState
import com.example.clothingapplication.utils.constants.IntentConst
import com.example.clothingapplication.utils.disable
import com.example.clothingapplication.utils.enable
import com.example.clothingapplication.utils.hide
import com.example.clothingapplication.utils.notifyUser
import com.example.clothingapplication.utils.show
import com.example.clothingapplication.viewmodel.ProductViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : BaseActivity(), OnProductItemClickInterface, OnCategoryItemClickInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ProductViewModel

    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private var productList: List<ProductModel> = ArrayList()
    private var filteredProductList: List<ProductModel> = ArrayList()

    private var categoryList: List<CategoryModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        setObservers()
    }

    private fun initialize() {
        viewModel = ViewModelProviders.of(this)[ProductViewModel::class.java]

        productAdapter = ProductAdapter(this)
        categoryAdapter = CategoryAdapter(this)

        binding.apply {

            rvItems.adapter = productAdapter
            rvCategories.adapter = categoryAdapter

            sivCartIcon.setOnClickListener {
                navToCartDetails()
            }

            var job: Job? = null
            etSearchProduct.addTextChangedListener {
                job?.cancel()
                job = MainScope().launch {
                    delay(1000L)
                    it.toString().trim().let { text ->
                        filteredProductList = if (text.isEmpty() || text == "") {
                            productList
                        } else {
                            productList.filter { productModel ->
                                val lowerText = text.lowercase(Locale.ROOT)
                                productModel.title.lowercase(Locale.ROOT)
                                    .contains(lowerText) ||
                                        productModel.price.toString().contains(lowerText) ||
                                        productModel.category.lowercase(Locale.ROOT)
                                            .contains(lowerText) ||
                                        productModel.description.lowercase(Locale.ROOT)
                                            .contains(lowerText)

                            }
                        }

                        productAdapter.productDiffer.submitList(filteredProductList)
                    }
                }
            }
        }
    }

    private fun setObservers() {

        viewModel.dataState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    hideProgressBar()
                    productList = dataState.data
                    productAdapter.productDiffer.submitList(productList)
                }

                is DataState.Error -> {
                    hideProgressBar()
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {
                    showProgressBar()
                }
            }
        }

        viewModel.dataStateCategory.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    hideProgressBar()
                    categoryList = dataState.data
                    categoryAdapter.categoryDiffer.submitList(categoryList)
                }

                is DataState.Error -> {
                    hideProgressBar()
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.apply {
            pbProduct.show()

            rvItems.hide()
            rvCategories.hide()

            etSearchProduct.disable()
        }
    }

    private fun hideProgressBar() {
        binding.apply {
            pbProduct.hide()

            rvItems.show()
            rvCategories.show()

            etSearchProduct.enable()
        }
    }

    override fun onItemClicked(categoryModel: CategoryModel) {
        categoryList.forEach {
            if (it.category == categoryModel.category)
                it.isSelected = true
            else
                it.isSelected = false
        }
        categoryAdapter.notifyDataSetChanged()

        productAdapter.productDiffer.submitList(
            if (categoryModel.category == "All") {
                productList
            } else {
                filteredProductList = productList.filter {
                    it.category == categoryModel.category
                }
                filteredProductList
            }
        )
    }

    override fun onItemClicked(productModel: ProductModel) {
        navToProductDetailsScreen(productModel)
    }

    private fun navToCartDetails() {
        startActivity(Intent(this, CartDetailsActivity::class.java))
    }

    private fun navToProductDetailsScreen(productModel: ProductModel) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(IntentConst.PRODUCT_MODEL_ID, productModel.id)
        intent.putExtra(IntentConst.PRODUCT_MODEL_IMAGE, productModel.image)
        intent.putExtra(IntentConst.PRODUCT_MODEL_PRICE, productModel.price)
        intent.putExtra(IntentConst.PRODUCT_MODEL_TITLE, productModel.title)
        intent.putExtra(IntentConst.PRODUCT_MODEL_CATEGORY, productModel.category)
        intent.putExtra(IntentConst.PRODUCT_MODEL_DESCRIPTION, productModel.description)
        intent.putExtra(IntentConst.PRODUCT_MODEL_RATING_RATE, productModel.rating.rate)
        intent.putExtra(IntentConst.PRODUCT_MODEL_RATING_COUNT, productModel.rating.count)
        startActivity(intent)
    }
}