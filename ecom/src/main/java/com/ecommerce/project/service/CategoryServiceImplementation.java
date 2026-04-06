package com.ecommerce.project.service;

import com.ecommerce.project.entity.Category;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService{
    //private List<Category> categoryList = new ArrayList<>();

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categoryList= categoryRepository.findAll();
        if(categoryList.isEmpty())  throw new APIException("Category doesn't exist yet!");
        return categoryList;
    }

    @Override
    public void createNewCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null)   throw new APIException("Category '"+category.getCategoryName()+"' already exist!");
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        Category foundCategory = optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        categoryRepository.delete(foundCategory);
        return "Deleted Category: '" + categoryId + "' Successfully!!!";
    }

    @Override
    public String updateCategory(Category category, Long categoryId) {

        // 1. Directly find the category or throw the exception if empty
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        // 2. Update the fields
        existingCategory.setCategoryName(category.getCategoryName());

        // 3. Save the managed entity (JPA performs an UPDATE here)
        categoryRepository.save(existingCategory);

        return "Category '"+categoryId+"' updated successfully";
    }
}
