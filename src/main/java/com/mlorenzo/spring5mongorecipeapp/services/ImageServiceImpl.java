package com.mlorenzo.spring5mongorecipeapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mlorenzo.spring5mongorecipeapp.domain.Recipe;
import com.mlorenzo.spring5mongorecipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5mongorecipeapp.repositories.RecipeRepository;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
	private final RecipeRepository recipeRepository;

    @Override
    public void saveImageFile(String recipeId, MultipartFile file) {
    	log.debug("Received a file");
    	Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
        try {
        	recipe.setImage(file.getBytes());
            recipeRepository.save(recipe);
        } catch (IOException e) {
            //todo handle better
            log.error("Error occurred", e);
            e.printStackTrace();
        }
    }

	@Override
	public byte[] getImage(String recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new NotFoundException("Recipe Not Found for id value: " + recipeId));
		return recipe.getImage();
	}
}
