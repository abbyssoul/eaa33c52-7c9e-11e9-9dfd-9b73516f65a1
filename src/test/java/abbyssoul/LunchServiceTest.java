package abbyssoul;

import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class LunchServiceTest {
    final Calendar calendar = Calendar.getInstance();
    final Date now = calendar.getTime();

    @Test
    public void noRecipesProducesEmptyList() {
        final LunchService testService = new LunchService();

        assertThat(testService.validRecipes(
                Arrays.asList(),
                Arrays.asList()).size(), is(0));

        assertThat(testService.validRecipes(
                Arrays.asList(),
                Arrays.asList(new Ingredient("useless", calendar.getTime(), calendar.getTime()))).size(), is(0));
    }

    @Test
    public void noIngredientRecipeIsValid() {
        final LunchService testService = new LunchService();

        assertThat(testService.validRecipes(
                    Arrays.asList(new Recipe("boom", Arrays.asList())),
                    Arrays.asList())
                .size(), is(1));

        assertThat(testService.validRecipes(
                    Arrays.asList(new Recipe("boom", Arrays.asList())),
                    Arrays.asList(new Ingredient("one", now, now)))
                .size(), is(1));
    }

    @Test
    public void noExpiredIngredientsInRecipes() {
        final LunchService testService = new LunchService();

        assertThat(testService.validRecipes(
                Arrays.asList(new Recipe("boom", Arrays.asList("expired", "ok"))),
                Arrays.asList(new Ingredient("one", now, expiredDate())))
                .size(), is(0));


        final Recipe okRecipe = new Recipe("ok", Arrays.asList("ok"));
        final List<Recipe> valid = testService.validRecipes(
                Arrays.asList(
                        new Recipe("old", Arrays.asList("expired", "ok")),
                        okRecipe
                ),
                Arrays.asList(
                        new Ingredient("one", now, futureDate()),
                        new Ingredient("ok", futureDate(), futureDate()),
                        new Ingredient("one", futureDate(), expiredDate())
                ));


        assertThat(valid.size(), is(1));
        assertThat(valid, contains(okRecipe));
    }


    @Test
    public void recipesWithNotBestIngredientsPutLast() {
        final LunchService testService = new LunchService();

        final Recipe okRecipe_1 = new Recipe("ok", Arrays.asList("ok", "ok-too"));
        final Recipe okRecipe_2 = new Recipe("ok", Arrays.asList("ok-too"));
        final Recipe notOkRecipe = new Recipe("so-so", Arrays.asList("ok", "so-so"));
        final List<Recipe> valid = testService.validRecipes(
                Arrays.asList(
                        okRecipe_1,
                        notOkRecipe,
                        okRecipe_2
                ),
                Arrays.asList(
                        new Ingredient("ok", futureDate(), futureDate()),
                        new Ingredient("ok-too", futureDate(), futureDate()),
                        new Ingredient("so-so", expiredDate(), futureDate())
                ));


        assertThat(valid.size(), is(3));
        assertThat(valid, contains(okRecipe_1, okRecipe_2, notOkRecipe));
    }


    Date expiredDate() {
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    Date futureDate() {
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, +1);
        return calendar.getTime();
    }
}