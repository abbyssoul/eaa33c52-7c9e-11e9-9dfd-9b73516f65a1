package abbyssoul;

import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LunchController {
    private static final Logger logger = LoggerFactory.getLogger(LunchController.class);

    private final IngredientsProvider ingredientsProvider;
    private final RecipesProvider recipesProvider;
    private final LunchService lunchService;


    @Autowired
    public LunchController(IngredientsProvider ingredientsProvider,
                           RecipesProvider recipesProvider,
                           LunchService lunchService) {
        this.ingredientsProvider = ingredientsProvider;
        this.recipesProvider = recipesProvider;
        this.lunchService = lunchService;
    }

    @ResponseStatus(value = HttpStatus.GATEWAY_TIMEOUT, reason="Request to remote service failed")
    class FetchingDataError extends RuntimeException {}

    @RequestMapping("/lunch")
    public Recipes whatsForLunch() throws InterruptedException {
        try {
            final Future<Recipes> futureRecipes = recipesProvider.getRecipes();
            final Future<Ingredients> futureIngredients = ingredientsProvider.getIngredients();

            final Recipes recipes = futureRecipes.get();
            final Ingredients ingredients = futureIngredients.get();

            // We have recipes and ingredients - lets filter what we can make
            return new Recipes(lunchService.validRecipes(recipes.getRecipes(), ingredients.getIngredients()));
        } catch (ExecutionException ex) {
            logger.error("Failed to fetch data: ", ex);
            // Failed to fetch data:
            throw new FetchingDataError();
        }
    }


}
