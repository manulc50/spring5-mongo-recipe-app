package com.mlorenzo.spring5mongorecipeapp.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mlorenzo.spring5mongorecipeapp.commands.IngredientCommand;
import com.mlorenzo.spring5mongorecipeapp.services.IngredientService;
import com.mlorenzo.spring5mongorecipeapp.services.UnitOfMeasureService;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IngredientController {
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        log.debug("Getting ingredient list for recipe id: " + recipeId);
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("ingredients", ingredientService.getIngredients(recipeId));
        return "recipe/ingredient/list";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String id, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        return "recipe/ingredient/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String newRecipeIngredient(@PathVariable String recipeId, Model model) {
        model.addAttribute("ingredient", ingredientService.createNewIngredient(recipeId));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientForm";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientForm";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute("ingredient") IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        log.debug("saved receipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteRecipeIngredient(@PathVariable String recipeId,
                                   @PathVariable String id){
        log.debug("deleting ingredient id:" + id);
        ingredientService.deleteById(recipeId, id);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
