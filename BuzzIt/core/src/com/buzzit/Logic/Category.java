package com.buzzit.Logic;

/**
 * Created by wnfuk_000 on 04/06/2016.
 */
public enum Category {
    SPORTS("SPORTS"),
    HISTORY("HISTORY"),
    SCIENCE("SCIENCE"),
    MUSIC("MUSIC");

    String category;

    private Category(String category){
        this.category = category;
    }

    static public int getIndex(String category){
        int indice = 0;
        switch(category){
            case "SPORTS":
                indice = 0;
                break;
            case "HISTORY":
                indice = 1;
                break;
            case "SCIENCE":
                indice = 2;
                break;
            case "MUSIC":
                indice = 3;
                break;
        }
        return indice;
    }

    static public String[] allCategories(){
        String[] t = new String[Category.values().length];
        int i=0;
        for (Category cat: Category.values()) {
            t[i] = cat.getCategory();
            i++;
        }
        return t;
    }
    
    public String getCategory(){
        return category;
    }

    
}
