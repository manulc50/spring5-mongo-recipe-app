package com.mlorenzo.spring5mongorecipeapp.controllers;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mlorenzo.spring5mongorecipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5mongorecipeapp.services.ImageService;
import com.mlorenzo.spring5mongorecipeapp.services.RecipeService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

// Anotación para poder usar Mockito en esta clase de pruebas
@RunWith(MockitoJUnitRunner.class) // Otra opción a esta anotación es usar la expresión o línea "MockitoAnnotations.initMocks(this);" en el método "setUp"
public class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void getImageForm() throws Exception {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId("1");
        when(recipeService.findCommandById(anyString())).thenReturn(command);
        //when
        mockMvc.perform(get("/recipe/1/image"))
        	//then
            .andExpect(status().isOk())
            .andExpect(view().name("recipe/imageUploadForm"))
            .andExpect(model().attributeExists("recipe"));
        verify(recipeService, times(1)).findCommandById(anyString());
    }

    @Test
    public void handleImagePost() throws Exception {
    	//given
        MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                		"fake image text".getBytes());
        //when
        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
        	//then
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/recipe/1/show"));
        verify(imageService, times(1)).saveImageFile(anyString(), any());
    }


    @Test
    public void renderImageFromDB() throws Exception {
    	//given
        String s = "fake image text";
        when(imageService.getImage(anyString())).thenReturn(s.getBytes());
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
        	//then
            .andExpect(status().isOk())
            .andReturn().getResponse();
        byte[] reponseBytes = response.getContentAsByteArray();
        assertEquals(s.getBytes().length, reponseBytes.length);
    }

}