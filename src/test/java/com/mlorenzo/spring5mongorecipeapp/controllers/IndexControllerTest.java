package com.mlorenzo.spring5mongorecipeapp.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.mlorenzo.spring5mongorecipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;
import com.mlorenzo.spring5mongorecipeapp.services.RecipeService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    IndexController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new IndexController(recipeService);
    }

    @Test
    public void testMockMVC() throws Exception {
    	//given
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        //when
        mockMvc.perform(get("/"))
        		//then
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void getIndexPage() throws Exception {
        //given
    	RecipeCommand recipeCommand1 = new RecipeCommand();
		recipeCommand1.setId("1");
		RecipeCommand recipeCommand2 = new RecipeCommand();
		recipeCommand2.setId("2");
		Set<RecipeCommand> returnedRecipeCommandSet = new HashSet<>();
		returnedRecipeCommandSet.add(recipeCommand1);
		returnedRecipeCommandSet.add(recipeCommand2);
        when(recipeService.getRecipes()).thenReturn(returnedRecipeCommandSet);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        //when
        String viewName = controller.getIndexPage(model);
        //then
        assertEquals("index", viewName);
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2, setInController.size());
    }

}