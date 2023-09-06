package com.mlorenzo.spring5mongorecipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5mongorecipeapp.commands.CategoryCommand;

public interface CategoryService {
	Set<CategoryCommand> getCategories();
	CategoryCommand getCategoryById(String id);
}
