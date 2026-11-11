package com.elevata.marketplace.product.web.controller;

import com.elevata.marketplace.product.ProductResponse;
import com.elevata.marketplace.product.ProductService;
import com.elevata.marketplace.product.domain.AddProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seller")
class ProductController {

    private final ProductService productService;

    @Autowired
    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("product")
    ProductResponse save(@Valid @RequestBody AddProduct product) {
        return productService.save(product);
    }

    //id is for seller and won't work now
    @GetMapping("product/page/{pageNumber}")
    Page<ProductResponse> getProducts(@PathVariable int pageNumber) {
        return productService.getProducts(pageNumber);
    }

    @PatchMapping("product/{productId}")
    ProductResponse updateStatus(@PathVariable Long productId,
                                 @RequestParam String status
    ) {
        return productService.updateStatus(productId,   status);
    }
}
