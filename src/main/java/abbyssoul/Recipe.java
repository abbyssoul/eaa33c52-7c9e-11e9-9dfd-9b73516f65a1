package abbyssoul;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {

    public Recipe() {}

    public Recipe(String title, List<String> ingredients) {
        this.title = title;
        this.ingredients = new HashSet<>(ingredients);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String value) {
        title = value;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> value) {
        ingredients = new HashSet<>(value);
    }

    private String title;
    private Set<String> ingredients;
}
