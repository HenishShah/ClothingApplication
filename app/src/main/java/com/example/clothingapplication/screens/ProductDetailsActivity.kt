package com.example.clothingapplication.screens

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.clothingapplication.databinding.ActivityProductDetailsBinding
import com.example.clothingapplication.model.ProductModel
import com.example.clothingapplication.model.RatingModel
import com.example.clothingapplication.roomdb.ProductDatabase
import com.example.clothingapplication.utils.DataState
import com.example.clothingapplication.utils.constants.IntentConst
import com.example.clothingapplication.utils.notifyUser
import com.example.clothingapplication.viewmodel.ProductDetailsViewModel

class ProductDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var viewModel: ProductDetailsViewModel

    private var productModel: ProductModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        setObservers()
    }

    private fun setObservers() {
        viewModel.insertDataState.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    notifyUser("Added To Cart")
                    finish()
                }

                is DataState.Error -> {
                    notifyUser(dataState.message)
                }

                is DataState.Loading -> {
                }
            }
        }
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]

        binding.apply {
            productModel = ProductModel(
                id = intent.getIntExtra(IntentConst.PRODUCT_MODEL_ID, 0),
                image = intent.getStringExtra(IntentConst.PRODUCT_MODEL_IMAGE) ?: "",
                price = intent.getDoubleExtra(IntentConst.PRODUCT_MODEL_PRICE, 0.0),
                title = intent.getStringExtra(IntentConst.PRODUCT_MODEL_TITLE) ?: "",
                category = intent.getStringExtra(IntentConst.PRODUCT_MODEL_CATEGORY) ?: "",
                description = intent.getStringExtra(IntentConst.PRODUCT_MODEL_DESCRIPTION) ?: "",
                rating = RatingModel(
                    rate = intent.getDoubleExtra(IntentConst.PRODUCT_MODEL_RATING_RATE, 0.0),
                    count = intent.getIntExtra(IntentConst.PRODUCT_MODEL_RATING_COUNT, 0),
                )
            )
            sivBackIcon.setOnClickListener {
                finish()
            }

            sivCartIcon.setOnClickListener {
                navToCartDetails()
            }

            productModel?.let {
                tvProductTitle.text = it.title
                tvProductPrice.text = "$${it.price}"
                tvDescription.text = it.description
                tvRatings.text = "${it.rating.rate} | ${it.rating.count}"

                Glide.with(this@ProductDetailsActivity)
                    .load("${it.image}")
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
                    }).into(sivImage)
            }

            val dao = ProductDatabase.invoke(application.applicationContext).getProductDao()

            btAddToCart.setOnClickListener {
                productModel?.let {
                    viewModel.setInsertDataStateEvent(dao, it)
                }
            }
        }
    }

    private fun navToCartDetails() {
        startActivity(Intent(this, CartDetailsActivity::class.java))
    }
}