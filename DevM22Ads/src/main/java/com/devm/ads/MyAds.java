package com.devm.ads;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.FrameLayout;

import com.devm.ads.companies.AdColonyAd;
import com.devm.ads.companies.AdmobAd;
import com.devm.ads.companies.ChartBoostAd;
import com.devm.ads.companies.FacebookAd;
import com.devm.ads.companies.IronSourceAd;
import com.devm.ads.companies.MaxAd;
import com.devm.ads.companies.UnityAd;
import com.devm.ads.companies.VungleAd;
import com.devm.ads.util.Data;
import com.devm.ads.util.LayoutNativeAdConfig;

public class MyAds {
    private static final String TAG = "MyAds";

    Activity activity = null;
    Data data;

    String modeAdsBanner = "0";
    String modeAdsNative = "0";
    String modeAdsInter = "0";
    String modeAdsRewardVideo = "0";

    AdmobAd admobAd;
    MaxAd maxAd;
    FacebookAd facebookAd;
    IronSourceAd ironSourceAd;
    ChartBoostAd chartBoostAd;
    AdColonyAd adColonyAd;
    UnityAd unityAd;
    VungleAd vungleAd;

    Thread thread;
    int timeOut = Config.DialogLoadingInterTimer;
    Dialog dialog;
    boolean result = false;
    CallbackDialogLoadingInterstitial callbackDialogLoadingInterstitial;
    int dialog_loading_interstitial = 0;
    CallbackInitialization callbackInitialization;

