package com.mlorenzo.spring5mongorecipeapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.mlorenzo.spring5mongorecipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5mongorecipeapp.converters.RecipeCommandToRecipe;
import com.mlorenzo.spring5mongorecipeapp.converters.RecipeToRecipeCommand;
import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;
import com.mlorenzo.spring5mongorecipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5mongorecipeapp.repositories.RecipeRepository;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    @Override
	public Set<RecipeCommand> getRecipes() {
		log.debug("I'm in the Recipe Service");
		Set<RecipeCommand> recipeCommands = new HashSet<RecipeCommand>();
		recipeRepository.findAll().forEach(recipe -> recipeCommands.add(recipeToRecipeCommand.convert(recipe)));
		return recipeCommands;
	}

    @Override
    public RecipeCommand findCommandById(String id) {
    	Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + id));
		return recipeToRecipeCommand.convert(recipe);
    }

    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public void deleteById(String idToDelete) {
        recipeRepository.deleteById(idToDelete);
    }
}
