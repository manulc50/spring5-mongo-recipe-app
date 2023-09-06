package com.mlorenzo.spring5mongorecipeapp.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.mlorenzo.spring5mongorecipeapp.repositories.RecipeRepository;
import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    ImageService imageService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    public void saveImageFile() throws Exception {
        //given
        String id = "1";
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
        		"fake image text".getBytes());
        Recipe recipe = new Recipe();
        recipe.setId(id);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        //when
        imageService.saveImageFile(id, multipartFile);
        //then
        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

}