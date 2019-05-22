package abbyssoul;

import java.util.concurrent.Future;

/**
 * Service interface / repository to request a list of available ingredients.
 */
public interface IngredientsProvider {
    Future<Ingredients> getIngredients();
}
