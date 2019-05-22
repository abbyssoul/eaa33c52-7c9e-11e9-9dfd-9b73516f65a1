package abbyssoul;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {
    private String title;
    private Date bestBefore;
    private Date useBy;


    public Ingredient() {
    }


    public Ingredient(String title, Date bestBefore, Date useBy) {
        this.title = title;
        this.bestBefore = bestBefore;
        this.useBy = useBy;
    }

    @JsonProperty("best-before")
    public Date getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(Date value) {
        bestBefore = value;
    }


    @JsonProperty("use-by")
    public Date getUseBy() {
        return useBy;
    }

    public void setUseBy(Date value) {
        useBy = value;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String value) {
        title = value;
    }
}
