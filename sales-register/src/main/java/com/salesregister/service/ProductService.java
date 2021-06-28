package com.salesregister.service;

import com.salesregister.domain.Products;
import com.salesregister.domain.User;
import com.salesregister.repository.ProductsRepository;
import com.salesregister.request.ProductsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductsRepository productsRepository;
    private final UserService userService;

    @Autowired
    ProductService(ProductsRepository productsRepository, UserService userService) {
        this.productsRepository = productsRepository;
        this.userService = userService;
    }

    public List<Products> getProductsForCurrentUser() {
        return productsRepository.findByUser(userService.getCurrentUser());
    }

    public void addProduct(ProductsRequest request) {
        Products products = new Products();
        products.setId(null);
        products.setName(request.getName());
        products.setPrice(request.getPrice());
        products.setUser(userService.getCurrentUser());

        productsRepository.save(products);
    }

    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }
}
