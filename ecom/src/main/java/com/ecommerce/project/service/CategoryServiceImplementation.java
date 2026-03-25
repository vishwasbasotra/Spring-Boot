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
    public void deleteCategory(String categoryId) {
        for(Category category: categoryList){
            if(category.getCategoryId().equals(categoryId)) categoryList.remove(category);
        }
    }

    @Override
    public void deleteCategory() {
        categoryList.removeLast();
    }
}
