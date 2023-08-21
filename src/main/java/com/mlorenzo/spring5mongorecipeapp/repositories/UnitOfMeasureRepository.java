package com.mlorenzo.spring5mongorecipeapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mlorenzo.spring5mongorecipeapp.domain.UnitOfMeasure;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {
    Optional<UnitOfMeasure> findByDescription(String description);
}
