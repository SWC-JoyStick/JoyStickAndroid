package com.codeseasy.loginui.cardView;

public class Card {
    private String name;
    private int imageId;

    public Card(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }
}
