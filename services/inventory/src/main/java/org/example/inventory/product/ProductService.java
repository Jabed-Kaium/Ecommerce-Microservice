package org.example.inventory.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.inventory.category.Category;
import org.example.inventory.category.CategoryRepository;
import org.example.inventory.exception.CategoryNotFoundException;
import org.example.inventory.exception.ProductNotFoundException;
import org.example.inventory.exception.ProductPurchaseException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductDto createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + productDto.categoryId()));    

        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .price(productDto.price())
                .availableQuantity(productDto.availableQuantity())
                .category(category)
                .createdAt(LocalDateTime.now().toString())
                .updatedAt(LocalDateTime.now().toString())
                .build();

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductDto(savedProduct);
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductDto)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(productDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + productDto.categoryId()));

        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setAvailableQuantity(productDto.availableQuantity());
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now().toString());

        return productMapper.toProductDto(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Transactional(rollbackFor = ProductPurchaseException.class)
    public void purchaseProducts(List<ProductPurchaseRequest> productPurchaseRequests) {
        List<Long> productIds = productPurchaseRequests
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        List<Product> products = productRepository.findAllByIdInOrderById(productIds);

        if(products.size() != productIds.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }

        List<ProductPurchaseRequest> sortedRequest = productPurchaseRequests
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            ProductPurchaseRequest request = sortedRequest.get(i);
            if(product.getAvailableQuantity() < request.quantity()) {
                throw new ProductPurchaseException("Not enough quantity available for product: " + product.getName());
            }
            product.setAvailableQuantity(product.getAvailableQuantity() - request.quantity());
            productRepository.save(product);
        }
    }
}
