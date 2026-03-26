package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService{
    private List<Category> categoryList = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        return categoryList;
    }

    @Override
    public void createNewCategory(Category category) {
        categoryList.add(category);
    }

    @Override
    public String deleteCategory(String categoryId) {
        Category category = categoryList.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!!!"));

        categoryList.remove(category);
        return "Deleted Category: '" + categoryId + "' Successfully!!!";
    }

    @Override
    public String updateCategory(String categoryId, String name) {
        Category category = categoryList.stream()
                            .filter(c -> c.getCategoryId()
                            .equals(categoryId)).findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found!!!"));
        category.setCategoryName(name);
        return "Category '"+categoryId+"' updated successfully";
    }
}
