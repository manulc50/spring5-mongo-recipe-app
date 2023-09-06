package com.mlorenzo.spring5mongorecipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5mongorecipeapp.commands.IngredientCommand;

public interface IngredientService {
	Set<IngredientCommand> getIngredients(String recipeId);
	IngredientCommand createNewIngredient(String recipeId);
    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    void deleteById(String recipeId, String idToDelete);
}
