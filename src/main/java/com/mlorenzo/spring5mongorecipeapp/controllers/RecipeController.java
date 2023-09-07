package com.mlorenzo.spring5mongorecipeapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.mlorenzo.spring5mongorecipeapp.commands.RecipeCommand;
import com.mlorenzo.spring5mongorecipeapp.services.CategoryService;
import com.mlorenzo.spring5mongorecipeapp.services.RecipeService;
import com.mlorenzo.spring5mongorecipeapp.commands.CategoryCommand;

import java.util.Set;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RecipeController { 
    private final RecipeService recipeService;
    private final CategoryService categoryService;

    @ModelAttribute("categorySet")
	public Set<CategoryCommand> getAllCategories(){
		return categoryService.getCategories();
	}
    
    @GetMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "recipe/show";
    }

    @GetMapping("recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeForm";
    }

    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return "recipe/recipeForm";
    }

    @PostMapping("recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
        	if(log.isDebugEnabled())
	            bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            return "recipe/recipeForm";
        }
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{id}/delete")
    public String deleteById(@PathVariable String id){
        log.debug("Deleting id: " + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

}
