package org.example.inventory.product;

import org.example.inventory.category.Category;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product toProduct(ProductDto productDto) {
        return Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .availableQuantity(productDto.availableQuantity())
                .price(productDto.price())
                .category(Category.builder()
                        .id(productDto.categoryId())
                        .build()
                )
                .build();
    }

    public ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .availableQuantity(product.getAvailableQuantity())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .build();
    }
}
