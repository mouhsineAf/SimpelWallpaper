package com.devm.ads.companies;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyZone;
import com.devm.ads.Config;
import com.devm.ads.util.Data;

public class AdColonyAd { //Done
    private static final String TAG = "AdColonyAd";


    Activity activity = null;
    Data data;

    AdColonyAdView adView;
    AdColonyInterstitial interstitialAd;
    AdColonyAdOptions adOptions;
    AdColonyInterstitial rewardAd;
    AdColonyAdOptions adOptionsReward;

    public AdColonyAd(Activity activity, CallbackInitialization callbackInitialization) {
        this.activity = activity;
        this.data = new Data(activity);
        // Construct optional app options object to be sent with configure
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                // .setUserID("unique_user_id")
                .setKeepScreenOn(true)
               // .setTestModeEnabled(true)
                ;

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(activity, appOptions, data.getString(Config.AdColonyAppId, ""));

     //   callbackInitialization.onInitializationComplete();
    }

    public void createBanner(CallbackBanner callbackBanner) {

        AdColonyAdViewListener listener = new AdColonyAdViewListener() {
            @Override
            public void onRequestFilled(AdColonyAdView adColonyAdView) {
                Log.d(TAG, "onRequestFilled");
                adView = adColonyAdView;
                callbackBanner.onAdLoaded(adView);
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
                Log.d(TAG, "onRequestNotFilled");
                callbackBanner.onAdFailedToLoad("onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyAdView ad) {
                super.onOpened(ad);
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onClosed(AdColonyAdView ad) {
                super.onClosed(ad);
                Log.d(TAG, "onClosed");
            }

            @Override
            public void onClicked(AdColonyAdView ad) {
                super.onClicked(ad);
                Log.d(TAG, "onClicked");
            }

            @Override
            public void onLeftApplication(AdColonyAdView ad) {
                super.onLeftApplication(ad);
                Log.d(TAG, "onLeftApplication");
            }
        };
        AdColonyAdOptions adOptions = new AdColonyAdOptions();
        AdColony.requestAdView(data.getString(Config.AdColonyBannerUnitId, ""), listener, AdColonyAdSize.BANNER, adOptions);


    }

    public void show_banner_ad(final boolean show) {
        if (adView == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (show) {
                    if (adView.isEnabled()) {
                        adView.setEnabled(true);
                    }
                    if (adView.getVisibility() == View.INVISIBLE) {
                        adView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (adView.isEnabled()) { adView.setEnabled(false); }
                    if (adView.getVisibility() != View.INVISIBLE) {
                        adView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }


    public void createInterstitial(CallbackInterstitial callbackInterstitial) {

        // Ad specific options to be sent with request
        adOptions = new AdColonyAdOptions();

        // Set up listener for interstitial ad callbacks. You only need to implement the callbacks
        // that you care about. The only required callback is onRequestFilled, as this is the only
        // way to get an ad object.
        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                interstitialAd = ad;
                callbackInterstitial.onAdLoaded();
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                Log.d(TAG, "onRequestNotFilled");
                callbackInterstitial.onAdFailedToLoad("onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring
                callbackInterstitial.onAdClosed();
                Log.d(TAG, "onExpiring");
            }
        };
        AdColony.requestInterstitial(data.getString(Config.AdColonyInterUnitId, ""), listener, adOptions);


    }


    public void createRewardedVideo(CallbackRewardVideo callbackRewardVideo) {
        // Ad specific options to be sent with request
        adOptionsReward = new AdColonyAdOptions()
                .enableConfirmationDialog(true)
                .enableResultsDialog(true);

        // Create and set a reward listener
        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward reward) {
                // Query reward object for info here
                Log.d(TAG, "onReward");
                callbackRewardVideo.onAdRewarded(reward.success());
            }
        });

        // Set up listener for interstitial ad callbacks. You only need to implement the callbacks
        // that you care about. The only required callback is onRequestFilled, as this is the only
        // way to get an ad object.
        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
                rewardAd = ad;
                callbackRewardVideo.onAdLoaded();
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                Log.d(TAG, "onRequestNotFilled");
                callbackRewardVideo.onAdFailedToLoad("onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring
                Log.d(TAG, "onExpiring");
                callbackRewardVideo.onAdClosed();
            }
        };
        AdColony.requestInterstitial(data.getString(Config.AdColonyRewardUnitId, ""), listener, adOptionsReward);

    }



    public void show_interstitial_ad () {
        if (interstitialAd == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (interstitialAd.isExpired()) {
                    interstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                }

            }
        });
    }

    public void show_rewarded_video_ad () {
        if (rewardAd == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (rewardAd.isExpired()) {
                    rewardAd.show();
                } else {
                    Log.d(TAG, "The reward ad wasn't ready yet.");
                }

            }
        });

    }

    public boolean is_interstitial_ad_loaded() {
        return interstitialAd != null && !interstitialAd.isExpired();
    }

    public boolean is_rewarded_video_loaded() {
        return rewardAd != null && !rewardAd.isExpired();
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
        if (adView != null){
            adView.destroy();
        }
    }

    public void onMainResume() {
    }

    public void onMainDestroy() {
        if (adView != null){
            adView.destroy();
        }
    }

}
