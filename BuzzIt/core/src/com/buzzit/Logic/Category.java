package com.buzzit.Logic;

/**
 * Created by wnfuk_000 on 04/06/2016.
 */
public enum Category {
    SPORTS("SPORTS", 0),
    HISTORY("HISTORY", 1),
    SCIENCE("SCIENCE", 2),
    MUSIC("MUSIC", 3);

    String category;
    int index;

    private Category(String category, int index){
        this.category = category;
        this.index = index;
    }

    static public int getIndex(Category category){
        return category.index;
    }

    static public Category[] allCategories(){
        Category[] t = new Category[Category.values().length];
        int i=0;
        for (Category cat: Category.values()) {
            t[i] = cat;
            i++;
        }
        return t;
    }
    
    public String getName(){
        return category;
    }

    static public Category getCategory(String cat){
        switch(cat){
            case "SPORTS":
                return SPORTS;
            case "HISTORY":
                return HISTORY;
            case "SCIENCE":
                return SCIENCE;
            case "MUSIC":
                return MUSIC;
            default:
                return null;
        }
    }
}
