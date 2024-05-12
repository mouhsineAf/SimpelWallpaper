package com.devm.ads.util;

public class LayoutNativeAdConfig {
    private boolean nativeSmallStyleEnable = false;
    private int nativeCardBackgroundColor;
    private int nativeCardBackground;
    private int actionAdButtonBackgroundColor;
    private int actionAdButtonBackground;
    private int actionAdButtonTextColor;
    private int nativeAdTextTitleColor;
    private float nativeAdTitleSize;
    private boolean nativeAdMediaViewEnable = true;
    private boolean nativeAdTextBodyEnable;
    private int nativeAdTextBodyColor;

    public LayoutNativeAdConfig() {}

    public boolean isNativeSmallStyleEnable() {
        return nativeSmallStyleEnable;
    }

    public void setNativeSmallStyleEnable(boolean nativeSmallStyleEnable) {
        this.nativeSmallStyleEnable = nativeSmallStyleEnable;
    }

    public int getNativeCardBackgroundColor() {
        return nativeCardBackgroundColor;
    }

    public void setNativeCardBackgroundColor(int color) {
        this.nativeCardBackgroundColor = color;
    }

    public int getNativeCardBackground() {
        return nativeCardBackground;
    }

    public void setNativeCardBackground(int resourceId) {
        this.nativeCardBackground = resourceId;
    }

    public int getActionAdButtonBackgroundColor() {
        return actionAdButtonBackgroundColor;
    }

    public void setActionAdButtonBackgroundColor(int color) {
        this.actionAdButtonBackgroundColor = color;
    }

    public int getActionAdButtonBackground() {
        return actionAdButtonBackground;
    }

    public void setActionAdButtonBackground(int resourceId) {
        this.actionAdButtonBackground = resourceId;
    }

    public int getActionAdButtonTextColor() {
        return actionAdButtonTextColor;
    }

    public void setActionAdButtonTextColor(int color) {
        this.actionAdButtonTextColor = color;
    }

    public int getNativeAdTextTitleColor() {
        return nativeAdTextTitleColor;
    }

    public void setNativeAdTextTitleColor(int color) {
        this.nativeAdTextTitleColor = color;
    }

    public float getNativeAdTitleSize() {
        return nativeAdTitleSize;
    }

    public void setNativeAdTitleSize(float size) {
        this.nativeAdTitleSize = size;
    }

    public boolean isNativeAdMediaViewEnable() {
        return nativeAdMediaViewEnable;
    }

    public void setNativeAdMediaViewEnable(boolean enable) {
        this.nativeAdMediaViewEnable = enable;
    }

    public boolean isNativeAdTextBodyEnable() {
        return nativeAdTextBodyEnable;
    }

    public void setNativeAdTextBodyEnable(boolean enable) {
        this.nativeAdTextBodyEnable = enable;
    }

    public int getNativeAdTextBodyColor() {
        return nativeAdTextBodyColor;
    }

    public void setNativeAdTextBodyColor(int color) {
        this.nativeAdTextBodyColor = color;
    }
}
