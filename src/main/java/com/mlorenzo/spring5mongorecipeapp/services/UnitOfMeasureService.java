package com.mlorenzo.spring5mongorecipeapp.services;

import java.util.Set;

import com.mlorenzo.spring5mongorecipeapp.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUoms();
}
