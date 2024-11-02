package com.learning.catalog.domain;

import com.learning.catalog.ApplicationProperties;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final ApplicationProperties properties;

    public ProductService(ApplicationProperties applicationProperties) {
        this.properties = applicationProperties;
    }

    public PagedResults<Product> getProducts(int pageNum) {
        Sort sort = Sort.by("name").ascending();
        pageNum = pageNum <= 1 ? 0 : pageNum - 1;
        Pageable pageable = PageRequest.of(pageNum, properties.pageSize(), sort);
        Page<Product> productpage = productRepository.findAll(pageable).map(ProductMapper::toProduct);
        return new PagedResults<>(
                productpage.getContent(),
                productpage.getTotalElements(),
                productpage.getNumber() + 1,
                productpage.getTotalPages(),
                productpage.isFirst(),
                productpage.isLast(),
                productpage.hasNext(),
                productpage.hasPrevious());
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code).map(ProductMapper::toProduct);
    }
}
