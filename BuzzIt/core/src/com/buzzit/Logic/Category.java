package com.buzzit.Logic;


public enum Category {
    SPORTS("SPORTS", 0),
    HISTORY("HISTORY", 1),
    SCIENCE("SCIENCE", 2),
    MUSIC("MUSIC", 3);

    private String category;
    private int index;

    Category(String category, int index){
        this.category = category;
        this.index = index;
    }

    public int getIndex(){
        return index;
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
