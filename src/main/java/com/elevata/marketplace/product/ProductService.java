package com.elevata.marketplace.product;

import com.elevata.marketplace.product.domain.AddProduct;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    ProductResponse save(AddProduct product);

    Page<ProductResponse> getProducts(Long id, int pageNumber);

    ProductResponse updateStatus(Long productId, String status);

    Page<ProductResponse> getAvailableProducts(List<Long> availableProductIds,
                                               int pageNumber,
                                               int catalogPageSize);
}
