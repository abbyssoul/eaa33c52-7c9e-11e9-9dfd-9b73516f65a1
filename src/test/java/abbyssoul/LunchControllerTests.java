/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package abbyssoul;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LunchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipesProvider recipesProvider;

    @MockBean
    private IngredientsProvider ingredientsProvider;

    @Before
    public void setUp() {
        final Calendar c = Calendar.getInstance();

        final Date now = new Date();
        c.setTime(now);
        c.add(Calendar.DATE, 1);
        final Date tomorrow = c.getTime();

        c.add(Calendar.DATE, 1);
        final Date dayAfter = c.getTime();

        c.setTime(now);
        c.add(Calendar.DATE, -1);
        final Date yesterday = c.getTime();

        final List<Ingredient> allGood = Arrays.asList(
                new Ingredient("good stuff", tomorrow, dayAfter),
                new Ingredient("good else", dayAfter, dayAfter)
        );

        final List<Ingredient> allExpired = Arrays.asList(
                new Ingredient("rotten stuff", yesterday, yesterday),
                new Ingredient("rotten else", yesterday, yesterday)
        );

        final List<Ingredient> allNotTheBest = Arrays.asList(
                new Ingredient("questionable stuff", yesterday, dayAfter),
                new Ingredient("rotten else", yesterday, dayAfter)
        );

        final List<Ingredient> allIngredients = new ArrayList<>(allGood);
        allIngredients.addAll(allExpired);
        allIngredients.addAll(allNotTheBest);


        final List<Recipe> allRecipes = Arrays.asList(
                new Recipe("good food", allGood.stream().map(Ingredient::getTitle).collect(Collectors.toList())),
                new Recipe("all rotten", allExpired.stream().map(Ingredient::getTitle).collect(Collectors.toList())),
                new Recipe("mixed bag",
                        Stream.concat(allExpired.stream().limit(1), allGood.stream().limit(1))
                                .map(Ingredient::getTitle)
                                .collect(Collectors.toList())
                ),

                new Recipe("unknown food", Arrays.asList("odd stuff", "good else"))
        );


        given(ingredientsProvider.getIngredients()).willReturn(CompletableFuture.completedFuture(new Ingredients(allIngredients)));
        given(recipesProvider.getRecipes()).willReturn(CompletableFuture.completedFuture(new Recipes(allRecipes)));
    }


    @Test
    public void lunchReturnsOnlyKnownRecipes() throws Exception {
        given(ingredientsProvider.getIngredients()).willReturn(CompletableFuture.completedFuture(new Ingredients(Arrays.asList())));
        given(recipesProvider.getRecipes()).willReturn(CompletableFuture.completedFuture(new Recipes(
                Arrays.asList(new Recipe("unknown", Arrays.asList("someXYX")))
        )));

        mockMvc.perform(get("/lunch")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes", hasSize(0)));
    }


    @Test
    public void lunchReturnsOnlyNonExpiredRecipes() throws Exception {
        mockMvc.perform(get("/lunch")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes", hasSize(1)))
                .andExpect(jsonPath("$.recipes[0].title", is("good food")));
    }



    @Test
    public void errorReturnedIfFailedToFetchIngredients() throws Exception {
        final CompletableFuture<Ingredients> failedFetch = new CompletableFuture<>();
        failedFetch.completeExceptionally(new RuntimeException("Bad things happened"));

        given(ingredientsProvider.getIngredients()).willReturn(failedFetch);
        given(recipesProvider.getRecipes()).willReturn(CompletableFuture.completedFuture(new Recipes(
                Arrays.asList(new Recipe("something to eat", Arrays.asList("someXXXX")))
        )));

        mockMvc.perform(get("/lunch")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }


}
