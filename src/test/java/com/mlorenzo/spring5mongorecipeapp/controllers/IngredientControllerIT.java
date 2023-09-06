package com.mlorenzo.spring5mongorecipeapp.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.mlorenzo.spring5mongorecipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import com.mlorenzo.spring5mongorecipeapp.services.IngredientService;
import com.mlorenzo.spring5mongorecipeapp.services.UnitOfMeasureService;

@RunWith(SpringRunner.class)
// Nota: Por defecto, esta anotación hace que también se procesen las plantillas(En nuestro caso, plantillas Thymeleaf)
@WebMvcTest(controllers = IngredientController.class)
public class IngredientControllerIT {
	
	@MockBean // Crea una Mock del servicio "ingredientService" y lo inyecta en el contexto de Spring
    IngredientService ingredientService;
	
	@MockBean // Crea una Mock del servicio "unitOfMeasureService" y lo inyecta en el contexto de Spring
	UnitOfMeasureService unitOfMeasureService;

	@Autowired
    MockMvc mockMvc;

    @Test
    public void listIngredientsTest() throws Exception {
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
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService, times(1)).getIngredients(anyString());
    }
    
    @Test
    public void showIngredientTest() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        ingredientCommand.setUom(uomCommand);
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
        //when
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
        	//then
        	.andExpect(status().isOk())
            .andExpect(view().name("recipe/ingredient/show"))
            .andExpect(model().attributeExists("ingredient"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyString(), anyString());
    }
    
    @Test
    public void newIngredientFormTest() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId("1");
        when(ingredientService.createNewIngredient(anyString())).thenReturn(ingredientCommand);
        // En realidad no hace falta este "when" porque, por defecto, Mockito crea instancias de las colecciones
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
    public void updateIngredientFormTest() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
        // En realidad no hace falta este when porque, por defecto, Mockito crea instancias de las colecciones
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());
        //when
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
        	//then
    		.andExpect(status().isOk())
            .andExpect(view().name("recipe/ingredient/ingredientForm"))
            .andExpect(model().attributeExists("ingredient"))
            .andExpect(model().attributeExists("uomList"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyString(), anyString());
        verify(unitOfMeasureService, times(1)).listAllUoms();
    }
    
    @Test
    public void saveOrUpdateTest() throws Exception {
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
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService).saveIngredientCommand(any());
    }
    
    @Test
    public void deleteIngredientTest() throws Exception {
    	//when
    	mockMvc.perform(get("/recipe/2/ingredient/3/delete"))
    		//then
		   .andExpect(status().is3xxRedirection())
		   .andExpect(view().name("redirect:/recipe/2/ingredients"));
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(ingredientService).deleteById(anyString(), anyString());
    }
}
