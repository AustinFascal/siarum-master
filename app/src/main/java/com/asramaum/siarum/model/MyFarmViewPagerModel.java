package com.asramaum.siarum.model;

/**
 * Created by austi on 12/9/2018.
 */

public class MyFarmViewPagerModel {
    private String menuName;
    private String menuDesc;
    private int menuImage;
    private String menuAppScriptLink;
    private String sheetLink;
    private String sheetLinkChart;

    public MyFarmViewPagerModel(String menuName, String menuDesc, int menuImage, String menuAppScriptLink, String sheetLink, String sheetLinkChart){
        this.menuName = menuName;
        this.menuDesc = menuDesc;
        this.menuImage = menuImage;
        this.menuAppScriptLink = menuAppScriptLink;
        this.sheetLink = sheetLink;
        this.sheetLinkChart = sheetLinkChart;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(int menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuAppScriptLink() {
        return menuAppScriptLink;
    }

    public void setMenuAppScriptLink(String menuAppScriptLink) {
        this.menuAppScriptLink = menuAppScriptLink;
    }

    public String getSheetLink() {
        return sheetLink;
    }

    public void setSheetLink(String sheetLink) {
        this.sheetLink = sheetLink;
    }

    public String getSheetLinkChart() {
        return sheetLinkChart;
    }

    public void setSheetLinkChart(String sheetLinkChart) {
        this.sheetLinkChart = sheetLinkChart;
    }
}
