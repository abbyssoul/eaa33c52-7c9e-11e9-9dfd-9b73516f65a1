package abbyssoul;

import java.util.concurrent.Future;

/**
 * A client to access recipes service
 */
public interface RecipesProvider {
    Future<Recipes> getRecipes();
}
