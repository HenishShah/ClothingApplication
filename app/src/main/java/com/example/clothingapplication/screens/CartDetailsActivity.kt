package com.example.clothingapplication.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.clothingapplication.adapter.CartAdapter
import com.example.clothingapplication.databinding.ActivityCartDetailsBinding
import com.example.clothingapplication.interfaces.OnCartItemClickInterface
import com.example.clothingapplication.interfaces.OnProductItemClickInterface
import com.example.clothingapplication.model.ProductModel
import com.example.clothingapplication.roomdb.ProductDao
import com.example.clothingapplication.roomdb.ProductDatabase
import com.example.clothingapplication.utils.DataState
import com.example.clothingapplication.utils.constants.IntentConst
import com.example.clothingapplication.utils.notifyUser
import com.example.clothingapplication.viewmodel.CartViewModel
import com.example.clothingapplication.viewmodel.ProductViewModel

class CartDetailsActivity : AppCompatActivity(), OnCartItemClickInterface {

    private lateinit var binding: ActivityCartDetailsBinding
    private lateinit var viewModel: CartViewModel

    private lateinit var cartAdapter: CartAdapter

    private lateinit var dao: ProductDao

    private var productList: List<ProductModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        setObservers()
    }

    private fun initialize() {
        viewModel = ViewModelProviders.of(this)[CartViewModel::class.java]
        dao = ProductDatabase.invoke(application.applicationContext).getProductDao()
        cartAdapter = CartAdapter(this)

        binding.apply {
            sivBackIcon.setOnClickListener {
                finish()
            }

            rvCart.adapter = cartAdapter

            btCheckOut.setOnClickListener {
                notifyUser("Check Out Successfully")
                finish()
            }
        }
    }

    private fun setObservers() {
        viewModel.getCartListDataState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    productList = dataState.data
                    cartAdapter.cartDiffer.submitList(productList)

                    var cartPrice = 0.0

                    binding.apply {

                        if (productList.isEmpty()){
                            tvDeliveryPrice.text = "$0.0"
                            tvTotalPrice.text = "$0.0"
                        } else {
                            productList.forEach {
                                cartPrice += it.price
                            }

                            tvDeliveryPrice.text = "$5.0"
                            tvTotalPrice.text = "$${cartPrice + 5}"
                        }
                        tvCartPrice.text = "$$cartPrice"
                    }
                }

                is DataState.Error -> {
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {

                }
            }
        }

        viewModel.deleteDataState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    notifyUser(dataState.data)
                }

                is DataState.Error -> {
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {

                }
            }
        }
    }

    override fun onItemClicked(productModel: ProductModel) {
        navToProductDetailsScreen(productModel)
    }

    override fun onDeleteItemClicked(productModel: ProductModel) {
        viewModel.setDeleteDataStateEvent(dao, productModel)
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