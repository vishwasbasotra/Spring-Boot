package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

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
    Category category = categoryList.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst().orElse(null);
    if(category == null)    return "Category '"+categoryId+"' not found!!!";
    return "Deleted Category: '"+categoryId+"' Successfully!!!";
    }
}
