package com.ecommerce.project.service;

import com.ecommerce.project.entity.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories();
    void createNewCategory(CategoryDTO categoryDTO);
    String deleteCategory(Long categoryId);
    String updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
