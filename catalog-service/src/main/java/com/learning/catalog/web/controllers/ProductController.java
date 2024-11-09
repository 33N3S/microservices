package com.learning.catalog.web.controllers;

import com.learning.catalog.domain.PagedResults;
import com.learning.catalog.domain.Product;
import com.learning.catalog.domain.ProductNotFoundException;
import com.learning.catalog.domain.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:8082/**")
class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    PagedResults<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNum) {
        return productService.getProducts(pageNum);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        return productService
                .getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
