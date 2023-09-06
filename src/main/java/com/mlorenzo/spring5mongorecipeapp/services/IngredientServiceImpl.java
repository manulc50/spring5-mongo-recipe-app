package com.mlorenzo.spring5mongorecipeapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.mlorenzo.spring5mongorecipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import com.mlorenzo.spring5mongorecipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5mongorecipeapp.converters.IngredientCommandToIngredient;
import com.mlorenzo.spring5mongorecipeapp.converters.IngredientToIngredientCommand;
import com.mlorenzo.spring5mongorecipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.mlorenzo.spring5mongorecipeapp.domain.Ingredient;
import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;
import com.mlorenzo.spring5mongorecipeapp.repositories.RecipeRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {
	private final RecipeRepository recipeRepository;
	private final IngredientToIngredientCommand ingrToIngrCommandConverter;
	private final IngredientCommandToIngredient ingrCommandToIngrConverter;
	private final UnitOfMeasureCommandToUnitOfMeasure uomCommandToUomConverter;
	
	@Override
	public Set<IngredientCommand> getIngredients(String recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
		return recipe.getIngredients().stream()
				// Versión simplificada de la expresión "ingredient -> ingrToIngrCommandConverter.convert(ingredient)"
				.map(ingrToIngrCommandConverter::convert)
				.collect(Collectors.toSet());
	}

	@Override
	public IngredientCommand createNewIngredient(String recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipe.getId());
        ingredientCommand.setId(UUID.randomUUID().toString());
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        return ingredientCommand;
	}

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
    	Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst();
        if(!ingredientOptional.isPresent()) {
            log.error("Ingredient id not found: " + ingredientId);
            throw new NotFoundException("Ingredient Not Found for id value: " + ingredientId);
        }
        IngredientCommand ingredientCommand = ingrToIngrCommandConverter.convert(ingredientOptional.get());
        ingredientCommand.setRecipeId(recipeId);
        return ingredientCommand;
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
    	Recipe recipe = recipeRepository.findById(command.getRecipeId())
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + command.getRecipeId()));
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();
        //update existing ingredient
        if(ingredientOptional.isPresent()){
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            ingredientFound.setUom(uomCommandToUomConverter.convert(command.getUom()));
        }
        //add new ingredient
        else
        	recipe.getIngredients().add(ingrCommandToIngrConverter.convert(command));
        Recipe savedRecipe = recipeRepository.save(recipe);
        // case of creating a new ingredient and updating an ingredient
        Ingredient savedIngredient = savedRecipe.getIngredients().stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                .findFirst().get();
        IngredientCommand ingredientCommand = ingrToIngrCommandConverter.convert(savedIngredient);
        ingredientCommand.setRecipeId(savedRecipe.getId());
        return ingredientCommand;
    }

    @Override
    public void deleteById(String recipeId, String idToDelete) {
    	Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
        Optional<Ingredient> removedIngredientOptional = recipe.getIngredients().stream()
        		.filter(ingredient -> ingredient.getId().equals(idToDelete))
        		.findFirst();
        if (removedIngredientOptional.isPresent()){
        	Ingredient removedIngredient = removedIngredientOptional.get();
        	recipe.getIngredients().remove(removedIngredient);
        	recipeRepository.save(recipe);
        }
    }

}
