package com.wikitude.samples.util;

/**
 * Created by Mahesh on 11/25/2017.
 */


public class TileItem {
    private String name;
    private String title;
    private int thumbnail;
    private String backGroundColor;

    public TileItem() {
    }

    public TileItem(String name, String title, int thumbnail, String backGroundColor) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.title = title;
        this.backGroundColor =backGroundColor;
    }

    public String getbackGroundColor() {
        return backGroundColor;
    }

    public void setbackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
