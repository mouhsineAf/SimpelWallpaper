package com.devm.ads.companies;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Mediation;
import com.chartboost.sdk.ads.Banner;
import com.chartboost.sdk.ads.Interstitial;
import com.chartboost.sdk.ads.Rewarded;
import com.chartboost.sdk.callbacks.BannerCallback;
import com.chartboost.sdk.callbacks.InterstitialCallback;
import com.chartboost.sdk.callbacks.RewardedCallback;
import com.chartboost.sdk.callbacks.StartCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.DismissEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.RewardEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.chartboost.sdk.events.StartError;
import com.devm.ads.Config;
import com.devm.ads.util.Data;

public class ChartBoostAd { //Done
    private static final String TAG = "ChartBoostAd";

    Activity activity = null;
    Data data;

    Banner chartboostBanner;
    Interstitial chartboostInterstitial;
    Rewarded chartboostRewarded;

    boolean chartboostAdInitialization = false;
    boolean bannerNotCreate = false;
    boolean interNotCreate = false;
    boolean rewardNotCreate = false;

    CallbackBanner callbackBanner;
    CallbackInterstitial callbackInterstitial;
    CallbackRewardVideo callbackRewardVideo;

    public ChartBoostAd(Activity activity, CallbackInitialization callbackInitialization) {
        this.activity = activity;
        this.data = new Data(activity);

        if (!chartboostAdInitialization){
            Chartboost.startWithAppId(activity, data.getString(Config.CharBoostAppId, ""), data.getString(Config.CharBoostAppSignature, ""), new StartCallback() {
                @Override
                public void onStartCompleted(@Nullable StartError startError) {
                    callbackInitialization.onInitializationComplete();
                    chartboostAdInitialization = true;
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
            });
        }


    }

    public void createBanner(CallbackBanner callbackBanner) {
        this.callbackBanner = callbackBanner;
        if (chartboostAdInitialization){
            Mediation mediation = new Mediation("Mediation", "1.0.0", "1.0.0.1");
            chartboostBanner = new Banner(activity, "location", Banner.BannerSize.STANDARD, new BannerCallback() {
                @Override
                public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
                    if (cacheError == null){
                        chartboostBanner.show();
                        callbackBanner.onAdLoaded(chartboostBanner);
                    }else {
                        callbackBanner.onAdFailedToLoad(cacheError.toString());
                    }
                }

                @Override
                public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

                }

                @Override
                public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

                }

                @Override
                public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

                }

                @Override
                public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

                }
            }, mediation);
            chartboostBanner.cache();
        }else {
            bannerNotCreate = true;
        }


    }

    public void show_banner_ad(final boolean show) {
        if (chartboostBanner == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (show) {
                    if (chartboostBanner.isEnabled()) {
                        chartboostBanner.setEnabled(true);
                    }
                    if (chartboostBanner.getVisibility() == View.INVISIBLE) {
                        chartboostBanner.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (chartboostBanner.isEnabled()) { chartboostBanner.setEnabled(false); }
                    if (chartboostBanner.getVisibility() != View.INVISIBLE) {
                        chartboostBanner.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }



    public void createInterstitial(CallbackInterstitial callbackInterstitial) {
        this.callbackInterstitial = callbackInterstitial;
        if (chartboostAdInitialization){
            Mediation mediation = new Mediation("Mediation", "1.0.0", "1.0.0.1");
            chartboostInterstitial = new Interstitial("location", new InterstitialCallback() {
                @Override
                public void onAdDismiss(@NonNull DismissEvent dismissEvent) {
                    callbackInterstitial.onAdClosed();
                }

                @Override
                public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
                    if (cacheError == null){
                        callbackInterstitial.onAdLoaded();
                    }else {
                        callbackInterstitial.onAdFailedToLoad(cacheError.toString());
                    }
                }

                @Override
                public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

                }

                @Override
                public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

                }

                @Override
                public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

                }

                @Override
                public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

                }
            }, mediation);
            chartboostInterstitial.cache();
        }else {
            interNotCreate = true;
        }

    }


    public void createRewardedVideo(CallbackRewardVideo callbackRewardVideo) {
        this.callbackRewardVideo = callbackRewardVideo;
        if (chartboostAdInitialization){
            Mediation mediation = new Mediation("Mediation", "1.0.0", "1.0.0.1");
            chartboostRewarded = new Rewarded("location", new RewardedCallback() {
                @Override
                public void onRewardEarned(@NonNull RewardEvent rewardEvent) {
                    callbackRewardVideo.onAdRewarded(true);
                }

                @Override
                public void onAdDismiss(@NonNull DismissEvent dismissEvent) {
                    callbackRewardVideo.onAdClosed();
                }

                @Override
                public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
                    if (cacheError == null){
                        callbackRewardVideo.onAdLoaded();
                    }else {
                        callbackRewardVideo.onAdFailedToLoad(cacheError.toString());
                    }
                }

                @Override
                public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

                }

                @Override
                public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

                }

                @Override
                public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

                }

                @Override
                public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

                }
            }, mediation);
            chartboostRewarded.cache();
        }else {
            rewardNotCreate = true;
        }

    }




    public void show_interstitial_ad () {
        if (chartboostInterstitial == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (chartboostInterstitial.isCached()) { // check is cached is not mandatory
                    chartboostInterstitial.show();

                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

            }
        });
    }

    public void show_rewarded_video_ad () {
        if (chartboostRewarded == null) { return; }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (chartboostRewarded.isCached()) { // check is cached is not mandatory
                    chartboostRewarded.show();

                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });

    }

    public boolean is_interstitial_ad_loaded() {
        return chartboostInterstitial != null && chartboostInterstitial.isCached();
    }

    public boolean is_rewarded_video_loaded() {
        return chartboostRewarded != null && chartboostRewarded.isCached();
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
    }

    public void onMainResume() {
    }

    public void onMainDestroy() {
        if (chartboostBanner != null){
            chartboostBanner.detach();
        }
    }

}
