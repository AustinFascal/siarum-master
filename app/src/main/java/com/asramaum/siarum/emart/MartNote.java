package com.asramaum.siarum.emart;

/**
 * Created by austi on 9/26/2018.
 */

public class MartNote {
    private String title;
    private int price;
    private int stock;

    public MartNote(){

    }

    public MartNote(String title, int price, int stock){
        this.title = title;
        this.price = price;
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
