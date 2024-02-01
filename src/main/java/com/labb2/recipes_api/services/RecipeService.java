package com.labb2.recipes_api.services;

import com.labb2.recipes_api.exception.EntityNotFoundException;
import com.labb2.recipes_api.models.Comment;
import com.labb2.recipes_api.models.Recipe;
import com.labb2.recipes_api.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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



    // hämta specifikt recept med id
    public Optional<Recipe> getRecipeById(String id){
        return recipeRepository.findById(id);
    }

    // update recipe

    public Recipe updateRecipe(String id, Recipe updatedRecipe){
        return recipeRepository.findById(id)
                .map(existingRecipe -> {
                    if (updatedRecipe.getTitle() != null){
                        existingRecipe.setTitle(updatedRecipe.getTitle());
                    }
                    if (updatedRecipe.getDescription() != null){
                        existingRecipe.setDescription(updatedRecipe.getDescription());
                    }
                    if (updatedRecipe.getIngredients() != null){
                        existingRecipe.setIngredients(updatedRecipe.getIngredients());
                    }
                    if (updatedRecipe.getTags() != null){
                        existingRecipe.setTags(updatedRecipe.getTags());
                    }
                    return recipeRepository.save(existingRecipe);
                })
                .orElseThrow(() -> new EntityNotFoundException("Recipe with id: " + id + " was not found!"));
    }


    // Delete

    public void deleteRecipe (String id){
        recipeRepository.deleteById(id);
    }


    // find recipe på tags ( filtrera på taggar )
    public List<Recipe> findRecipesByTags(List<String> tags){
        return recipeRepository.findByTagsIn(tags);
    }


    //find ingredients
    public List<Recipe> findIngredientsInRecipe(List<String> ingredients){
        return recipeRepository.findIngredientsInRecipe(ingredients);
    }



    //lägga till kommentar till recept reference dokument
    public Recipe addCommentToRecipe(String recipeId, Comment comment) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        Comment savedComment = commentService.saveComment(comment);
        recipe.getComments().add(savedComment);
        return recipeRepository.save(recipe);
    }

    // pagination och sortering
    public Page<Recipe> getRecipeWithPaginationAndSorting(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return recipeRepository.findAll(pageable);
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
