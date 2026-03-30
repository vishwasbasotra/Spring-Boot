package com.ecommerce.project.model;


public class Category {
    private static long categoryCount = 1;
    private Long categoryId;
    private String categoryName;

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryId = categoryCount++;
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
