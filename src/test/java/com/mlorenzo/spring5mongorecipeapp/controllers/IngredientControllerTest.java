package com.mlorenzo.spring5mongorecipeapp.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mlorenzo.spring5mongorecipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5mongorecipeapp.services.IngredientService;
import com.mlorenzo.spring5mongorecipeapp.services.UnitOfMeasureService;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    IngredientController controller;
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new IngredientController(ingredientService, unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListIngredients() throws Exception {
        //given
    	Set<IngredientCommand> ingredientCommands = new HashSet<>();
        when(ingredientService.getIngredients(anyString())).thenReturn(ingredientCommands);
        //when
        mockMvc.perform(get("/recipe/1/ingredients"))
        	//then
            .andExpect(status().isOk())
            .andExpect(view().name("recipe/ingredient/list"))
            .andExpect(model().attributeExists("recipeId"))
            .andExpect(model().attributeExists("ingredients"));
        verify(ingredientService, times(1)).getIngredients(anyString());
    }

    @Test
    public void testShowIngredient() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
        //when
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
        	//then
            .andExpect(status().isOk())
            .andExpect(view().name("recipe/ingredient/show"))
            .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void testNewIngredientForm() throws Exception {
        //given
    	IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId("1");
        when(ingredientService.createNewIngredient(anyString())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());
        //when
        mockMvc.perform(get("/recipe/1/ingredient/new"))
        	//then
            .andExpect(status().isOk())
            .andExpect(view().name("recipe/ingredient/ingredientForm"))
            .andExpect(model().attributeExists("ingredient"))
            .andExpect(model().attributeExists("uomList"));
        verify(ingredientService, times(1)).createNewIngredient(anyString());

    }

    @Test
    public void testUpdateIngredientForm() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());
        //when
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
        	//then
            .andExpect(status().isOk())
            .andExpect(view().name("recipe/ingredient/ingredientForm"))
            .andExpect(model().attributeExists("ingredient"))
            .andExpect(model().attributeExists("uomList"));
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId("3");
        command.setRecipeId("2");
        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);
        //when
        mockMvc.perform(post("/recipe/2/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string"))
        	//then
	        .andExpect(status().is3xxRedirection())
	        .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
    }

    @Test
    public void testDeleteIngredient() throws Exception {
        //when
        mockMvc.perform(get("/recipe/2/ingredient/3/delete"))
        	//then
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/recipe/2/ingredients"));
        verify(ingredientService, times(1)).deleteById(anyString(), anyString());
    }
}