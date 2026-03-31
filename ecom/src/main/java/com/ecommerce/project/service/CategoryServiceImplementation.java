package com.ecommerce.project.service;

import com.ecommerce.project.entity.Category;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService{
    //private List<Category> categoryList = new ArrayList<>();

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createNewCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categoryList = getAllCategories();

        Category category = categoryList.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!!!"));

        categoryRepository.delete(category);
        return "Deleted Category: '" + categoryId + "' Successfully!!!";
    }

    @Override
    public String updateCategory(Category category, Long categoryId) {
        List<Category> categoryList = getAllCategories();

        Optional<Category> optionalCategory = categoryList.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();

        if(optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(existingCategory);
            return "Category '"+categoryId+"' updated successfully";
        }else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found!!!");
    }
}
