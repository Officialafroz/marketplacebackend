package com.elevata.marketplace.product;

import com.elevata.marketplace.product.domain.AddProduct;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    ResponseEntity<ProductResponse> save(AddProduct product);

    ResponseEntity<Page<ProductResponse>> getProducts(int pageNumber);

    ResponseEntity<ProductResponse> updateStatus(Long productId, String status);

    Page<ProductResponse> getAvailableProducts(List<Long> availableProductIds,
                                               int pageNumber,
                                               int catalogPageSize);

    ProductResponse findById(Long productId);
}
