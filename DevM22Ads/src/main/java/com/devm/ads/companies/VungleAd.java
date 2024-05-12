package com.devm.ads.companies;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.devm.ads.util.Data;
import com.vungle.warren.AdConfig;
import com.vungle.warren.BannerAdConfig;
import com.vungle.warren.Banners;
import com.vungle.warren.InitCallback;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleBanner;
import com.vungle.warren.error.VungleException;

public class VungleAd { // Done
    private static final String TAG = "VungleAd";


    Activity activity = null;
    Data data;

    VungleBanner vungleBanner;
    PlayAdCallback vunglePlayInterAdCallback;
    PlayAdCallback vunglePlayRewardAdCallback;

    boolean interAdIsLoaded = false;
    boolean rewardAdIsLoaded = false;
    boolean vungleAdInitialization = false;
    boolean bannerNotCreate = false;
    boolean interNotCreate = false;
    boolean rewardNotCreate = false;

    CallbackBanner callbackBanner;
    CallbackInterstitial callbackInterstitial;
    CallbackRewardVideo callbackRewardVideo;

    public VungleAd(Activity activity, CallbackInitialization callbackInitialization) {
        this.activity = activity;
        this.data = new Data(activity);

        if (!vungleAdInitialization){
            Vungle.init("62ff6558e285e936f4a73f8d", activity.getApplicationContext(), new InitCallback() {
                @Override
                public void onSuccess() {
                    // SDK has successfully initialized
                    Log.e(TAG, "onSuccess: True");
                    callbackInitialization.onInitializationComplete();

                    vungleAdInitialization = true;
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
                public void onError(VungleException exception) {
                    // SDK has failed to initialize
                    Log.e(TAG, "onError: " + exception.toString());
                    vungleAdInitialization = false;
                    callbackInitialization.onInitializationFailed(exception.getMessage());
                }

                @Override
                public void onAutoCacheAdAvailable(String placementId) {
                    // Ad has become available to play for a cache optimized placement
                }
            });
        }


    }

    public void createBanner(CallbackBanner callbackBanner) {
        this.callbackBanner = callbackBanner;
        if (vungleAdInitialization){
            final BannerAdConfig bannerAdConfig = new BannerAdConfig();
            bannerAdConfig.setAdSize(AdConfig.AdSize.BANNER);

            LoadAdCallback vungleLoadAdCallback = new LoadAdCallback() {
                @Override
                public void onAdLoad(String id) {
                    // Ad has been successfully loaded for the placement
                    if (Banners.canPlayAd("BANNER-9634933", bannerAdConfig.getAdSize())) {
                        vungleBanner = Banners.getBanner("BANNER-9634933", bannerAdConfig, null);
                        callbackBanner.onAdLoaded(vungleBanner);
                    }
                }

                @Override
                public void onError(String id, VungleException exception) {
                    // Ad has failed to load for the placement
                    callbackBanner.onAdFailedToLoad(exception.toString());
                }
            };

            Banners.loadBanner("BANNER-9634933", bannerAdConfig, vungleLoadAdCallback);
            Banners.canPlayAd("BANNER-9634933", bannerAdConfig.getAdSize());
        }else {
            bannerNotCreate = true;
        }

    }

    public void show_banner_ad(final boolean show) {
        if (vungleBanner == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (show) {
                    if (vungleBanner.isEnabled()) {
                        vungleBanner.setEnabled(true);
                    }
                    if (vungleBanner.getVisibility() == View.INVISIBLE) {
                        vungleBanner.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (vungleBanner.isEnabled()) { vungleBanner.setEnabled(false); }
                    if (vungleBanner.getVisibility() != View.INVISIBLE) {
                        vungleBanner.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }


    public void createInterstitial(CallbackInterstitial callbackInterstitial) {
        this.callbackInterstitial = callbackInterstitial;
        if (vungleAdInitialization){
            interAdIsLoaded = false;
            vunglePlayInterAdCallback = new PlayAdCallback() {
                @Override
                public void creativeId(String creativeId) {

                }

                @Override
                public void onAdStart(String placementId) {
                    interAdIsLoaded = false;
                }

                @Override
                public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {
                }

                @Override
                public void onAdEnd(String placementId) {
                    callbackInterstitial.onAdClosed();
                }

                @Override
                public void onAdClick(String placementId) {

                }

                @Override
                public void onAdRewarded(String placementId) {

                }

                @Override
                public void onAdLeftApplication(String placementId) {

                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    callbackInterstitial.onAdFailedToLoad(exception.getMessage());
                    interAdIsLoaded = false;
                }

                @Override
                public void onAdViewed(String placementId) {

                }
            };

            // Load Ad Implementation
            Vungle.loadAd("DEFAULT-6518033", new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementReferenceId) {
                    interAdIsLoaded = true;
                    callbackInterstitial.onAdLoaded();
                }

                @Override
                public void onError(String placementReferenceId, VungleException exception) {
                    interAdIsLoaded = false;
                    callbackInterstitial.onAdFailedToLoad(exception.getMessage());
                }
            });

        }else {
            interNotCreate = true;
        }

    }


    public void createRewardedVideo(CallbackRewardVideo callbackRewardVideo) {
        this.callbackRewardVideo = callbackRewardVideo;
        if (vungleAdInitialization){
            vunglePlayRewardAdCallback = new PlayAdCallback() {
                @Override
                public void creativeId(String creativeId) {

                }

                @Override
                public void onAdStart(String placementId) {
                    rewardAdIsLoaded = false;
                }

                @Override
                public void onAdEnd(String placementId, boolean completed, boolean isCTAClicked) {
                    callbackRewardVideo.onAdRewarded(completed);

                }

                @Override
                public void onAdEnd(String placementId) {
                    callbackRewardVideo.onAdClosed();
                }

                @Override
                public void onAdClick(String placementId) {

                }

                @Override
                public void onAdRewarded(String placementId) {

                }

                @Override
                public void onAdLeftApplication(String placementId) {

                }

                @Override
                public void onError(String placementId, VungleException exception) {
                    rewardAdIsLoaded = false;
                    callbackRewardVideo.onAdFailedToLoad(exception.getMessage());
                }

                @Override
                public void onAdViewed(String placementId) {

                }
            };

            // Load Ad Implementation
            Vungle.loadAd("DEFAULT-6518033", new LoadAdCallback() {
                @Override
                public void onAdLoad(String placementReferenceId) {
                    rewardAdIsLoaded = true;
                    callbackRewardVideo.onAdLoaded();
                }

                @Override
                public void onError(String placementReferenceId, VungleException exception) {
                    rewardAdIsLoaded = false;
                    callbackRewardVideo.onAdFailedToLoad(exception.getMessage());
                }
            });
        }else {
            rewardNotCreate = true;
        }

    }


    public void show_interstitial_ad () {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AdConfig adConfig = new AdConfig();
                adConfig.setAdOrientation(AdConfig.AUTO_ROTATE);
                adConfig.setMuted(true);

                if (interAdIsLoaded) {
                    Vungle.playAd("DEFAULT-6518033", adConfig, vunglePlayInterAdCallback);
                }else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

            }
        });
    }

    public void show_rewarded_video_ad () {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AdConfig adConfig = new AdConfig();
                adConfig.setAdOrientation(AdConfig.AUTO_ROTATE);
                adConfig.setMuted(true);

                if (rewardAdIsLoaded) {
                    Vungle.playAd("DEFAULT-6518033", adConfig, vunglePlayRewardAdCallback);
                }else {
                    Log.d("TAG", "The reward ad wasn't ready yet.");
                }

            }
        });

    }

    public boolean is_interstitial_ad_loaded() {
        return interAdIsLoaded;
    }

    public boolean is_rewarded_video_loaded() {
        return rewardAdIsLoaded;
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

    public void onMainPause() {
        if (vungleBanner != null){
            vungleBanner.destroyAd();
        }
    }

    public void onMainResume() {
    }

    public void onMainDestroy() {
        if (vungleBanner != null){
            vungleBanner.destroyAd();
        }

    }


}
