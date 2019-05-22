package abbyssoul;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipes {

    @JsonCreator
    public Recipes(@JsonProperty("recipes") List<Recipe> recipes) {
        this.recipes = (recipes == null)
                ? new ArrayList<>()
                : recipes;
    }

    @NonNull
    public List<Recipe> getRecipes() {
        return recipes;
    }

    private final List<Recipe> recipes;
}
