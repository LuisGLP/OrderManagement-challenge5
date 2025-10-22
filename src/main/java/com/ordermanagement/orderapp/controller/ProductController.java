package com.ordermanagement.orderapp.controller;

import com.ordermanagement.orderapp.entity.Product;
import com.ordermanagement.orderapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a new product.
     *
     * @param product Product data
     * @return Created product with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Retrieves all products.
     *
     * @param activeOnly Optional parameter to filter only active products
     * @return List of products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        List<Product> products = activeOnly ?
                productService.getActiveProducts() :
                productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieves a product by ID.
     *
     * @param id Product ID
     * @return Product data
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Searches products by name.
     *
     * @param name Name to search for
     * @return List of matching products
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    /**
     * Updates an existing product.
     *
     * @param id Product ID
     * @param product Updated product data
     * @return Updated product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @Valid @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Deletes a product.
     *
     * @param id Product ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
