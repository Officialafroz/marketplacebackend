package com.elevata.marketplace.catalogs;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CatalogService {
    Page<CatalogResponse> getCatalogs(int pageNumber);
}
