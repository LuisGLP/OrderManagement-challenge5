package com.ordermanagement.orderapp.controller;

import com.ordermanagement.orderapp.entity.Product;
import com.ordermanagement.orderapp.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "CRUD for Products")
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a new product.
     *
     * @param product Product data
     * @return Created product with HTTP 201 status
     */
    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product in the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
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
    @Operation(summary = "Get all products", description = "Retrieves a list of all products or only active ones")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<Product>> getAllProducts(
            @Parameter(description = "Filter only active products")
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
    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
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
    @Operation(summary = "Search products by name", description = "Searches products containing the specified name")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Name to search for")
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
    @Operation(summary = "Update a product", description = "Updates an existing product's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
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
    @Operation(summary = "Delete a product", description = "Deletes a product from the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
