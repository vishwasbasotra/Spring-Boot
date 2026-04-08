package com.ecommerce.project.service;

import com.ecommerce.project.entity.Category;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService{
    //private List<Category> categoryList = new ArrayList<>();

    @Autowired
    private CategoryRepository categoryRepository;

    // Map: Entity -> DTO
    private CategoryDTO mapToDTO(Category category){

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());

        return categoryDTO;
    }

    // Map: DTO -> Entity
    private Category toEntity(CategoryDTO categoryDTO){

        Category category = new Category();

        if(categoryDTO.getCategoryName() != null){
            category.setCategoryName(categoryDTO.getCategoryName());
        }
        return category;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categoryList= categoryPage.getContent();

        if(categoryList.isEmpty())  throw new APIException("Category doesn't exist yet!");

        List<CategoryDTO> categoryDTOList = categoryList.stream()
                .map(this::mapToDTO)
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setContent(categoryDTOList);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO createNewCategory(CategoryDTO categoryDTO) {

        Category category = toEntity(categoryDTO);
        Category exsistingCategory = categoryRepository.findByCategoryName(category.getCategoryName());

        if(exsistingCategory != null)   throw new APIException("Category '"+category.getCategoryName()+"' already exist!");

        Category savedCategory = categoryRepository.save(category);

        return mapToDTO(savedCategory);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category foundCategory = optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        categoryRepository.delete(foundCategory);

        return mapToDTO(foundCategory);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

        // 1. Convert CategoryDTO to Category entity
        Category category = toEntity(categoryDTO);

        // 2. Directly find the category or throw the exception if empty
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        // 3. Update the fields
        existingCategory.setCategoryName(category.getCategoryName());

        // 4. Save the managed entity (JPA performs an UPDATE here)
        Category updatedCategory = categoryRepository.save(existingCategory);

        return mapToDTO(updatedCategory);
    }
}
