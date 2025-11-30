package com.ordermanagement.orderapp.service;

import com.ordermanagement.orderapp.entity.Product;
import com.ordermanagement.orderapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Creates a new product.
     *
     * @param product Product entity to create
     * @return Created product
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Retrieves all products.
     *
     * @return List of all products
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves only active products.
     *
     * @return List of active products
     */
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    /**
     * Retrieves a product by ID.
     *
     * @param id Product ID
     * @return Product entity
     * @throws IllegalArgumentException if product not found
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    /**
     * Updates an existing product.
     *
     * @param id             Product ID
     * @param productDetails Updated product details
     * @return Updated product
     * @throws IllegalArgumentException if product not found
     */
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setIsActive(productDetails.getIsActive());

        return productRepository.save(product);
    }

    /**
     * Deletes a product by ID.
     *
     * @param id Product ID
     * @throws IllegalArgumentException if product not found
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Searches products by name.
     *
     * @param name Name to search for
     * @return List of matching products
     */
    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
