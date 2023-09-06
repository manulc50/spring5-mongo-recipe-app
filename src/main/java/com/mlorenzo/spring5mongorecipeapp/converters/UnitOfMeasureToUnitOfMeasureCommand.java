package com.mlorenzo.spring5mongorecipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mlorenzo.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import com.mlorenzo.spring5mongorecipeapp.domain.UnitOfMeasure;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Override
    public UnitOfMeasureCommand convert(UnitOfMeasure unitOfMeasure) {
        if (unitOfMeasure != null) {
            final UnitOfMeasureCommand uomc = new UnitOfMeasureCommand();
            uomc.setId(unitOfMeasure.getId());
            uomc.setDescription(unitOfMeasure.getDescription());
            return uomc;
        }
        return null;
    }
}
