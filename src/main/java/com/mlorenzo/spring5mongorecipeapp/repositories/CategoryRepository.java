package com.mlorenzo.spring5mongorecipeapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mlorenzo.spring5mongorecipeapp.domain.Category;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, String> {
    Optional<Category> findByDescription(String description);
}
