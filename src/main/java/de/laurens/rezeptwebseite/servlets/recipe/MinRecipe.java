package de.laurens.rezeptwebseite.servlets.recipe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinRecipe {

    public String id;
    public String title;
    public String informationSet;
    public String category;
    public int author;
    public int rating;

    public MinRecipe(String id, String title, String informationSet, String category, int author, int rating) {
        this.id = id;
        this.title = title;
        this.informationSet = informationSet;
        this.category = category;
        this.author = author;
        this.rating = rating;
    }
}
