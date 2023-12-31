package com.mlorenzo.spring5mongorecipeapp.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "recipes")
public class Recipe {

	@Id
    private String id;
	
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Set<Ingredient> ingredients = new HashSet<>();
    private byte[] image;
    private Difficulty difficulty;
    private String notes;
    
    @DBRef
    private Set<Category> categories = new HashSet<>();
}
