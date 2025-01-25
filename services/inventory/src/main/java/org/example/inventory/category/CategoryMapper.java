package org.example.inventory.category;

import org.example.inventory.product.Product;
import org.example.inventory.product.ProductDto;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.name())
                .description(categoryDto.description())
                .build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
