package com.devm.ads.companies;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.devm.ads.Config;
import com.devm.ads.util.Data;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InitializationListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

public class IronSourceAd {
    private static final String TAG = "IronSourceAd";

    Activity activity = null;
    Data data;

    IronSourceBannerLayout banner;


    public IronSourceAd(Activity activity, CallbackInitialization callbackInitialization) {
        this.activity = activity;
        this.data = new Data(activity);

        IronSource.init(activity, data.getString(Config.IronSourceAppId, ""), new InitializationListener() {
            @Override
            public void onInitializationComplete() {
                callbackInitialization.onInitializationComplete();
            }
        }, IronSource.AD_UNIT.INTERSTITIAL, IronSource.AD_UNIT.REWARDED_VIDEO, IronSource.AD_UNIT.BANNER);

    }

    public void createBanner(CallbackBanner callbackBanner) {
        banner = IronSource.createBanner(activity, ISBannerSize.BANNER);
        banner.setBannerListener(new BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                callbackBanner.onAdLoaded(banner);
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
                callbackBanner.onAdFailedToLoad(ironSourceError.toString());
            }

            @Override
            public void onBannerAdClicked() {

            }

            @Override
            public void onBannerAdScreenPresented() {

            }

            @Override
            public void onBannerAdScreenDismissed() {

            }

            @Override
            public void onBannerAdLeftApplication() {

            }
        });
       // IronSource.loadBanner(banner);
        IronSource.loadBanner(banner, data.getString(Config.IronSourceBannerUnitId, ""));

    }

    public void show_banner_ad(final boolean show) {
        if (banner == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (show) {
                    if (banner.isEnabled()) {
                        banner.setEnabled(true);
                    }
                    if (banner.getVisibility() == View.INVISIBLE) {
                        banner.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (banner.isEnabled()) { banner.setEnabled(false); }
                    if (banner.getVisibility() != View.INVISIBLE) {
                        banner.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }


    public void createInterstitial(CallbackInterstitial callbackInterstitial) {
        IronSource.setInterstitialListener(new InterstitialListener() {
            /**
             * Invoked when Interstitial Ad is ready to be shown after load function was called.
             */
            @Override
            public void onInterstitialAdReady() {
                Log.e(TAG, "onInterstitialAdReady");
                callbackInterstitial.onAdLoaded();
            }
            /**
             * invoked when there is no Interstitial Ad available after calling load function.
             */
            @Override
            public void onInterstitialAdLoadFailed(IronSourceError error) {
                callbackInterstitial.onAdFailedToLoad(error.toString());
            }
            /**
             * Invoked when the Interstitial Ad Unit is opened
             */
            @Override
            public void onInterstitialAdOpened() {
            }
            /*
             * Invoked when the ad is closed and the user is about to return to the application.
             */
            @Override
            public void onInterstitialAdClosed() {
                callbackInterstitial.onAdClosed();
            }
            /**
             * Invoked when Interstitial ad failed to show.
             * @param error - An object which represents the reason of showInterstitial failure.
             */
            @Override
            public void onInterstitialAdShowFailed(IronSourceError error) {
            }
            /*
             * Invoked when the end user clicked on the interstitial ad, for supported networks only.
             */
            @Override
            public void onInterstitialAdClicked() {
            }
            /** Invoked right before the Interstitial screen is about to open.
             *  NOTE - This event is available only for some of the networks.
             *  You should NOT treat this event as an interstitial impression, but rather use InterstitialAdOpenedEvent
             */
            @Override
            public void onInterstitialAdShowSucceeded() {
            }
        });

        IronSource.loadInterstitial();
    }


    public void createRewardedVideo(CallbackRewardVideo callbackRewardVideo) {
        IronSource.setRewardedVideoListener(new RewardedVideoListener() {
            /**
             * Invoked when the RewardedVideo ad view has opened.
             * Your Activity will lose focus. Please avoid performing heavy
             * tasks till the video ad will be closed.
             */
            @Override
            public void onRewardedVideoAdOpened() {
            }
            /*Invoked when the RewardedVideo ad view is about to be closed.
            Your activity will now regain its focus.*/
            @Override
            public void onRewardedVideoAdClosed() {
                callbackRewardVideo.onAdClosed();
            }
            /**
             * Invoked when there is a change in the ad availability status.
             *
             * @param - available - value will change to true when rewarded videos are *available.
             *          You can then show the video by calling showRewardedVideo().
             *          Value will change to false when no videos are available.
             */
            @Override
            public void onRewardedVideoAvailabilityChanged(boolean available) {
                //Change the in-app 'Traffic Driver' state according to availability.
                if (available) {
                    callbackRewardVideo.onAdLoaded();
                }
            }
            /**
             /**
             * Invoked when the user completed the video and should be rewarded.
             * If using server-to-server callbacks you may ignore this events and wait *for the callback from the ironSource server.
             *
             * @param - placement - the Placement the user completed a video from.
             */
            @Override
            public void onRewardedVideoAdRewarded(Placement placement) {
                /** here you can reward the user according to the given amount.
                 String rewardName = placement.getRewardName();
                 int rewardAmount = placement.getRewardAmount();
                 */
                callbackRewardVideo.onAdRewarded(true);
            }
            /* Invoked when RewardedVideo call to show a rewarded video has failed
             * IronSourceError contains the reason for the failure.
             */
            @Override
            public void onRewardedVideoAdShowFailed(IronSourceError error) {
                callbackRewardVideo.onAdFailedToLoad(error.toString());
            }
            /*Invoked when the end user clicked on the RewardedVideo ad
             */
            @Override
            public void onRewardedVideoAdClicked(Placement placement){
            }
            @Override
            public void onRewardedVideoAdStarted(){
            }
            /* Invoked when the video ad finishes plating. */
            @Override
            public void onRewardedVideoAdEnded(){
                callbackRewardVideo.onAdClosed();
            }
        });
    }




    public void show_interstitial_ad () {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (is_interstitial_ad_loaded()) {
                    IronSource.showInterstitial(data.getString(Config.IronSourceInterUnitId, ""));
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

            }
        });
    }

    public void show_rewarded_video_ad () {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (is_rewarded_video_loaded()) {
                    IronSource.showRewardedVideo(data.getString(Config.IronSourceRewardVideoUnitId, ""));
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });

    }

    public boolean is_interstitial_ad_loaded() {
        return IronSource.isInterstitialReady();
    }

    public boolean is_rewarded_video_loaded() {
        return IronSource.isRewardedVideoAvailable();
    }


    //interfaces
    public interface CallbackInitialization {
        void onInitializationComplete();
        void onInitializationFailed(String error);
    }

    public interface CallbackBanner {
        void onAdLoaded(FrameLayout adView);
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
        IronSource.onPause(activity);
        IronSource.destroyBanner(banner);

    }

    public void onMainResume() {
        IronSource.onResume(activity);
    }

    public void onMainDestroy() {
        IronSource.destroyBanner(banner);
    }
}
