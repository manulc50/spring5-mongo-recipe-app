package com.mlorenzo.spring5mongorecipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5mongorecipeapp.commands.RecipeCommand;

public interface RecipeService {
    Set<RecipeCommand> getRecipes();
    RecipeCommand findCommandById(String id);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    void deleteById(String idToDelete);
}
