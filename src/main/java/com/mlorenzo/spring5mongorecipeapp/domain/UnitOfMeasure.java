package com.mlorenzo.spring5mongorecipeapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "unitOfMeasures")
public class UnitOfMeasure {

	@Id
    private String id;
	
    private String description;
}
