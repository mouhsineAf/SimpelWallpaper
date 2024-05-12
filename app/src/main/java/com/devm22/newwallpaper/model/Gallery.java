package com.devm22.newwallpaper.model;

public class Gallery {
    private int galleryId;
    private String galleryName;
    private String galleryPath;
    private boolean isSelected = false;

    public Gallery(int galleryId, String galleryName, String galleryPath) {
        this.galleryId = galleryId;
        this.galleryName = galleryName;
        this.galleryPath = galleryPath;
    }

    public int getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(int galleryId) {
        this.galleryId = galleryId;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public String getGalleryPath() {
        return galleryPath;
    }

    public void setGalleryPath(String galleryPath) {
        this.galleryPath = galleryPath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
