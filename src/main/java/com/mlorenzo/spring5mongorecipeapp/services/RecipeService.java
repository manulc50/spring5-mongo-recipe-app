package com.mlorenzo.spring5mongorecipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5mongorecipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;

public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(String id);
    RecipeCommand findCommandById(String id);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    void deleteById(String idToDelete);
}
