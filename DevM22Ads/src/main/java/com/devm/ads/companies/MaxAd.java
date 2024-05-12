package com.devm.ads.companies;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdRevenueListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.devm.ads.Config;
import com.devm.ads.R;
import com.devm.ads.util.Data;
import com.devm.ads.util.LayoutNativeAdConfig;


public class MaxAd {
    private static final String TAG = "MaxAd";

    Activity activity = null;
    Data data;

    MaxAdView adView;
    MaxNativeAdLoader nativeAdLoader;
    com.applovin.mediation.MaxAd loadedNativeAd;
    MaxInterstitialAd interstitialAd;
    MaxRewardedAd rewardedAd;


    public MaxAd(Activity activity, CallbackInitialization callbackInitialization) {
        this.activity = activity;
        this.data = new Data(activity);

        AppLovinSdk.getInstance(activity).setMediationProvider("max");
        //  AppLovinSdk.getInstance( activity ).getSettings().setCreativeDebuggerEnabled( true );
        //  AppLovinSdk.getInstance( activity ).showMediationDebugger();
        AppLovinSdk.initializeSdk(activity, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration){
                // AppLovin SDK is initialized, start loading ads

                callbackInitialization.onInitializationComplete();

            }
        });
    }


    public void createBanner(CallbackBanner callbackBanner) {
        adView = new MaxAdView(data.getString(Config.MaxBannerUnitId, ""), activity);
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdLoaded(com.applovin.mediation.MaxAd ad) {
                callbackBanner.onAdLoaded(adView);
            }

            @Override
            public void onAdDisplayed(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdHidden(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdClicked(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                callbackBanner.onAdFailedToLoad(error.toString());
            }

            @Override
            public void onAdDisplayFailed(com.applovin.mediation.MaxAd ad, MaxError error) {

            }
        });

        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;

        // Banner height on phones and tablets is 50 and 90, respectively
        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.banner_height);

        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );

        // Set background or background color for banners to be fully functional
        adView.setBackgroundColor(activity.getResources().getColor(R.color.white));
        adView.setEnabled(false);
        adView.setVisibility(View.INVISIBLE);

        // Load the ad
        adView.loadAd();
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


    public void createNative(LayoutNativeAdConfig layoutNativeAdConfig, CallbackNative callbackNative){
        nativeAdLoader = new MaxNativeAdLoader(data.getString(Config.MaxNativeUnitId, ""), activity);
        nativeAdLoader.setRevenueListener(new MaxAdRevenueListener() {
            @Override
            public void onAdRevenuePaid(com.applovin.mediation.MaxAd ad) {

            }
        });
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, com.applovin.mediation.MaxAd maxAd) {
                super.onNativeAdLoaded(maxNativeAdView, maxAd);
                if ( loadedNativeAd != null )
                {
                    nativeAdLoader.destroy( loadedNativeAd );
                }

                // Save ad for cleanup.
                loadedNativeAd = maxAd;
                callbackNative.onAdLoaded(maxNativeAdView);

            }

            @Override
            public void onNativeAdLoadFailed(String s, MaxError maxError) {
                super.onNativeAdLoadFailed(s, maxError);
                callbackNative.onAdFailedToLoad(maxError.toString());
            }

            @Override
            public void onNativeAdClicked(com.applovin.mediation.MaxAd maxAd) {
                super.onNativeAdClicked(maxAd);
            }
        });

        nativeAdLoader.loadAd(createNativeAdView(layoutNativeAdConfig));

    }

    private MaxNativeAdView createNativeAdView(LayoutNativeAdConfig layoutNativeAdConfig) {
        MaxNativeAdViewBinder binder;
        if (layoutNativeAdConfig.isNativeSmallStyleEnable()){
            binder = new MaxNativeAdViewBinder.Builder(R.layout.layout_max_small_native_ad)
                    .setTitleTextViewId( R.id.title_text_view )
                    .setBodyTextViewId( R.id.body_text_view )
                    .setAdvertiserTextViewId( R.id.advertiser_textView )
                    .setIconImageViewId( R.id.icon_image_view )
                    //      .setMediaContentViewId( R.id.media_view_container )
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    //  .setOptionsContentViewId( R.id.options_view )
                    .setCallToActionButtonId( R.id.cta_button )
                    .build();
        }else {
            binder = new MaxNativeAdViewBinder.Builder(R.layout.layout_max_native_ad)
                    .setTitleTextViewId( R.id.title_text_view )
                    .setBodyTextViewId( R.id.body_text_view )
                    .setAdvertiserTextViewId( R.id.advertiser_textView )
                    .setIconImageViewId( R.id.icon_image_view )
                    //      .setMediaContentViewId( R.id.media_view_container )
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    //  .setOptionsContentViewId( R.id.options_view )
                    .setCallToActionButtonId( R.id.cta_button )
                    .build();
        }



        return new MaxNativeAdView(binder, activity);
    }


    public void show_native_ad(final boolean show) {
        if (nativeAdLoader == null) { return; }
    }


    public void createInterstitial(CallbackInterstitial callbackInterstitial) {
        interstitialAd = new MaxInterstitialAd(data.getString(Config.MaxInterUnitId, ""), activity);
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(com.applovin.mediation.MaxAd ad) {
                callbackInterstitial.onAdLoaded();
            }

            @Override
            public void onAdDisplayed(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdHidden(com.applovin.mediation.MaxAd ad) {
                callbackInterstitial.onAdClosed();
            }

            @Override
            public void onAdClicked(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.e(TAG, "onAdLoadFailed: " + error.toString());
                callbackInterstitial.onAdFailedToLoad(error.toString());
            }

            @Override
            public void onAdDisplayFailed(com.applovin.mediation.MaxAd ad, MaxError error) {

            }
        });

        // Load the first ad
        interstitialAd.loadAd();
    }


    public void createRewardedVideo(CallbackRewardVideo callbackRewardVideo) {
        rewardedAd = MaxRewardedAd.getInstance(data.getString(Config.MaxRewardedVideoUnitId, ""), activity);
        rewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onRewardedVideoStarted(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onRewardedVideoCompleted(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onUserRewarded(com.applovin.mediation.MaxAd ad, MaxReward reward) {
                callbackRewardVideo.onAdRewarded(true);
            }

            @Override
            public void onAdLoaded(com.applovin.mediation.MaxAd ad) {
                callbackRewardVideo.onAdLoaded();
            }

            @Override
            public void onAdDisplayed(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdHidden(com.applovin.mediation.MaxAd ad) {
                callbackRewardVideo.onAdClosed();
            }

            @Override
            public void onAdClicked(com.applovin.mediation.MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                callbackRewardVideo.onAdFailedToLoad(error.toString());
            }

            @Override
            public void onAdDisplayFailed(com.applovin.mediation.MaxAd ad, MaxError error) {

            }
        });

        rewardedAd.loadAd();
    }




    public void show_interstitial_ad () {
        if (interstitialAd == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (interstitialAd.isReady()) {
                    interstitialAd.showAd();

                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

            }
        });
    }

    public void show_rewarded_video_ad () {
        if (rewardedAd == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (rewardedAd.isReady()) {
                    rewardedAd.showAd();

                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });

    }

    public boolean is_interstitial_ad_loaded() {
        if (interstitialAd == null){
            return false;
        }
        return interstitialAd.isReady();
    }

    public boolean is_rewarded_video_loaded() {
        if (rewardedAd == null){
            return false;
        }
        return rewardedAd.isReady();
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
    }
}
