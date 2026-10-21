package com.elevata.marketplace.product.domain;

import com.elevata.marketplace.product.ProductResponse;
import com.elevata.marketplace.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static final int PRODUCT_PAGE_SIZE = 30;
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse save(AddProduct product) {
        Product p = new Product(product.productName(), product.category());
//        Why product status as null?
        p = productRepository.save(p);

        return new ProductResponse(p.getProductId(),
                p.getProductName(),
                p.getCategory(),
                p.getProductStatus() == null ?
                        ProductStatus.UNAVAILABLE : ProductStatus.AVAILABLE);
    }

    @Override
    public Page<ProductResponse> getProducts(Long id, int pageNumber) {
        return productRepository.getProducts(id, PageRequest.of(pageNumber,
                PRODUCT_PAGE_SIZE));
    }

    @Override
    public ProductResponse updateStatus(Long productId, String status) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product for id-" + productId + " not found."));

        if (ProductStatus.UNAVAILABLE.name().equalsIgnoreCase(status)
                || ProductStatus.AVAILABLE.name().equalsIgnoreCase(status)
        ) {
            p.setProductStatus(ProductStatus.valueOf(status.toUpperCase()));
            productRepository.save(p);

            return new ProductResponse(p.getProductId(),
                    p.getProductName(),
                    p.getCategory(),
                    p.getProductStatus());
        }

        throw new InvalidProductStatusException("Invalid status");
    }

    @Override
    public Page<ProductResponse> getAvailableProducts(List<Long> availableProductIds,
                                                      int pageNumber,
                                                      int catalogPageSize
    ) {
        return productRepository
                .findAllProductsByIdUsingIn(availableProductIds,
                        PageRequest.of(pageNumber, catalogPageSize));
    }
}
