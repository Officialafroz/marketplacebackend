package com.elevata.marketplace.catalogs.web.controller;

import com.elevata.marketplace.catalogs.CatalogResponse;
import com.elevata.marketplace.catalogs.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/customer/catalogs")
public class CatalogController {

    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("page/{pageNumber}")
    public Page<CatalogResponse> getCatalogs(@PathVariable int pageNumber) {
        return catalogService.getCatalogs(pageNumber);
    }
}
