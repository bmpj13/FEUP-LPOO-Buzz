package com.buzzit.Logic;


public enum Category {
    SPORTS("SPORTS", 0),
    HISTORY("HISTORY", 1),
    SCIENCE("SCIENCE", 2),
    MUSIC("MUSIC", 3);

    private String category;
    private int index;

    /**
     * Constructor
     * @param category string of category
     * @param index position in array
     */
    Category(String category, int index){
        this.category = category;
        this.index = index;
    }

    /**
     * Gets index position of array of size category.values().length
     * @return array position
     */
    public int getIndex(){
        return index;
    }


    /**
     * Gets string value of enum
     * @return string of category
     */
    public String getName(){
        return category;
    }


    /**
     * Gets the corresponding enum from a string
     * @param categoryName string of category
     * @return Enum of category
     */
    static public Category getCategory(String categoryName){
        switch(categoryName) {
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