    public MyAds (Activity activity, CallbackInitialization callback) {
        this.activity = activity;
        data = new Data(activity);
        callbackInitialization = callback;

        modeAdsBanner = data.getString(Config.ModeAdsBanner, "0");
        modeAdsNative = data.getString(Config.ModeAdsNative, "0");
        modeAdsInter = data.getString(Config.ModeAdsInterstitial, "0");
        modeAdsRewardVideo = data.getString(Config.ModeAdsRewardedVideo, "0");

        if (modeAdsBanner.equalsIgnoreCase(Config.Admob) || modeAdsNative.equalsIgnoreCase(Config.Admob) || modeAdsInter.equalsIgnoreCase(Config.Admob) || modeAdsRewardVideo.equalsIgnoreCase(Config.Admob)) {
            admobAd = new AdmobAd(activity, new AdmobAd.CallbackInitialization() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();
                }

                @Override
                public void onInitializationFailed(String error) {
                    callbackInitialization.onInitializationFailed(error);
                }
            });
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Max) || modeAdsNative.equalsIgnoreCase(Config.Max) || modeAdsInter.equalsIgnoreCase(Config.Max) || modeAdsRewardVideo.equalsIgnoreCase(Config.Max)) {
            maxAd = new MaxAd(activity, new MaxAd.CallbackInitialization() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();
                }

                @Override
                public void onInitializationFailed(String error) {
                    callbackInitialization.onInitializationFailed(error);
                }
            });
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Facebook) || modeAdsNative.equalsIgnoreCase(Config.Facebook) || modeAdsInter.equalsIgnoreCase(Config.Facebook) || modeAdsRewardVideo.equalsIgnoreCase(Config.Facebook)) {
            facebookAd = new FacebookAd(activity, new FacebookAd.CallbackInitialization() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();
                }

                @Override
                public void onInitializationFailed(String error) {
                    callbackInitialization.onInitializationFailed(error);
                }
            });
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.IronSource) || modeAdsNative.equalsIgnoreCase(Config.IronSource) || modeAdsInter.equalsIgnoreCase(Config.IronSource) || modeAdsRewardVideo.equalsIgnoreCase(Config.IronSource)) {
            ironSourceAd = new IronSourceAd(activity, new IronSourceAd.CallbackInitialization() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();
                }

                @Override
                public void onInitializationFailed(String error) {
                    callbackInitialization.onInitializationFailed(error);
                }
            });
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.ChartBoost) || modeAdsNative.equalsIgnoreCase(Config.ChartBoost) || modeAdsInter.equalsIgnoreCase(Config.ChartBoost) || modeAdsRewardVideo.equalsIgnoreCase(Config.ChartBoost)) {
            chartBoostAd = new ChartBoostAd(activity, new ChartBoostAd.CallbackInitialization() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();
                }

                @Override
                public void onInitializationFailed(String error) {
                    callbackInitialization.onInitializationFailed(error);
                }
            });
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.AdColony) || modeAdsNative.equalsIgnoreCase(Config.AdColony) || modeAdsInter.equalsIgnoreCase(Config.AdColony) || modeAdsRewardVideo.equalsIgnoreCase(Config.AdColony)) {
            adColonyAd = new AdColonyAd(activity, new AdColonyAd.CallbackInitialization() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();
                }

                @Override
                public void onInitializationFailed(String error) {
                    callbackInitialization.onInitializationFailed(error);
                }
            });
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Unity) || modeAdsNative.equalsIgnoreCase(Config.Unity) || modeAdsInter.equalsIgnoreCase(Config.Unity) || modeAdsRewardVideo.equalsIgnoreCase(Config.Unity)) {
            unityAd = new UnityAd(activity, new UnityAd.CallbackInitialization() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();
                }

                @Override
                public void onInitializationFailed(String error) {
                    callbackInitialization.onInitializationFailed(error);
                }
            });
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Vungle) || modeAdsNative.equalsIgnoreCase(Config.Vungle) || modeAdsInter.equalsIgnoreCase(Config.Vungle) || modeAdsRewardVideo.equalsIgnoreCase(Config.Vungle)) {
            vungleAd = new VungleAd(activity, new VungleAd.CallbackInitialization() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();
                }

                @Override
                public void onInitializationFailed(String error) {
                    callbackInitialization.onInitializationFailed(error);
                }
            });
        }

    }


    // Create Banner Ad
    public void createBannerAd(CallbackBanner callbackBanner){
        modeAdsBanner = data.getString(Config.ModeAdsBanner, "0");
        switch (modeAdsBanner) {
            case Config.Admob:
                admobAd.createBanner(new AdmobAd.CallbackBanner() {
                    @Override
                    public void onAdLoaded(View adView) {
                        callbackBanner.onAdLoaded(adView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackBanner.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Max:
                maxAd.createBanner(new MaxAd.CallbackBanner() {
                    @Override
                    public void onAdLoaded(View adView) {
                        callbackBanner.onAdLoaded(adView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackBanner.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Facebook:
                facebookAd.createBanner(new FacebookAd.CallbackBanner() {
                    @Override
                    public void onAdLoaded(View adView) {
                        callbackBanner.onAdLoaded(adView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackBanner.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.IronSource:
                ironSourceAd.createBanner(new IronSourceAd.CallbackBanner() {
                    @Override
                    public void onAdLoaded(FrameLayout adView) {
                        callbackBanner.onAdLoaded(adView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackBanner.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.ChartBoost:
                chartBoostAd.createBanner(new ChartBoostAd.CallbackBanner() {
                    @Override
                    public void onAdLoaded(View adView) {
                        callbackBanner.onAdLoaded(adView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackBanner.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.AdColony:
                adColonyAd.createBanner(new AdColonyAd.CallbackBanner() {
                    @Override
                    public void onAdLoaded(View adView) {
                        callbackBanner.onAdLoaded(adView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackBanner.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Unity:
                unityAd.createBanner(new UnityAd.CallbackBanner() {
                    @Override
                    public void onAdLoaded(View adView) {
                        callbackBanner.onAdLoaded(adView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackBanner.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Vungle:
                vungleAd.createBanner(new VungleAd.CallbackBanner() {
                    @Override
                    public void onAdLoaded(View adView) {
                        callbackBanner.onAdLoaded(adView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackBanner.onAdFailedToLoad(error);
                    }
                });
                break;
        }
    }

    // Show Banner Ad
    public void showBannerAd(boolean show){
        switch (modeAdsBanner) {
            case Config.Admob:
                admobAd.show_banner_ad(show);
                break;
            case Config.Max:
                maxAd.show_banner_ad(show);
                break;
            case Config.Facebook:
                facebookAd.show_banner_ad(show);
                break;
            case Config.IronSource:
                ironSourceAd.show_banner_ad(show);
                break;
            case Config.ChartBoost:
                chartBoostAd.show_banner_ad(show);
                break;
            case Config.AdColony:
                adColonyAd.show_banner_ad(show);
                break;
            case Config.Unity:
                unityAd.show_banner_ad(show);
                break;
            case Config.Vungle:
                vungleAd.show_banner_ad(show);
                break;
        }
    }


    // Create Native Ad
    public void createNativeAd(LayoutNativeAdConfig layoutNativeAdConfig, CallbackNative callbackNative){
        modeAdsNative = data.getString(Config.ModeAdsNative, "0");
        switch (modeAdsNative) {
            case Config.Admob:
                admobAd.createNative(layoutNativeAdConfig, new AdmobAd.CallbackNative() {
                    @Override
                    public void onAdLoaded(View nativeAdView) {
                        callbackNative.onAdLoaded(nativeAdView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackNative.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Max:
                maxAd.createNative(layoutNativeAdConfig, new MaxAd.CallbackNative() {
                    @Override
                    public void onAdLoaded(View nativeAdView) {
                        callbackNative.onAdLoaded(nativeAdView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackNative.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Facebook:
                facebookAd.createNative(layoutNativeAdConfig, new FacebookAd.CallbackNative() {
                    @Override
                    public void onAdLoaded(View nativeAdView) {
                        callbackNative.onAdLoaded(nativeAdView);
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackNative.onAdFailedToLoad(error);
                    }
                });
                break;

        }
    }

    // Show Native Ad
    public void showNativeAd(boolean show){
        switch (modeAdsNative) {
            case Config.Admob:
                admobAd.show_native_ad(show);
                break;
            case Config.Max:
                maxAd.show_native_ad(show);
                break;
            case Config.Facebook:
                facebookAd.show_native_ad(show);
                break;

        }
    }


    // Create Inter Ad
    public void createInterstitialAd(CallbackInterstitial callbackInterstitial){
        modeAdsInter = data.getString(Config.ModeAdsInterstitial, "0");
        switch (modeAdsInter) {
            case Config.Admob:
                admobAd.createInterstitial(new AdmobAd.CallbackInterstitial() {
                    @Override
                    public void onAdLoaded() {
                        callbackInterstitial.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackInterstitial.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackInterstitial.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Max:
                maxAd.createInterstitial(new MaxAd.CallbackInterstitial() {
                    @Override
                    public void onAdLoaded() {
                        callbackInterstitial.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackInterstitial.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackInterstitial.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Facebook:
                facebookAd.createInterstitial(new FacebookAd.CallbackInterstitial() {
                    @Override
                    public void onAdLoaded() {
                        callbackInterstitial.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackInterstitial.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackInterstitial.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.IronSource:
                ironSourceAd.createInterstitial(new IronSourceAd.CallbackInterstitial() {
                    @Override
                    public void onAdLoaded() {
                        callbackInterstitial.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackInterstitial.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackInterstitial.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.ChartBoost:
                chartBoostAd.createInterstitial(new ChartBoostAd.CallbackInterstitial() {
                    @Override
                    public void onAdLoaded() {
                        callbackInterstitial.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackInterstitial.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackInterstitial.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.AdColony:
                adColonyAd.createInterstitial(new AdColonyAd.CallbackInterstitial() {
                    @Override
                    public void onAdLoaded() {
                        callbackInterstitial.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackInterstitial.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackInterstitial.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Unity:
                unityAd.createInterstitial(new UnityAd.CallbackInterstitial() {
                    @Override
                    public void onAdLoaded() {
                        callbackInterstitial.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackInterstitial.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackInterstitial.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Vungle:
                vungleAd.createInterstitial(new VungleAd.CallbackInterstitial() {
                    @Override
                    public void onAdLoaded() {
                        callbackInterstitial.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackInterstitial.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackInterstitial.onAdFailedToLoad(error);
                    }
                });
                break;
        }
    }

    // Show Inter Ad
    public void showInterstitialAd(){
        switch (modeAdsInter) {
            case Config.NoThing:
                break;
            case Config.Admob:
                if (admobAd.is_interstitial_ad_loaded()){
                    admobAd.show_interstitial_ad();
                }
                break;
            case Config.Max:
                if (maxAd.is_interstitial_ad_loaded()){
                    maxAd.show_interstitial_ad();
                }
                break;
            case Config.Facebook:
                if (facebookAd.is_interstitial_ad_loaded()){
                    facebookAd.show_interstitial_ad();
                }
                break;
            case Config.IronSource:
                if (ironSourceAd.is_interstitial_ad_loaded()){
                    ironSourceAd.show_interstitial_ad();
                }
                break;
            case Config.ChartBoost:
                if (chartBoostAd.is_interstitial_ad_loaded()){
                    chartBoostAd.show_interstitial_ad();
                }
                break;
            case Config.AdColony:
                if (adColonyAd.is_interstitial_ad_loaded()){
                    adColonyAd.show_interstitial_ad();
                }
                break;
            case Config.Unity:
                if (unityAd.is_interstitial_ad_loaded()){
                    unityAd.show_interstitial_ad();
                }
                break;
            case Config.Vungle:
                if (vungleAd.is_interstitial_ad_loaded()){
                    vungleAd.show_interstitial_ad();
                }
                break;
        }
    }

    // Show Dialog Inter Ad
    public void showDialogInterstitialAd(int dialog_loading_interstitial, int dialogTimer, CallbackDialogLoadingInterstitial callback){
        this.dialog_loading_interstitial = dialog_loading_interstitial;
        timeOut = dialogTimer;
        callbackDialogLoadingInterstitial = callback;
        switch (modeAdsInter) {
            case Config.NoThing:
                callbackDialogLoadingInterstitial.onFinish(false);
                break;
            case Config.Admob:
                if (admobAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Max:
                if (maxAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Facebook:
                if (facebookAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.IronSource:
                if (ironSourceAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.ChartBoost:
                if (chartBoostAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.AdColony:
                if (adColonyAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Unity:
                if (unityAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Vungle:
                if (vungleAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;

        }

    }

    public void showDialogInterstitialAd(int dialogTimer, CallbackDialogLoadingInterstitial callback){
        timeOut = dialogTimer;
        callbackDialogLoadingInterstitial = callback;
        switch (modeAdsInter) {
            case Config.NoThing:
                callbackDialogLoadingInterstitial.onFinish(false);
                break;
            case Config.Admob:
                if (admobAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Max:
                if (maxAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Facebook:
                if (facebookAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.IronSource:
                if (ironSourceAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.ChartBoost:
                if (chartBoostAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.AdColony:
                if (adColonyAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Unity:
                if (unityAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Vungle:
                if (vungleAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;

        }

    }

    public void showDialogInterstitialAd(CallbackDialogLoadingInterstitial callback){
        callbackDialogLoadingInterstitial = callback;
        switch (modeAdsInter) {
            case Config.NoThing:
                callbackDialogLoadingInterstitial.onFinish(false);
                break;
            case Config.Admob:
                if (admobAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Max:
                if (maxAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Facebook:
                if (facebookAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.IronSource:
                if (ironSourceAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.ChartBoost:
                if (chartBoostAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.AdColony:
                if (adColonyAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Unity:
                if (unityAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;
            case Config.Vungle:
                if (vungleAd.is_interstitial_ad_loaded()){
                    callbackDialogLoadingInterstitial.onFinish(true);
                }else {
                    show();
                    run();
                }
                break;

        }

    }

    // Create Reward Ad
    public void createRewardAd(CallbackRewardVideo callbackRewardVideo){
        modeAdsRewardVideo = data.getString(Config.ModeAdsRewardedVideo, "0");
        switch (modeAdsRewardVideo) {
            case Config.Admob:
                admobAd.createRewardedVideo(new AdmobAd.CallbackRewardVideo() {
                    @Override
                    public void onAdRewarded(Boolean b) {
                        callbackRewardVideo.onAdRewarded(b);
                    }

                    @Override
                    public void onAdLoaded() {
                        callbackRewardVideo.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackRewardVideo.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackRewardVideo.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Max:
                maxAd.createRewardedVideo(new MaxAd.CallbackRewardVideo() {
                    @Override
                    public void onAdRewarded(Boolean b) {
                        callbackRewardVideo.onAdRewarded(b);
                    }

                    @Override
                    public void onAdLoaded() {
                        callbackRewardVideo.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackRewardVideo.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackRewardVideo.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Facebook:
                facebookAd.createRewardedVideo(new FacebookAd.CallbackRewardVideo() {
                    @Override
                    public void onAdRewarded(Boolean b) {
                        callbackRewardVideo.onAdRewarded(b);
                    }

                    @Override
                    public void onAdLoaded() {
                        callbackRewardVideo.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackRewardVideo.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackRewardVideo.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.IronSource:
                ironSourceAd.createRewardedVideo(new IronSourceAd.CallbackRewardVideo() {
                    @Override
                    public void onAdRewarded(Boolean b) {
                        callbackRewardVideo.onAdRewarded(b);
                    }

                    @Override
                    public void onAdLoaded() {
                        callbackRewardVideo.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackRewardVideo.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackRewardVideo.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.ChartBoost:
                chartBoostAd.createRewardedVideo(new ChartBoostAd.CallbackRewardVideo() {
                    @Override
                    public void onAdRewarded(Boolean b) {
                        callbackRewardVideo.onAdRewarded(b);
                    }

                    @Override
                    public void onAdLoaded() {
                        callbackRewardVideo.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackRewardVideo.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackRewardVideo.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.AdColony:
                adColonyAd.createRewardedVideo(new AdColonyAd.CallbackRewardVideo() {
                    @Override
                    public void onAdRewarded(Boolean b) {
                        callbackRewardVideo.onAdRewarded(b);
                    }

                    @Override
                    public void onAdLoaded() {
                        callbackRewardVideo.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackRewardVideo.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackRewardVideo.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Unity:
                unityAd.createRewardedVideo(new UnityAd.CallbackRewardVideo() {
                    @Override
                    public void onAdRewarded(Boolean b) {
                        callbackRewardVideo.onAdRewarded(b);
                    }

                    @Override
                    public void onAdLoaded() {
                        callbackRewardVideo.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackRewardVideo.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackRewardVideo.onAdFailedToLoad(error);
                    }
                });
                break;
            case Config.Vungle:
                vungleAd.createRewardedVideo(new VungleAd.CallbackRewardVideo() {
                    @Override
                    public void onAdRewarded(Boolean b) {
                        callbackRewardVideo.onAdRewarded(b);
                    }

                    @Override
                    public void onAdLoaded() {
                        callbackRewardVideo.onAdLoaded();
                    }

                    @Override
                    public void onAdClosed() {
                        callbackRewardVideo.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        callbackRewardVideo.onAdFailedToLoad(error);
                    }
                });
                break;
        }
    }

    // Show Reward Ad
    public void showRewardAd(){
        switch (modeAdsRewardVideo) {
            case Config.Admob:
                if (admobAd.is_rewarded_video_loaded()){
                    admobAd.show_rewarded_video_ad();
                }
                break;
            case Config.Max:
                if (maxAd.is_rewarded_video_loaded()){
                    maxAd.show_rewarded_video_ad();
                }
                break;
            case Config.Facebook:
                if (facebookAd.is_rewarded_video_loaded()){
                    facebookAd.show_rewarded_video_ad();
                }
                break;
            case Config.IronSource:
                if (ironSourceAd.is_rewarded_video_loaded()){
                    ironSourceAd.show_rewarded_video_ad();
                }
                break;
            case Config.ChartBoost:
                if (chartBoostAd.is_rewarded_video_loaded()){
                    chartBoostAd.show_rewarded_video_ad();
                }
                break;
            case Config.AdColony:
                if (adColonyAd.is_rewarded_video_loaded()){
                    adColonyAd.show_rewarded_video_ad();
                }
                break;
            case Config.Unity:
                if (unityAd.is_rewarded_video_loaded()){
                    unityAd.show_rewarded_video_ad();
                }
                break;
            case Config.Vungle:
                if (vungleAd.is_rewarded_video_loaded()){
                    vungleAd.show_rewarded_video_ad();
                }
                break;
        }
    }

    //Inter Manager

    private void show() {
        dialog = new Dialog(activity);
        dialog.setCancelable(false);
        if (dialog_loading_interstitial == 0){
            dialog.setContentView(R.layout.dialog_loading_ads);
        }else {
            dialog.setContentView(dialog_loading_interstitial);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void run(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                switch (modeAdsInter) {
                    case Config.Admob:
                        if (admobAd.is_rewarded_video_loaded()){
                            result = true;
                        }else {
                            for (int i = 0; i < timeOut; i++) {
                                if (admobAd.is_interstitial_ad_loaded()){
                                    result = true;
                                    runOnUiThread();
                                    break;
                                }


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case Config.Max:
                        if (maxAd.is_rewarded_video_loaded()){
                            result = true;
                        }else {
                            for (int i = 0; i < timeOut; i++) {
                                if (maxAd.is_interstitial_ad_loaded()){
                                    result = true;
                                    runOnUiThread();
                                    break;
                                }


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case Config.Facebook:
                        if (facebookAd.is_rewarded_video_loaded()){
                            result = true;
                        }else {
                            for (int i = 0; i < timeOut; i++) {
                                if (facebookAd.is_interstitial_ad_loaded()){
                                    result = true;
                                    runOnUiThread();
                                    break;
                                }


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case Config.IronSource:
                        if (ironSourceAd.is_rewarded_video_loaded()){
                            result = true;
                        }else {
                            for (int i = 0; i < timeOut; i++) {
                                if (ironSourceAd.is_interstitial_ad_loaded()){
                                    result = true;
                                    runOnUiThread();
                                    break;
                                }


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case Config.ChartBoost:
                        if (chartBoostAd.is_rewarded_video_loaded()){
                            result = true;
                        }else {
                            for (int i = 0; i < timeOut; i++) {
                                if (chartBoostAd.is_interstitial_ad_loaded()){
                                    result = true;
                                    runOnUiThread();
                                    break;
                                }


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case Config.AdColony:
                        if (adColonyAd.is_rewarded_video_loaded()){
                            result = true;
                        }else {
                            for (int i = 0; i < timeOut; i++) {
                                if (adColonyAd.is_interstitial_ad_loaded()){
                                    result = true;
                                    runOnUiThread();
                                    break;
                                }


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case Config.Unity:
                        if (unityAd.is_rewarded_video_loaded()){
                            result = true;
                        }else {
                            for (int i = 0; i < timeOut; i++) {
                                if (unityAd.is_interstitial_ad_loaded()){
                                    result = true;
                                    runOnUiThread();
                                    break;
                                }


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case Config.Vungle:
                        if (vungleAd.is_rewarded_video_loaded()){
                            result = true;
                        }else {
                            for (int i = 0; i < timeOut; i++) {
                                if (vungleAd.is_interstitial_ad_loaded()){
                                    result = true;
                                    runOnUiThread();
                                    break;
                                }


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                }

                runOnUiThread();


            }
        });
        thread.start();
    }

    private void runOnUiThread() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (dialog != null){
                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
                thread = null;

                callbackDialogLoadingInterstitial.onFinish(result);

            }
        });
    }


    //interfaces
    public interface CallbackInitialization {
        void onInitializationComplete();
        void onInitializationFailed(String error);
    }

    public interface CallbackBanner {
        void onAdLoaded(View adView);
        void onAdFailedToLoad(String error);
    }

    public interface CallbackNative {
        void onAdLoaded(View nativeAdView);
        void onAdFailedToLoad(String error);
    }

    public interface CallbackInterstitial {
        void onAdLoaded();
        void onAdClosed();
        void onAdFailedToLoad(String error);
    }

    public interface CallbackRewardVideo {
        void onAdRewarded(Boolean b);
        void onAdLoaded();
        void onAdClosed();
        void onAdFailedToLoad(String error);
    }

    public interface CallbackDialogLoadingInterstitial {
        void onFinish(Boolean isLoaded);
    }

    public void onAdPause () {
        if (modeAdsBanner.equalsIgnoreCase(Config.Admob) || modeAdsNative.equalsIgnoreCase(Config.Admob) || modeAdsInter.equalsIgnoreCase(Config.Admob) || modeAdsRewardVideo.equalsIgnoreCase(Config.Admob)) {
            admobAd.onMainPause();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Max) || modeAdsNative.equalsIgnoreCase(Config.Max) || modeAdsInter.equalsIgnoreCase(Config.Max) || modeAdsRewardVideo.equalsIgnoreCase(Config.Max)) {
            maxAd.onMainPause();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Facebook) || modeAdsNative.equalsIgnoreCase(Config.Facebook) || modeAdsInter.equalsIgnoreCase(Config.Facebook) || modeAdsRewardVideo.equalsIgnoreCase(Config.Facebook)) {
            facebookAd.onMainPause();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.IronSource) || modeAdsNative.equalsIgnoreCase(Config.IronSource) || modeAdsInter.equalsIgnoreCase(Config.IronSource) || modeAdsRewardVideo.equalsIgnoreCase(Config.IronSource)) {
            ironSourceAd.onMainPause();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.ChartBoost) || modeAdsNative.equalsIgnoreCase(Config.ChartBoost) || modeAdsInter.equalsIgnoreCase(Config.ChartBoost) || modeAdsRewardVideo.equalsIgnoreCase(Config.ChartBoost)) {
            chartBoostAd.onMainPause();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.AdColony) || modeAdsNative.equalsIgnoreCase(Config.AdColony) || modeAdsInter.equalsIgnoreCase(Config.AdColony) || modeAdsRewardVideo.equalsIgnoreCase(Config.AdColony)) {
            adColonyAd.onMainPause();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Unity) || modeAdsNative.equalsIgnoreCase(Config.Unity) || modeAdsInter.equalsIgnoreCase(Config.Unity) || modeAdsRewardVideo.equalsIgnoreCase(Config.Unity)) {
            unityAd.onMainPause();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Vungle) || modeAdsNative.equalsIgnoreCase(Config.Vungle) || modeAdsInter.equalsIgnoreCase(Config.Vungle) || modeAdsRewardVideo.equalsIgnoreCase(Config.Vungle)) {
            vungleAd.onMainPause();
        }
    }

    public void onAdResume () {
        if (modeAdsBanner.equalsIgnoreCase(Config.Admob) || modeAdsNative.equalsIgnoreCase(Config.Admob) || modeAdsInter.equalsIgnoreCase(Config.Admob) || modeAdsRewardVideo.equalsIgnoreCase(Config.Admob)) {
            admobAd.onMainResume();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Max) || modeAdsNative.equalsIgnoreCase(Config.Max) || modeAdsInter.equalsIgnoreCase(Config.Max) || modeAdsRewardVideo.equalsIgnoreCase(Config.Max)) {
            maxAd.onMainResume();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Facebook) || modeAdsNative.equalsIgnoreCase(Config.Facebook) || modeAdsInter.equalsIgnoreCase(Config.Facebook) || modeAdsRewardVideo.equalsIgnoreCase(Config.Facebook)) {
            facebookAd.onMainResume();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.IronSource) || modeAdsNative.equalsIgnoreCase(Config.IronSource) || modeAdsInter.equalsIgnoreCase(Config.IronSource) || modeAdsRewardVideo.equalsIgnoreCase(Config.IronSource)) {
            ironSourceAd.onMainResume();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.ChartBoost) || modeAdsNative.equalsIgnoreCase(Config.ChartBoost) || modeAdsInter.equalsIgnoreCase(Config.ChartBoost) || modeAdsRewardVideo.equalsIgnoreCase(Config.ChartBoost)) {
            chartBoostAd.onMainResume();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.AdColony) || modeAdsNative.equalsIgnoreCase(Config.AdColony) || modeAdsInter.equalsIgnoreCase(Config.AdColony) || modeAdsRewardVideo.equalsIgnoreCase(Config.AdColony)) {
            adColonyAd.onMainResume();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Unity) || modeAdsNative.equalsIgnoreCase(Config.Unity) || modeAdsInter.equalsIgnoreCase(Config.Unity) || modeAdsRewardVideo.equalsIgnoreCase(Config.Unity)) {
            unityAd.onMainResume();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Vungle) || modeAdsNative.equalsIgnoreCase(Config.Vungle) || modeAdsInter.equalsIgnoreCase(Config.Vungle) || modeAdsRewardVideo.equalsIgnoreCase(Config.Vungle)) {
            vungleAd.onMainResume();
        }
    }

    public void onAdDestroy () {
        if (modeAdsBanner.equalsIgnoreCase(Config.Admob) || modeAdsNative.equalsIgnoreCase(Config.Admob) || modeAdsInter.equalsIgnoreCase(Config.Admob) || modeAdsRewardVideo.equalsIgnoreCase(Config.Admob)) {
            admobAd.onMainDestroy();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Max) || modeAdsNative.equalsIgnoreCase(Config.Max) || modeAdsInter.equalsIgnoreCase(Config.Max) || modeAdsRewardVideo.equalsIgnoreCase(Config.Max)) {
            maxAd.onMainDestroy();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Facebook) || modeAdsNative.equalsIgnoreCase(Config.Facebook) || modeAdsInter.equalsIgnoreCase(Config.Facebook) || modeAdsRewardVideo.equalsIgnoreCase(Config.Facebook)) {
            facebookAd.onMainDestroy();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.IronSource) || modeAdsNative.equalsIgnoreCase(Config.IronSource) || modeAdsInter.equalsIgnoreCase(Config.IronSource) || modeAdsRewardVideo.equalsIgnoreCase(Config.IronSource)) {
            ironSourceAd.onMainDestroy();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.ChartBoost) || modeAdsNative.equalsIgnoreCase(Config.ChartBoost) || modeAdsInter.equalsIgnoreCase(Config.ChartBoost) || modeAdsRewardVideo.equalsIgnoreCase(Config.ChartBoost)) {
            chartBoostAd.onMainDestroy();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.AdColony) || modeAdsNative.equalsIgnoreCase(Config.AdColony) || modeAdsInter.equalsIgnoreCase(Config.AdColony) || modeAdsRewardVideo.equalsIgnoreCase(Config.AdColony)) {
            adColonyAd.onMainDestroy();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Unity) || modeAdsNative.equalsIgnoreCase(Config.Unity) || modeAdsInter.equalsIgnoreCase(Config.Unity) || modeAdsRewardVideo.equalsIgnoreCase(Config.Unity)) {
            unityAd.onMainDestroy();
        }

        if (modeAdsBanner.equalsIgnoreCase(Config.Vungle) || modeAdsNative.equalsIgnoreCase(Config.Vungle) || modeAdsInter.equalsIgnoreCase(Config.Vungle) || modeAdsRewardVideo.equalsIgnoreCase(Config.Vungle)) {
            vungleAd.onMainDestroy();
        }
    }


}
