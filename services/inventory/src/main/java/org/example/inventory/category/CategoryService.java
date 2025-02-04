package org.example.inventory.category;

import lombok.RequiredArgsConstructor;
import org.example.inventory.exception.CategoryNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @CacheEvict(value = "categoryCache", allEntries = true)
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);

        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Cacheable(value = "categoryCache")
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "categoryCache", key = "#id")
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryDto)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @CacheEvict(value = "categoryCache", allEntries = true)
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        category.setName(categoryDto.name());
        category.setDescription(categoryDto.description());

        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @CacheEvict(value = "categoryCache", allEntries = true)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }
}
