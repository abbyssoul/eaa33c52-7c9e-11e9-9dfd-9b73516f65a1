package abbyssoul;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredients {

    @JsonCreator
    public Ingredients(@JsonProperty("ingredients") List<Ingredient> ingredients) {
        this.ingredients = (ingredients == null)
                ? new ArrayList<>()
                : ingredients;
    }

    @NonNull
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    private final List<Ingredient> ingredients;
}
