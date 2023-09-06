package com.mlorenzo.spring5mongorecipeapp.services;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mlorenzo.spring5mongorecipeapp.commands.CategoryCommand;
import com.mlorenzo.spring5mongorecipeapp.converters.CategoryToCategoryCommand;
import com.mlorenzo.spring5mongorecipeapp.domain.Category;
import com.mlorenzo.spring5mongorecipeapp.exceptions.NotFoundException;
import com.mlorenzo.spring5mongorecipeapp.repositories.CategoryRepository;

//Anotación para poder usar Mockito en esta clase de pruebas
@RunWith(MockitoJUnitRunner.class) // Otra opción a esta anotación es usar la expresión o línea "MockitoAnnotations.initMocks(this);" en el método "setUp"
public class CategoryServiceImplTest {
	
	@Mock
    CategoryRepository categoryRepository;
	
	CategoryToCategoryCommand categoryToCategoryCommand = new CategoryToCategoryCommand();
	CategoryService service;
	
	@Before
	public void setUp() throws Exception {
		service = new CategoryServiceImpl(categoryRepository,categoryToCategoryCommand);
	}
	
	@Test
	public void getCategoriesTest() {
		//given
		Category cat1 = new Category();
		cat1.setId("1");
		Category cat2 = new Category();
		cat2.setId("2");
		Set<Category> returnedSet = new HashSet<Category>();
		returnedSet.add(cat1);
		returnedSet.add(cat2);
		when(categoryRepository.findAll()).thenReturn(returnedSet);
		//when
		Set<CategoryCommand> categoryCommands = service.getCategories();
		//then
		assertEquals(2, categoryCommands.size());
		// Si no se indica el número de llamadas en el método "times", por defecto es 1
		verify(categoryRepository).findAll();
	}
	
	@Test
	public void getCategoryByIdTest() throws Exception {
        Category categoryReturned = new Category();
        categoryReturned.setId("1");
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(categoryReturned));
        CategoryCommand categoryCommand = service.getCategoryById("1");
        assertNotNull("Null recipe returned", categoryCommand);
        // Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(categoryRepository, times(1)).findById(anyString());
        verify(categoryRepository, never()).findAll();
    }
	
	@Test(expected = NotFoundException.class)
	public void getCategoryByIdNotFoundTest() {
    	Optional<Category> categoryOptional = Optional.empty();
    	when(categoryRepository.findById(anyString())).thenReturn(categoryOptional);
        //should go boom
    	service.getCategoryById("1");
    	// Si no se indica el número de llamadas en el método "times", por defecto es 1
        verify(categoryRepository, times(1)).findById(anyString());
    }

}
