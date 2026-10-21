package com.elevata.marketplace.product;

import com.elevata.marketplace.product.domain.AddProduct;
import com.elevata.marketplace.product.domain.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {

    ProductResponse save(AddProduct product);

    Page<ProductResponse> getProducts(Long id, int pageNumber);

    ProductResponse updateStatus(Long productId, String status);
}
