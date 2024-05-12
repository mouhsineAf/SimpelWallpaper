package com.devm.ads.companies;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.devm.ads.Config;
import com.devm.ads.util.Data;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;

public class UnityAd { //Done
    private static final String TAG = "UnityAd";


    Activity activity = null;
    Data data;

    IUnityAdsShowListener IUnityAdsShowListenerInter;
    IUnityAdsShowListener IUnityAdsShowListenerRewardVideo;

    final Boolean testMode = false;
    boolean adInterstitialAdLoaded = false;
    boolean adRewardedVideoAdLoaded = false;
    View bannerView;

    boolean unityAdInitialization = false;
    boolean bannerNotCreate = false;
    boolean interNotCreate = false;
    boolean rewardNotCreate = false;

    CallbackBanner callbackBanner;
    CallbackInterstitial callbackInterstitial;
    CallbackRewardVideo callbackRewardVideo;

    public UnityAd(Activity activity, CallbackInitialization callbackInitialization) {
        this.activity = activity;
        this.data = new Data(activity);

        if (!unityAdInitialization){
            UnityAds.initialize(activity, data.getString(Config.UnityAdGameId, ""), testMode, new IUnityAdsInitializationListener() {
                @Override
                public void onInitializationComplete() {
                    callbackInitialization.onInitializationComplete();

                    unityAdInitialization = true;
                    if (bannerNotCreate){
                        createBanner(callbackBanner);
                    }
                    if (interNotCreate){
                        createInterstitial(callbackInterstitial);
                    }
                    if (rewardNotCreate){
                        createRewardedVideo(callbackRewardVideo);
                    }
                }

                @Override
                public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                    callbackInitialization.onInitializationFailed(message);
                    unityAdInitialization = false;

                }
            });
        }


    }


    public void createBanner(CallbackBanner callbackBanner) {
        this.callbackBanner = callbackBanner;
        if (unityAdInitialization){
            UnityBanners.setBannerListener(new IUnityBannerListener() {
                @Override
                public void onUnityBannerLoaded (String placementId, View view) {
                    // When the banner content loads, add it to the view hierarchy:
                    bannerView = view;
                    callbackBanner.onAdLoaded(bannerView);
                }

                @Override
                public void onUnityBannerUnloaded (String placementId) {
                    // When the banner’s no longer in use, remove it from the view hierarchy:
                    bannerView = null;
                    callbackBanner.onAdFailedToLoad("unloaded");

                }

                @Override
                public void onUnityBannerShow (String placementId) {
                    // Called when the banner is first visible to the user.
                }

                @Override
                public void onUnityBannerClick (String placementId) {
                    // Called when the banner is clicked.
                }

                @Override
                public void onUnityBannerHide (String placementId) {
                    // Called when the banner is hidden from the user.
                }

                @Override
                public void onUnityBannerError (String message) {
                    // Called when an error occurred, and the banner failed to load or show.
                    callbackBanner.onAdFailedToLoad(message);

                }
            });
            //    UnityAds.setDebugMode(true);
            UnityBanners.loadBanner (activity, data.getString(Config.UnityAdBannerUnitId, ""));
        }else {
            bannerNotCreate = true;
        }

    }

    public void show_banner_ad(final boolean show) {
        if (bannerView == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (show) {
                    if (bannerView.isEnabled()) {
                        bannerView.setEnabled(true);
                    }
                    if (bannerView.getVisibility() == View.INVISIBLE) {
                        bannerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (bannerView.isEnabled()) { bannerView.setEnabled(false); }
                    if (bannerView.getVisibility() != View.INVISIBLE) {
                        bannerView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    public void createInterstitial(CallbackInterstitial callbackInterstitial) {
        this.callbackInterstitial = callbackInterstitial;
        if (unityAdInitialization){
            adInterstitialAdLoaded = false;
            IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String placementId) {
                    Log.e(TAG, "onUnityAdsAdLoaded");
                    callbackInterstitial.onAdLoaded();
                    adInterstitialAdLoaded = true;
                }

                @Override
                public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                    Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                    callbackInterstitial.onAdFailedToLoad(message);
                    adInterstitialAdLoaded = false;
                }
            };

            IUnityAdsShowListenerInter = new IUnityAdsShowListener() {
                @Override
                public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {

                }

                @Override
                public void onUnityAdsShowStart(String placementId) {

                }

                @Override
                public void onUnityAdsShowClick(String placementId) {

                }

                @Override
                public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                    Log.e(TAG, "onUnityAdsShowComplete");
                    callbackInterstitial.onAdClosed();
                    adInterstitialAdLoaded = false;
                }
            };


            UnityAds.load(data.getString(Config.UnityAdInterUnitId, ""), loadListener);

        }else {
            interNotCreate = true;
        }

    }


    public void createRewardedVideo(CallbackRewardVideo callbackRewardVideo) {
        this.callbackRewardVideo = callbackRewardVideo;
        if (unityAdInitialization){
            adRewardedVideoAdLoaded = false;

            IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
                @Override
                public void onUnityAdsAdLoaded(String placementId) {
                    callbackRewardVideo.onAdLoaded();
                    adRewardedVideoAdLoaded = true;
                }

                @Override
                public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                    Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                    callbackRewardVideo.onAdFailedToLoad(message);
                    adRewardedVideoAdLoaded = false;
                }
            };

            IUnityAdsShowListenerRewardVideo = new IUnityAdsShowListener() {
                @Override
                public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {

                }

                @Override
                public void onUnityAdsShowStart(String placementId) {

                }

                @Override
                public void onUnityAdsShowClick(String placementId) {

                }

                @Override
                public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                    adRewardedVideoAdLoaded = false;
                    callbackRewardVideo.onAdClosed();
                    if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
                        // Reward the user for watching the ad to completion
                        callbackRewardVideo.onAdRewarded(true);

                    } else {
                        // Do not reward the user for skipping the ad
                        callbackRewardVideo.onAdRewarded(false);

                    }

                }
            };

            //  UnityAds.setDebugMode(true);

            UnityAds.load(data.getString(Config.UnityAdRewardedVideoUnitId, ""), loadListener);
        }else {
            rewardNotCreate = true;
        }

    }




    public void show_interstitial_ad () {
        UnityAds.show(activity, data.getString(Config.UnityAdInterUnitId, ""), new UnityAdsShowOptions(), IUnityAdsShowListenerInter);
    }

    public void show_rewarded_video_ad () {
        UnityAds.show(activity, data.getString(Config.UnityAdRewardedVideoUnitId, ""), new UnityAdsShowOptions(), IUnityAdsShowListenerRewardVideo);
    }

    public boolean is_interstitial_ad_loaded() {
        return adInterstitialAdLoaded;
    }

    public boolean is_rewarded_video_loaded() {
        return adRewardedVideoAdLoaded;
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

    public void onMainPause() {
        UnityBanners.destroy();
    }

    public void onMainResume() {
    }

    public void onMainDestroy() {
        UnityBanners.destroy();
    }
}
