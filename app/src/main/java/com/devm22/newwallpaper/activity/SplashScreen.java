package com.devm22.newwallpaper.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devm.ads.util.Data;
import com.devm22.newwallpaper.Config;
import com.devm22.newwallpaper.R;
import com.devm22.newwallpaper.utils.SharedApp;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";

    SharedApp sharedApp;
    private boolean dataIsLoaded = false;
    private boolean timeIsFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        ShapeableImageView logo = findViewById(R.id.logoInSplash);
        logo.setAnimation(animation);

        int duration = 3000;
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion == 13) duration = 0;

        sharedApp = new SharedApp(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                timeIsFinish = true;
                if (dataIsLoaded){
                    open();
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (checkInternet()){

                            }
                        }
                    });

                }
            }
        }, duration);

        loadAllData();

    }

    public boolean checkInternet(){
        ConnectivityManager connectivityManager = null;
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_no_wifi, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            Button button = dialogView.findViewById(R.id.retry);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    loadAllData();
                }
            });
            dialog.show();
            return false;
        }
        return true;
    }

    private void open(){
        Intent intent = new Intent(SplashScreen.this, FirstActivity.class);
        intent.putExtra("UPDATE_STATE", true);
        startActivity(intent);
        finish();

    }

    public void loadAllData(){
        RequestQueue rq = Volley.newRequestQueue(SplashScreen.this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Config.jsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject mData = jsonObj.getJSONObject("config");

                            Data data = new Data(SplashScreen.this);

                            String ModeAdsBanner = mData.getString(Config.ModeAdsBanner);
                            String ModeAdsNative = mData.getString(Config.ModeAdsNative);
                            String ModeAdsInterstitial = mData.getString(Config.ModeAdsInterstitial);
                            data.putModeAdsData(ModeAdsBanner, ModeAdsNative, ModeAdsInterstitial, com.devm.ads.Config.NoThing);


                            JSONObject admob = mData.getJSONObject("admob");
                            String admobBannerId = admob.getString(Config.AdmobBannerUnitId);
                            String admobNativeId = admob.getString(Config.AdmobNativeUnitId);
                            String admobInterstitialId = admob.getString(Config.AdmobInterUnitId);
                            data.putAdmobUnitData(admobBannerId, admobNativeId, admobInterstitialId, null);

                            JSONObject facebook = mData.getJSONObject("facebook");
                            String facebookBannerId = facebook.getString(Config.FacebookBannerUnitId);
                            String facebookNativeId = facebook.getString(Config.FacebookNativeUnitId);
                            String facebookInterstitialId = facebook.getString(Config.FacebookInterUnitId);
                            data.putFacebookUnitData(facebookBannerId, facebookNativeId, facebookInterstitialId, null);

                            JSONObject max = mData.getJSONObject("max");
                            String maxBannerId = max.getString(Config.MaxBannerUnitId);
                            String maxNativeId = max.getString(Config.MaxNativeUnitId);
                            String maxInterstitialId = max.getString(Config.MaxInterUnitId);
                            data.putMaxUnitData(maxBannerId, maxNativeId, maxInterstitialId, null);

                            JSONObject chartBoost = mData.getJSONObject("chartBoost");
                            String chartBoostAppId = chartBoost.getString(Config.CharBoostAppId);
                            String chartBoostAppSignature = chartBoost.getString(Config.CharBoostAppSignature);
                            data.putCharBoostUnitData(chartBoostAppId, chartBoostAppSignature);

                            JSONObject ironSource = mData.getJSONObject("ironSource");
                            String ironSourceAppId = ironSource.getString(Config.IronSourceAppId);
                            String ironSourceBannerId = ironSource.getString(Config.IronSourceBannerUnitId);
                            String ironSourceInterstitialId = ironSource.getString(Config.IronSourceInterUnitId);
                            data.putIronSourceUnitData(ironSourceAppId, ironSourceBannerId, ironSourceInterstitialId, null);

                            JSONObject adcolony = mData.getJSONObject("adcolony");
                            String adcolonyAppId = adcolony.getString(Config.AdColonyAppId);
                            String adcolonyBannerId = adcolony.getString(Config.AdColonyBannerUnitId);
                            String adcolonyInterstitialId = adcolony.getString(Config.AdColonyInterUnitIdId);
                            data.putAdColonyUnitData(adcolonyAppId, adcolonyBannerId, adcolonyInterstitialId, null);

                            JSONObject unity = mData.getJSONObject("unity");
                            String unityGameId = unity.getString(Config.UnityGameId);
                            String unityBannerId = unity.getString(Config.UnityAdBannerUnitId);
                            String unityInterstitialId = unity.getString(Config.UnityAdInterUnitId);
                            data.putUnityUnitData(unityGameId, unityBannerId, unityInterstitialId, null);

                            JSONObject vungle = mData.getJSONObject("vungle");
                            String vungleAppId = vungle.getString(Config.VungleAppId);
                            String vungleBannerId = vungle.getString(Config.VungleBannerUnitId);
                            String vungleInterstitialId = vungle.getString(Config.VungleInterUnitId);
                            data.putVungleUnitData(vungleAppId, vungleBannerId, vungleInterstitialId, null);


                            JSONObject update = mData.getJSONObject("update");
                            boolean updateEnable = update.getBoolean("updateEnable");
                            boolean updateEnableClosed = update.getBoolean("updateEnableClosed");
                            String updateIcon = update.getString("updateIcon");
                            String updateTitle = update.getString("updateTitle");
                            String updateDescription = update.getString("updateDescription");
                            String UpdateUrl = update.getString("updateUrl");
                            String UpdateButtonText = update.getString("updateButtonText");


                            sharedApp.putBoolean(Config.UpdateEnable, updateEnable);
                            sharedApp.putBoolean(Config.UpdateEnableClosed, updateEnableClosed);
                            sharedApp.putString(Config.UpdateIcon, updateIcon);
                            sharedApp.putString(Config.UpdateTitle, updateTitle);
                            sharedApp.putString(Config.UpdateDescription, updateDescription);
                            sharedApp.putString(Config.UpdateUrl, UpdateUrl);
                            sharedApp.putString(Config.UpdateButtonText, UpdateButtonText);


                            dataIsLoaded = true;

                            if (timeIsFinish){
                                open();
                            }

                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  println("에러 -> " + error.getMessage());
                        checkInternet();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        rq.add(request);
    }



}