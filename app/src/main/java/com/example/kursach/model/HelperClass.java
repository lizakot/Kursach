package com.example.kursach.model;

import java.util.List;

public class HelperClass {

    String id, name, email, username, password;
    List<String> categoryIds;
    List<String> expenseIds;

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<String> getExpenseIds() {
        return expenseIds;
    }

    public void setExpenseIds(List<String> expenseIds) {
        this.expenseIds = expenseIds;
    }


    public void addCategoryId(String categoryId) {
        categoryIds.add(categoryId);
    }

    public void removeCategoryId(String categoryId) {
        categoryIds.remove(categoryId);
    }

    public void addExpenseId(String expenseId) {
        expenseIds.add(expenseId);
    }

    public void removeExpenseId(String expenseId) {
        expenseIds.remove(expenseId);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = password;
    }

    public HelperClass(List<String> categoryIds, String id, String name, String email, String username, String password,List<String> expenseIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.categoryIds = categoryIds;
        this.expenseIds = expenseIds;
    }

    public HelperClass() {
    }
}
