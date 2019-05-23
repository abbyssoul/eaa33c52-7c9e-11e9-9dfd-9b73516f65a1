package abbyssoul;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.style.ToStringCreator;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {

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

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("title", title)
                .append("best-before", bestBefore)
                .append("use-by", useBy)
                .toString();
    }


    private String title;
    private Date bestBefore;
    private Date useBy;
}
