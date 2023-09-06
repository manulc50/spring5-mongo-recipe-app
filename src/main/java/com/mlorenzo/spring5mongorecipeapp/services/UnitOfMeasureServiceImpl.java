package com.mlorenzo.spring5mongorecipeapp.services;

import org.springframework.stereotype.Service;

import com.mlorenzo.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import com.mlorenzo.spring5mongorecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.mlorenzo.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;

import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {
        return StreamSupport.stream(unitOfMeasureRepository.findAll()
                .spliterator(), false)
                .map(unitOfMeasureToUnitOfMeasureCommand::convert)
                .collect(Collectors.toSet());
    }
}
