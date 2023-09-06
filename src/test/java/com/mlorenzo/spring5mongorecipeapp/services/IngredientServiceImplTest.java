package com.mlorenzo.spring5mongorecipeapp.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mlorenzo.spring5mongorecipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5mongorecipeapp.converters.IngredientCommandToIngredient;
import com.mlorenzo.spring5mongorecipeapp.converters.IngredientToIngredientCommand;
import com.mlorenzo.spring5mongorecipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.mlorenzo.spring5mongorecipeapp.repositories.RecipeRepository;
import com.mlorenzo.spring5mongorecipeapp.domain.Ingredient;
import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;
    
    @Mock
    IngredientToIngredientCommand ingrToIngrCommandConverter;
	
	@Mock
    IngredientCommandToIngredient ingrCommandToIngrConverter;
	
	@Mock
    UnitOfMeasureCommandToUnitOfMeasure uomCommandToUomConverter;

    IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(recipeRepository, ingrToIngrCommandConverter, ingrCommandToIngrConverter, uomCommandToUomConverter);
    }

    @Test
    public void findByRecipeIdAndReceipeIdHappyPath() throws Exception {
        //given
    	Recipe recipe = new Recipe();
        recipe.setId("1");
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");;
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2");
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");
        recipe.getIngredients().add(ingredient1);
        recipe.getIngredients().add(ingredient2);
        recipe.getIngredients().add(ingredient3);
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(ingredient3.getId());
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));
        when(ingrToIngrCommandConverter.convert(any(Ingredient.class))).thenReturn(ingredientCommand);
        //when
        IngredientCommand ingredientCommandReturned = ingredientService.findByRecipeIdAndIngredientId("1", "3");
        //then
        assertEquals("3", ingredientCommandReturned.getId());
        assertEquals("1", ingredientCommandReturned.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(ingrToIngrCommandConverter).convert(any(Ingredient.class));
    }

    @Test
    public void testSaveRecipeCommand() throws Exception {
        //given
    	IngredientCommand command = new IngredientCommand();
        command.setId("3");
        command.setRecipeId("2");
        Recipe savedRecipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(command.getId());
        savedRecipe.getIngredients().add(ingredient);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(new Recipe()));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);
        when(ingrCommandToIngrConverter.convert(any(IngredientCommand.class))).thenReturn(ingredient);
        when(ingrToIngrCommandConverter.convert(any(Ingredient.class))).thenReturn(command);
        //when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        //then
        assertEquals("3", savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void testDeleteById() throws Exception {
        //given
    	String idToDelete = "2";
        String idRecipe = "3";
        Recipe recipe = new Recipe();
        recipe.setId(idRecipe);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(idToDelete);
        recipe.getIngredients().add(ingredient);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));
        //when
        ingredientService.deleteById(idRecipe, idToDelete);
        //then
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
    }
}