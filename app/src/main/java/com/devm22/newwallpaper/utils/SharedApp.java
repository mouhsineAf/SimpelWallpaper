package com.devm22.newwallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.devm22.newwallpaper.Config;

public class SharedApp {
    private static SharedApp shared;
    private SharedPreferences SP;
    private static final String filename="shared";

    public SharedApp(Context context) {
        SP = context.getApplicationContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    public static SharedApp getInstance(Context context) {
        if (shared == null) {
            shared = new SharedApp(context);
        }
        return shared;
    }

    public void putSplashData(boolean modeAdsBanner, boolean modeAdsInterstitial, boolean modeAdsNative, String admobBannerUnitId, String admobNativeUnitId, String admobInterUnitId) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putBoolean(Config.ModeAdsBanner, modeAdsBanner);
        editor.putBoolean(Config.ModeAdsInterstitial, modeAdsInterstitial);
        editor.putBoolean(Config.ModeAdsNative, modeAdsNative);

        editor.putString(Config.AdmobBannerUnitId, admobBannerUnitId);
        editor.putString(Config.AdmobNativeUnitId, admobNativeUnitId);
        editor.putString(Config.AdmobInterUnitId, admobInterUnitId);

        editor.apply();
    }


    public void putString(String key, String value) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String mDefault) {
        return SP.getString(key, mDefault);
    }

    public int getInt(String key, int mDefault) {
        return SP.getInt(key, mDefault);
    }

    public void putInt(String key, int num) {
        SharedPreferences.Editor editor;
        editor = SP.edit();

        editor.putInt(key, num);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean mDefault) {
        return SP.getBoolean(key, mDefault);
    }

    public void putBoolean(String key, boolean b) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putBoolean(key, b);
        editor.apply();
    }


    public void clear(){
        SharedPreferences.Editor editor;
        editor = SP.edit();

        editor.clear();
        editor.apply();
    }

    public void remove(){
        SharedPreferences.Editor editor;
        editor = SP.edit();

        editor.remove(filename);
        editor.apply();
    }
}

