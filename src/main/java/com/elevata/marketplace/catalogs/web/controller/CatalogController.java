package com.elevata.marketplace.catalogs.web.controller;

import com.elevata.marketplace.catalogs.CatalogResponse;
import com.elevata.marketplace.catalogs.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer/catalogs")
class CatalogController {

    private final CatalogService catalogService;

    @Autowired
    CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("page/{pageNumber}")
    Page<CatalogResponse> getCatalogs(@PathVariable int pageNumber) {
        return catalogService.getCatalogs(pageNumber);
    }

    @GetMapping("search/product-name/page/{pageNumber}")
    Page<CatalogResponse> search(@RequestParam String productName,
                                            @PathVariable int pageNumber) {
        return catalogService.search(productName, pageNumber);
    }
}
