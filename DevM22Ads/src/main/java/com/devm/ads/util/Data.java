package com.devm.ads.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.devm.ads.Config;

import java.util.Locale;

public class Data {

    private static Data shared;
    private SharedPreferences SP;
    private static final String filename = "sharedData";

    public Data(Context context) {
        SP = context.getApplicationContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    public static Data getInstance(Context context) {
        if (shared == null) {
            shared = new Data(context);
        }
        return shared;
    }

    public void putModeAdsData(String modeAdsBanner, String modeAdsNative, String modeAdsInterstitial, String modeAdsRewardedVideo) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.ModeAdsBanner, modeAdsBanner.toLowerCase(Locale.ROOT));
        editor.putString(Config.ModeAdsNative, modeAdsNative.toLowerCase(Locale.ROOT));
        editor.putString(Config.ModeAdsInterstitial, modeAdsInterstitial.toLowerCase(Locale.ROOT));
        editor.putString(Config.ModeAdsRewardedVideo, modeAdsRewardedVideo.toLowerCase(Locale.ROOT));

        editor.apply();
    }

    public void putAdmobUnitData(String admobBannerUnitId, String admobNativeUnitId, String admobInterUnitId, String admobRewardedVideoUnitId) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.AdmobBannerUnitId, admobBannerUnitId);
        editor.putString(Config.AdmobNativeUnitId, admobNativeUnitId);
        editor.putString(Config.AdmobInterUnitId, admobInterUnitId);
        editor.putString(Config.AdmobRewardedVideoUnitId, admobRewardedVideoUnitId);

        editor.apply();
    }

    public void putMaxUnitData(String maxBannerUnitId, String maxNativeUnitId, String maxInterUnitId, String maxRewardedVideoUnitId) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.MaxBannerUnitId, maxBannerUnitId);
        editor.putString(Config.MaxNativeUnitId, maxNativeUnitId);
        editor.putString(Config.MaxInterUnitId, maxInterUnitId);
        editor.putString(Config.MaxRewardedVideoUnitId, maxRewardedVideoUnitId);

        editor.apply();
    }

    public void putFacebookUnitData(String facebookBannerUnitId, String facebookNativeUnitId, String facebookInterUnitId, String facebookRewardedVideoUnitId) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.FacebookBannerUnitId, facebookBannerUnitId);
        editor.putString(Config.FacebookNativeUnitId, facebookNativeUnitId);
        editor.putString(Config.FacebookInterUnitId, facebookInterUnitId);
        editor.putString(Config.FacebookRewardedVideoUnitId, facebookRewardedVideoUnitId);

        editor.apply();
    }

    public void putCharBoostUnitData(String charBoostAppId, String charBoostAppSignature) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.CharBoostAppId, charBoostAppId);
        editor.putString(Config.CharBoostAppSignature, charBoostAppSignature);

        editor.apply();
    }

    public void putIronSourceUnitData(String ironSourceAppId, String ironSourceBannerUnitId, String ironSourceInterUnitId, String ironSourceRewardVideoUnitId) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.IronSourceAppId, ironSourceAppId);
        editor.putString(Config.IronSourceBannerUnitId, ironSourceBannerUnitId);
        editor.putString(Config.IronSourceInterUnitId, ironSourceInterUnitId);
        editor.putString(Config.IronSourceRewardVideoUnitId, ironSourceRewardVideoUnitId);

        editor.apply();
    }

    public void putUnityUnitData(String unityGameId, String unityBannerUnitId, String unityInterUnitId, String unityRewardedVideoUnitId) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.UnityAdGameId, unityGameId);
        editor.putString(Config.UnityAdBannerUnitId, unityBannerUnitId);
        editor.putString(Config.UnityAdInterUnitId, unityInterUnitId);
        editor.putString(Config.UnityAdRewardedVideoUnitId, unityRewardedVideoUnitId);

        editor.apply();
    }


    public void putAdColonyUnitData(String adColonyAppId, String adColonyBannerUnitId, String adColonyInterUnitId, String adColonyRewardedVideoUnitId) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.AdColonyAppId, adColonyAppId);
        editor.putString(Config.AdColonyBannerUnitId, adColonyBannerUnitId);
        editor.putString(Config.AdColonyInterUnitId, adColonyInterUnitId);
        editor.putString(Config.AdColonyRewardUnitId, adColonyRewardedVideoUnitId);

        editor.apply();
    }


    public void putVungleUnitData(String vungleAppId, String vungleBannerUnitId, String vungleInterUnitId, String vungleRewardVideoUnitId) {
        SharedPreferences.Editor editor;
        editor = SP.edit();
        editor.putString(Config.VungleAppId, vungleAppId);
        editor.putString(Config.VungleBannerUnitId, vungleBannerUnitId);
        editor.putString(Config.VungleInterUnitId, vungleInterUnitId);
        editor.putString(Config.VungleRewardVideoUnitId, vungleRewardVideoUnitId);

        editor.apply();
    }

    //
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
