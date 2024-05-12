package com.devm.ads.companies;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devm.ads.Config;
import com.devm.ads.R;
import com.devm.ads.util.Data;
import com.devm.ads.util.LayoutNativeAdConfig;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;


public class FacebookAd {
    private static final String TAG = "FacebookAd";

    Activity activity = null;
    Data data;

    AdView bannerAdView;
    InterstitialAd mInterstitialAd;
    NativeAd nativeAd;
    LinearLayout adView;
    RewardedVideoAd rewardedVideoAd;

    CallbackNative callbackNative;

    public FacebookAd(Activity activity, CallbackInitialization callbackInitialization) {
        this.activity = activity;
        this.data = new Data(activity);

        AudienceNetworkAds.initialize(activity);
       // AdSettings.addTestDevice("e36990fe-aad8-4bb2-b02d-b1245b45f77b");
       // AdSettings.setTestMode(true);
        new AudienceNetworkAds.InitListener() {
            @Override
            public void onInitialized(AudienceNetworkAds.InitResult initResult) {
                callbackInitialization.onInitializationComplete();
            }
        };

    }

    public void createBanner(CallbackBanner callbackBanner) {
        bannerAdView = new AdView(activity, data.getString(Config.FacebookBannerUnitId, ""), AdSize.BANNER_HEIGHT_50);

        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                callbackBanner.onAdFailedToLoad(adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                callbackBanner.onAdLoaded(bannerAdView);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        bannerAdView.loadAd(bannerAdView.buildLoadAdConfig().withAdListener(adListener).build());

    }

    public void show_banner_ad(final boolean show) {
        if (bannerAdView == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (show) {
                    if (bannerAdView.isEnabled()) {
                        bannerAdView.setEnabled(true);
                    }
                    if (bannerAdView.getVisibility() == View.INVISIBLE) {
                        bannerAdView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (bannerAdView.isEnabled()) { bannerAdView.setEnabled(false); }
                    if (bannerAdView.getVisibility() != View.INVISIBLE) {
                        bannerAdView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }


    public void createNative(LayoutNativeAdConfig layoutNativeAdConfig, CallbackNative callbackNative){
        this.callbackNative = callbackNative;
        nativeAd = new NativeAd(activity, data.getString(Config.FacebookNativeUnitId, ""));

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                callbackNative.onAdFailedToLoad(adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                // Race condition, load() called again before last ad was displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd, layoutNativeAdConfig);

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        };

        // Request an ad
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());


    }

    private void inflateAd(NativeAd nativeAd, LayoutNativeAdConfig layoutNativeAdConfig) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        NativeAdLayout nativeAdLayout = new NativeAdLayout(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        if (layoutNativeAdConfig.isNativeSmallStyleEnable()){
            adView = (LinearLayout) inflater.inflate(R.layout.layout_facebook_small_native_ad, nativeAdLayout, false);

        }else {
            adView = (LinearLayout) inflater.inflate(R.layout.layout_facebook_native_ad, nativeAdLayout, false);

        }
        nativeAdLayout.addView(adView);


        // Add the AdOptionsView
      //  LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
      // adChoicesContainer.removeAllViews();
      // adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
        //

       if (layoutNativeAdConfig != null) {

           if (layoutNativeAdConfig.getNativeCardBackground() != 0){
               //  adView.setBackgroundResource(layoutNativeAdConfig.getNativeCardBackground());
               //  adView.getBackground().clearColorFilter();

           }


           if (layoutNativeAdConfig.getActionAdButtonBackground() != 0){
               nativeAdCallToAction.setBackgroundResource(layoutNativeAdConfig.getActionAdButtonBackground());
           }
           if (layoutNativeAdConfig.getActionAdButtonBackgroundColor() != 0){
               nativeAdCallToAction.setBackgroundColor(layoutNativeAdConfig.getActionAdButtonBackgroundColor());
           }
           if (layoutNativeAdConfig.getNativeAdTextTitleColor() != 0){
               nativeAdTitle.setTextColor(layoutNativeAdConfig.getNativeAdTextTitleColor());
           }
           if (layoutNativeAdConfig.getNativeAdTitleSize() != 0){
               nativeAdTitle.setTextSize(layoutNativeAdConfig.getNativeAdTitleSize());
           }
           if (layoutNativeAdConfig.getNativeAdTextBodyColor() != 0){
               nativeAdBody.setTextColor(layoutNativeAdConfig.getNativeAdTextBodyColor());
           }
           if (layoutNativeAdConfig.getActionAdButtonTextColor() != 0){
               ((TextView) nativeAdCallToAction).setTextColor(layoutNativeAdConfig.getActionAdButtonTextColor());
           }
           if (layoutNativeAdConfig.getNativeAdTextBodyColor() != 0){
               nativeAdBody.setTextColor(layoutNativeAdConfig.getNativeAdTextBodyColor());
           }

           if (!layoutNativeAdConfig.isNativeAdMediaViewEnable()){
               nativeAdMedia.setVisibility(View.GONE);
           }

           if (!layoutNativeAdConfig.isNativeAdTextBodyEnable()){
               nativeAdMedia.setVisibility(View.GONE);
           }


       }

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);


        callbackNative.onAdLoaded(adOptionsView);

    }

    public void show_native_ad(final boolean show) {
        if (nativeAd == null) { return; }
    }

    public void createInterstitial(CallbackInterstitial callbackInterstitial) {
        mInterstitialAd = new InterstitialAd(activity, data.getString(Config.FacebookInterUnitId, ""));

        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                callbackInterstitial.onAdClosed();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                callbackInterstitial.onAdFailedToLoad(adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                callbackInterstitial.onAdLoaded();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        mInterstitialAd.loadAd(
                mInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }


    public void createRewardedVideo(CallbackRewardVideo callbackRewardVideo) {
        rewardedVideoAd = new RewardedVideoAd(activity, data.getString(Config.FacebookRewardedVideoUnitId, ""));
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage());
                callbackRewardVideo.onAdFailedToLoad(error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
                callbackRewardVideo.onAdLoaded();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                Log.d(TAG, "Rewarded video completed!");

                // Call method to give reward
                // giveReward();
                callbackRewardVideo.onAdRewarded(true);
            }

            @Override
            public void onRewardedVideoClosed() {
                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                Log.d(TAG, "Rewarded video ad closed!");
                callbackRewardVideo.onAdClosed();
            }
        };
        rewardedVideoAd.loadAd(
                rewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());

    }




    public void show_interstitial_ad () {
        if (mInterstitialAd == null) { return; }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (mInterstitialAd.isAdLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                }

            }
        });
    }

    public void show_rewarded_video_ad () {
        if (rewardedVideoAd == null) { return; }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (rewardedVideoAd.isAdLoaded()) {
                    rewardedVideoAd.show();
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });

    }

    public boolean is_interstitial_ad_loaded() {
        return mInterstitialAd != null && mInterstitialAd.isAdLoaded();
    }

    public boolean is_rewarded_video_loaded() {
        return rewardedVideoAd != null && rewardedVideoAd.isAdLoaded();
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
        if (rewardedVideoAd != null) {
            rewardedVideoAd.destroy();
            rewardedVideoAd = null;
        }
        if (mInterstitialAd != null){
            mInterstitialAd.destroy();
            mInterstitialAd = null;
        }
        if (bannerAdView != null){
            bannerAdView.destroy();
        }
        if (nativeAd != null){
            nativeAd.destroy();
        }
    }


}
