package abbyssoul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * An implementation of resource provider that fetches a list of available ingredients from a remote REST service.
 * Note that fetching remove resources is an async operation that can fail or timeout.
 */
@Component
public class IngredientsRestProvider implements IngredientsProvider {
    private static final String IngredientsUrl = "https://www.mocky.io/v2/5cdd037d300000da25e23402";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService executorService;

    @Autowired
    public IngredientsRestProvider(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Future<Ingredients> getIngredients() {
        return executorService.submit(() -> fetchIngredients());
    }

    private Ingredients fetchIngredients() {
        return restTemplate.getForObject(IngredientsUrl, Ingredients.class);
    }

}
