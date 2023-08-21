package com.mlorenzo.spring5mongorecipeapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
