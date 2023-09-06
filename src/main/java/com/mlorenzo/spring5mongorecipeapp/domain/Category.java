package com.mlorenzo.spring5mongorecipeapp.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "categories")
public class Category {
	
	@Id
    private String id;
	
    private String description;
    private Set<Recipe> recipes;
}
