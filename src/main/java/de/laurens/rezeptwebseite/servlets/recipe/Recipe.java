package de.laurens.rezeptwebseite.servlets.recipe;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipe {

    public String id;
    public String title;
    public String informationSet;
    public String category;
    public String ingredients;
    public String instruction;
    public String[] images;
    public String[] keywords;
    public int author;

    public Recipe(String id, String title, String informationSet, String category, String ingredients, String instruction, String[] images, String[] keywords, int author) {
        this.id = id;
        this.title = title;
        this.informationSet = informationSet;
        this.category = category;
        this.ingredients = ingredients;
        this.instruction = instruction;
        this.images = images;
        this.keywords = keywords;
        this.author = author;
    }
}
