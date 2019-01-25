package com.asramaum.siarum;

/**
 * Created by abdalla on 1/12/18.
 */

public class MenuData {

    private String menuName;
    private int menuImage;
    private String menuAppScriptLink;
    private String sheetLink;
    private String sheetLinkChart;


    public MenuData(String menuName, Integer menuImage, String menuAppScriptLink, String sheetLink, String sheetLinkChart) {
        this.menuName = menuName;
        this.menuImage = menuImage;
        this.menuAppScriptLink = menuAppScriptLink;
        this.sheetLink = sheetLink;
        this.sheetLinkChart = sheetLinkChart;
    }

    public String getMenuName() {
        return menuName;
    }

    public Integer getMenuImage() {
        return menuImage;
    }

    public String getMenuAppScriptLink() {
        return menuAppScriptLink;
    }

    public String getSheetLink() {
        return sheetLink;
    }

    public String getSheetLinkChart() {
        return sheetLinkChart;
    }
}
