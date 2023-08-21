package com.mlorenzo.spring5mongorecipeapp.domain;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notes {

	@Id
    private String id;
	
    private String recipeNotes;
}
