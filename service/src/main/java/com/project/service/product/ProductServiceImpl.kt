package com.project.service.product

import android.content.Context
import com.project.models.product.Product
import com.project.models.product.ProductService

class ProductServiceImpl : ProductService{
    override fun getProductService(context: Context): List<Product> {
        val repository= ProductFactory.getRepository(context)
        return repository.getProduct(context)
    }
}
