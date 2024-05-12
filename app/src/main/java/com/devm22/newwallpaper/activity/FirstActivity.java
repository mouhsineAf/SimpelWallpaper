package com.devm22.newwallpaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.devm.ads.MyAds;
import com.devm.ads.util.LayoutNativeAdConfig;
import com.devm22.newwallpaper.Config;
import com.devm22.newwallpaper.R;
import com.devm22.newwallpaper.utils.SharedApp;
import com.nostra13.universalimageloader.BuildConfig;
import com.squareup.picasso.Picasso;

public class FirstActivity extends AppCompatActivity {

    FrameLayout layoutNativeAds;
    Button btnStart, btnShareApp;

    MyAds myAds;

    SharedApp shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        layoutNativeAds = findViewById(R.id.layout_native_ads);
        btnStart = findViewById(R.id.btnStart);
        btnShareApp = findViewById(R.id.btnShareApp);

        shared = new SharedApp(this);

        checkUpdate();
        intAds();
        buildInterAd();
        buildNativeAd();

        btnStart.setOnClickListener(view -> {
            myAds.showDialogInterstitialAd(4, new MyAds.CallbackDialogLoadingInterstitial() {
                @Override
                public void onFinish(Boolean isLoaded) {
                    if (isLoaded){
                        myAds.showInterstitialAd();
                    }else {
                        start();
                    }
                }
            });
        });

        btnShareApp.setOnClickListener(view -> {
            shareApp();
        });



    }

    public void shareApp(){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }


    private void checkUpdate(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            bundle.clear();
            if (shared.getBoolean(Config.UpdateEnable, false)){
                showDialogUpdate();
            }
        }

    }

    private void showDialogUpdate() {
        Dialog dialog = new Dialog(FirstActivity.this, R.style.full_screen_dialog);
        dialog.setContentView(R.layout.dialog_update);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        }
        dialog.setCancelable(false);

        ImageView imageUpdate = dialog.findViewById(R.id.update_icon);
        TextView textTitle = dialog.findViewById(R.id.update_title);
        TextView textDescription = dialog.findViewById(R.id.update_description);
        Button btnAction = dialog.findViewById(R.id.btnUpdate);
        ImageView btnCancer = dialog.findViewById(R.id.btnUpdateCancel);

        String updateIcon = shared.getString(Config.UpdateIcon, "");
        String updateTitle = shared.getString(Config.UpdateTitle, "");
        String updateDescription = shared.getString(Config.UpdateDescription, "");
        String updateActionText = shared.getString(Config.UpdateButtonText, "");
        String updateUrl = shared.getString(Config.UpdateUrl, "");
        boolean updateCancelEnable = shared.getBoolean(Config.UpdateEnableClosed, true);


        if (updateIcon.isEmpty()){
            imageUpdate.setImageResource(R.drawable.ic_update);
        }else {
            Picasso.get().load(updateIcon).into(imageUpdate);
        }

        if (updateTitle.isEmpty()){
            textTitle.setText(getResources().getString(R.string.text_update_title));
        }else {
            textTitle.setText(updateTitle);
        }

        if (updateDescription.isEmpty()){
            textDescription.setText(getResources().getString(R.string.text_update_description));
        }else {
            textDescription.setText(updateDescription);
        }

        if (updateActionText.isEmpty()){
            btnAction.setText(getResources().getString(R.string.text_update_action));
        }else {
            btnAction.setText(updateActionText);
        }

        if (!updateCancelEnable){
            btnCancer.setEnabled(false);
            btnCancer.setVisibility(View.INVISIBLE);
        }


        btnCancer.setOnClickListener(view -> {
            dialog.dismiss();
        });

        btnAction.setOnClickListener(view -> {
            if (updateCancelEnable){
                dialog.dismiss();
            }
            if (!updateUrl.isEmpty()){
                if (updateUrl.contains("play.google.com/store/apps/details?id=")){
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + updateUrl.split("id=")[1])));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)));
                    }
                }else if (updateUrl.contains("https://") || updateUrl.contains("http://")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                    startActivity(browserIntent);
                }
            }
        });

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
        btnAction.startAnimation(animation);

        dialog.show();
    }

    //inti ads
    private void intAds() {
        myAds = new MyAds(FirstActivity.this, new MyAds.CallbackInitialization() {
            @Override
            public void onInitializationComplete() {

            }

            @Override
            public void onInitializationFailed(String error) {

            }
        });
    }

    //build ads
    private void buildNativeAd(){
        //create native ad
        LayoutNativeAdConfig layoutNativeAdConfig = new LayoutNativeAdConfig();
        layoutNativeAdConfig.setNativeAdTextTitleColor(R.color.colorPrimary);

        myAds.createNativeAd(layoutNativeAdConfig, new MyAds.CallbackNative() {
            @Override
            public void onAdLoaded(View nativeAdView) {
                layoutNativeAds.removeAllViews();
                layoutNativeAds.addView(nativeAdView);
            }

            @Override
            public void onAdFailedToLoad(String error) {

            }
        });
        myAds.showNativeAd(true);
    }

    private void buildInterAd(){
        //create inter ad
        myAds.createInterstitialAd(new MyAds.CallbackInterstitial() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {
                start();
            }

            @Override
            public void onAdFailedToLoad(String error) {

            }
        });

    }

    //Start next Activity
    private void start(){
        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        myAds.onAdResume();
        super.onResume();
        buildInterAd();
    }

    @Override
    protected void onPause() {
        myAds.onAdPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        myAds.onAdDestroy();
        super.onDestroy();
    }

}