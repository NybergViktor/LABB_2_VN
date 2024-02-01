package com.labb2.recipes_api.services;

import com.labb2.recipes_api.models.Comment;
import com.labb2.recipes_api.models.Recipe;
import com.labb2.recipes_api.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    private CommentService commentService;
    // skapa ett recept
    public Recipe addRecipe(Recipe recipe){
        return recipeRepository.save(recipe);
    }

    // Hämta alla recept
    public List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    // Hämta recept med tagg




    //lägga till kommentar till recept reference dokument
public Recipe addCommentToRecipe(String recipeId, Comment comment){
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        Comment savedComment = commentService.saveComment(comment);
        recipe.getComments().add(savedComment);
        return recipeRepository.save(recipe);
}


    // lägga till kommentar till recept inbäddat dokument
    /*public Recipe addCommentToRecipe(String recipeId, Comment comment){
        //Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        return recipeRepository.findById(recipeId)
                .map(recipe -> {
                    recipe.addComment(comment);
                    return recipeRepository.save(recipe);

                })
                .orElseThrow(() -> new EntityNotFoundException("Recipe with id: " + recipeId + " was not found!"));
    }*/
}
