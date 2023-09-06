package com.mlorenzo.spring5mongorecipeapp.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mlorenzo.spring5mongorecipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5mongorecipeapp.converters.RecipeCommandToRecipe;
import com.mlorenzo.spring5mongorecipeapp.converters.RecipeToRecipeCommand;
import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;
import com.mlorenzo.spring5mongorecipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5mongorecipeapp.repositories.RecipeRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void getRecipeCommandByIdTest() throws Exception {
    	Recipe recipeReturned = new Recipe();
        recipeReturned.setId("1");
        RecipeCommand commandReturned = new RecipeCommand();
        commandReturned.setId(recipeReturned.getId());
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipeReturned));
        when(recipeToRecipeCommand.convert(any(Recipe.class))).thenReturn(commandReturned);
        RecipeCommand recipeCommand = recipeService.findCommandById("1");
        assertNotNull("Null recipe returned", recipeCommand);
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
        verify(recipeToRecipeCommand).convert(any(Recipe.class));
    }

    @Test(expected = NotFoundException.class)
    public void getRecipeCommandByIdTestNotFound() throws Exception {
        Optional<Recipe> recipeOptional = Optional.empty();
        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        //should go boom
        recipeService.findCommandById("1");
    }

    @Test
    public void getRecipesTest() throws Exception {
    	Recipe recipe1 = new Recipe();
		recipe1.setId("1");
		Recipe recipe2 = new Recipe();
		recipe2.setId("2");
		RecipeCommand command1 = new RecipeCommand();
		command1.setId(recipe1.getId());
		RecipeCommand command2 = new RecipeCommand();
		command2.setId(recipe2.getId());
		Set<Recipe> returnedRecipesSet = new HashSet<Recipe>();
		returnedRecipesSet.add(recipe1);
		returnedRecipesSet.add(recipe2);
		when(recipeRepository.findAll()).thenReturn(returnedRecipesSet);
		when(recipeToRecipeCommand.convert(any(Recipe.class))).thenReturn(command1).thenReturn(command2);
		Set<RecipeCommand> recipeCommands = recipeService.getRecipes();
		assertEquals(2, recipeCommands.size());
		// Si no se indica el número de llamadas en el método "times", por defecto es 1
		verify(recipeRepository, times(1)).findAll();
		verify(recipeToRecipeCommand, times(2)).convert(any(Recipe.class));
    }

    @Test
    public void testDeleteById() throws Exception {
        //given
        String idToDelete = "2";
        //when
        recipeService.deleteById(idToDelete);
        //no 'when', since method has void return type
        //then
        verify(recipeRepository, times(1)).deleteById(anyString());
    }
}