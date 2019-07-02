package com.asramaum.siarum.model;

/**
 * Created by austi on 12/9/2018.
 */

public class MainMenuViewPagerModel {
    private int imageMainMenuViewPagerItem;
    private String titleMainMenuViewPagerItem;
    private String descMainMenuViewPagerItem;

    public MainMenuViewPagerModel(int imageMainMenuViewPagerItem, String titleMainMenuViewPagerItem, String descMainMenuViewPagerItem){
        this.imageMainMenuViewPagerItem = imageMainMenuViewPagerItem;
        this.titleMainMenuViewPagerItem = titleMainMenuViewPagerItem;
        this.descMainMenuViewPagerItem = descMainMenuViewPagerItem;
    }

    public int getImageMainMenuViewPagerItem() {
        return imageMainMenuViewPagerItem;
    }

    public void setImageMainMenuViewPagerItem(int imageMainMenuViewPagerItem) {
        this.imageMainMenuViewPagerItem = imageMainMenuViewPagerItem;
    }

    public String getTitleMainMenuViewPagerItem() {
        return titleMainMenuViewPagerItem;
    }

    public void setTitleMainMenuViewPagerItem(String titleMainMenuViewPagerItem) {
        this.titleMainMenuViewPagerItem = titleMainMenuViewPagerItem;
    }

    public String getDescMainMenuViewPagerItem() {
        return descMainMenuViewPagerItem;
    }

    public void setDescMainMenuViewPagerItem(String descMainMenuViewPagerItem) {
        this.descMainMenuViewPagerItem = descMainMenuViewPagerItem;
    }


}
