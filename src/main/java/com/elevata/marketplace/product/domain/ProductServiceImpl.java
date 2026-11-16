package com.elevata.marketplace.product.domain;

import com.elevata.marketplace.product.ProductResponse;
import com.elevata.marketplace.product.ProductService;
import com.elevata.marketplace.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class ProductServiceImpl implements ProductService {
    private static final int PRODUCT_PAGE_SIZE = 30;
    private final ProductRepository productRepository;
    private final CurrentUser currentUser;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CurrentUser currentUser) {
        this.productRepository = productRepository;
        this.currentUser = currentUser;
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponse> save(AddProduct product) {
        Long sellerId = currentUser.id();
        Product p = ProductMapper.toEntity(product, sellerId);
//        Why product status as null?
        p = productRepository.save(p);

        ProductResponse productResponse = ProductMapper.toDto(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @Override
    public ResponseEntity<Page<ProductResponse>> getProducts(int pageNumber) {
        Long sellerId = currentUser.id();
        Page<ProductResponse> productResponsePage = productRepository
                .getProducts(sellerId, PageRequest.of(pageNumber, PRODUCT_PAGE_SIZE));
        return ResponseEntity.ok(productResponsePage);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponse> updateStatus(Long productId, String status) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product for id-" + productId + " not found."));

        if (ProductStatus.UNAVAILABLE.name().equalsIgnoreCase(status)
                || ProductStatus.AVAILABLE.name().equalsIgnoreCase(status)
        ) {
            p.setProductStatus(ProductStatus.valueOf(status.toUpperCase()));
            productRepository.save(p);

            ProductResponse productResponse = ProductMapper.toDto(p);
            return ResponseEntity.ok(productResponse);
        }

        throw new InvalidProductStatusException("Invalid status");
    }

    @Override
    public Page<ProductResponse> getAvailableProducts(List<Long> availableProductIds,
                                                      int pageNumber,
                                                      int catalogPageSize
    ) {
        return productRepository.findAllProductsByIdUsingIn(availableProductIds,
                        PageRequest.of(pageNumber, catalogPageSize));
    }

    @Override
    public ProductResponse findById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for id " + productId));
        return ProductMapper.toDto(product);
    }
}
