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
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("product")
    public ProductResponse save(@Valid @RequestBody AddProduct product) {
        return productService.save(product);
    }

    //id is for seller and won't work now
    @GetMapping("product/{id}/page/{pageNumber}")
    public Page<ProductResponse> getProducts(@PathVariable Long id,
                                             @PathVariable int pageNumber
    ) {
        return productService.getProducts(id, pageNumber);
    }

    @PatchMapping("product/{productId}")
    public ProductResponse updateStatus(@PathVariable Long productId,
                                        @RequestParam String status
    ) {
        return productService.updateStatus(productId,   status);
    }
}
