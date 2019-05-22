package abbyssoul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Main business service of the application
 * to determine a list of valid recipes from the list of possible recipes and available ingredients.
 */
@Service
public class LunchService {
    private static final Logger logger = LoggerFactory.getLogger(LunchService.class);

    private final Calendar calendar = Calendar.getInstance();

    /**
     * Given list of all known recipes and a list of all ingredients - compute recipes for which we have non expired ingredients
     * @param recipes A collection of all known recipes that can possibly cooked.
     * @param ingredients A collection of ingredients available. Some ingredients may be expired.
     * @return A list of valid recipes for which there are all non-expired ingredients available.
     */
    @NonNull
    public List<Recipe> validRecipes(@NonNull List<Recipe> recipes, @NonNull List<Ingredient> ingredients) {
        final Date now = calendar.getTime();

        final Set<String> nonExpiredIngredients = new HashSet<>();
        final Set<String> notBestIngredients = new HashSet<>();
        for (Ingredient i : ingredients) {
            if (now.before(i.getUseBy())) {
                nonExpiredIngredients.add(i.getTitle());
                logger.info("Filtered out ingredient '{}' as {} is after {}", i.getTitle(), i.getUseBy(), now);
            }

            if (now.after(i.getBestBefore())) {
                notBestIngredients.add(i.getTitle());
                logger.info("Not best ingredient '{}' as {} is after {}", i.getTitle(), i.getBestBefore(), now);
            }
        }

        return recipes.stream()
                .filter(r -> nonExpiredIngredients.containsAll(r.getIngredients()))
                .sorted(Comparator.comparingInt(r -> (Collections.disjoint(r.getIngredients(), notBestIngredients) ? 1 : 0)))
                .collect(Collectors.toList());
    }
}
