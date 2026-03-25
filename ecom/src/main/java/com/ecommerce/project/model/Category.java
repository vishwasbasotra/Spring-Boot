package com.ecommerce.project.model;


public class Category {
    private static long categoryCount = 1;
    private String categoryId;
    private String categoryName;

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryId = "Category-"+categoryCount++;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
