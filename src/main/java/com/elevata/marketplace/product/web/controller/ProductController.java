package com.elevata.marketplace.product.web.controller;

import com.elevata.marketplace.product.ProductResponse;
import com.elevata.marketplace.product.ProductService;
import com.elevata.marketplace.product.domain.AddProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/seller")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("product")
    ResponseEntity<ProductResponse> save(@Valid @RequestBody AddProduct product) {
        return productService.save(product);
    }

    //id is for seller and won't work now
    @GetMapping("product/page/{pageNumber}")
    ResponseEntity<Page<ProductResponse>> getProducts(@PathVariable int pageNumber) {
        return productService.getProducts(pageNumber);
    }

    // Marked for removal
    @PatchMapping("product/{productId}")
    ResponseEntity<ProductResponse> updateStatus(@PathVariable Long productId,
                                 @RequestParam String status
    ) {
        return productService.updateStatus(productId,   status);
    }
}
