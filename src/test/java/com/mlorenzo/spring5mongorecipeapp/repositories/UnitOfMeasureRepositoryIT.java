package com.mlorenzo.spring5mongorecipeapp.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.mlorenzo.spring5mongorecipeapp.bootstrap.RecipeBootstrap;
import com.mlorenzo.spring5mongorecipeapp.domain.UnitOfMeasure;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;
    
    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    RecipeRepository recipeRepository;

    @BeforeEach
    public void setUp() throws Exception {
    	recipeRepository.deleteAll();
    	unitOfMeasureRepository.deleteAll();
    	categoryRepository.deleteAll();
    	RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);
    	recipeBootstrap.onApplicationEvent(null);
    }

    @Test
    public void findByDescription() throws Exception {
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        assertEquals("Teaspoon", uomOptional.get().getDescription());
    }

    @Test
    public void findByDescriptionCup() throws Exception {
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Cup");
        assertEquals("Cup", uomOptional.get().getDescription());
    }

}