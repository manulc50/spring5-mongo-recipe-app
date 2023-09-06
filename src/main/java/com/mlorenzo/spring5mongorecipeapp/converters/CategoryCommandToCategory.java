package com.mlorenzo.spring5mongorecipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mlorenzo.spring5mongorecipeapp.commands.CategoryCommand;
import com.mlorenzo.spring5mongorecipeapp.domain.Category;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category>{

    @Override
    public Category convert(CategoryCommand source) {
        if (source == null) {
            return null;
        }
        final Category category = new Category();
        category.setId(source.getId());
        return category;
    }
}
