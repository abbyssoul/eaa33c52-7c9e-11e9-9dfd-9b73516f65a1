package abbyssoul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * An implementation of resource provider that fetches a list of available recipes from a remote REST service.
 * Note that fetching remove resources is an async operation that can fail or timeout.
 */
@Component
public class RecipesRestProvider implements RecipesProvider {
    private static final String RecipesUrl = "https://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";

    private final ExecutorService executorService;
    private final RestTemplate restTemplate;

    @Autowired
    public RecipesRestProvider(ExecutorService executorService, RestTemplate restTemplate) {
        this.executorService = executorService;
        this.restTemplate = restTemplate;
    }

    @Override
    public Future<Recipes> getRecipes() {
        return executorService.submit(() -> fetchRecipes());
    }

    private Recipes fetchRecipes() {
        return restTemplate.getForObject(RecipesUrl, Recipes.class);
    }
}
