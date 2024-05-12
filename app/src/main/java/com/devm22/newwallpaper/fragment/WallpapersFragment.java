package com.devm22.newwallpaper.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devm.ads.MyAds;
import com.devm22.newwallpaper.Config;
import com.devm22.newwallpaper.R;
import com.devm22.newwallpaper.activity.WallpaperViewer;
import com.devm22.newwallpaper.adapter.WallpaperAdapter;
import com.devm22.newwallpaper.model.Wallpapers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WallpapersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallpapersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    ArrayList<Wallpapers> arrayList;
    WallpaperAdapter wallpaperAdapter;

    ProgressBar progressBarLoading;

    int position;

    MyAds myAds;

    public WallpapersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WallpapersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WallpapersFragment newInstance(String param1, String param2) {
        WallpapersFragment fragment = new WallpapersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallpapers, container, false);

        progressBarLoading = view.findViewById(R.id.progress_loading);
        recyclerView = view.findViewById(R.id.wallpapers__recycler);

        intAds();
        buildInterAd();

        buildRecycleView();
        loadWallpapers();

        return view;
    }


    private void buildRecycleView(){
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        wallpaperAdapter = new WallpaperAdapter(getContext());
        recyclerView.setAdapter(wallpaperAdapter);
        wallpaperAdapter.setOnItemClickListener(new WallpaperAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int ps) {
                position = ps;
                myAds.showDialogInterstitialAd(4, new MyAds.CallbackDialogLoadingInterstitial() {
                    @Override
                    public void onFinish(Boolean isLoaded) {
                        if (isLoaded){
                            myAds.showInterstitialAd();
                        }else {
                            goToDetails();
                        }
                    }
                });
            }
        });

    }

    private void goToDetails(){
        Intent intent = new Intent(getContext(), WallpaperViewer.class);
        Wallpapers model = arrayList.get(position);
        intent.putExtra("IsUrl", true);
        intent.putExtra("WallpaperUrl", model.getImageUrl());
        intent.putExtra("WallpaperName", model.getImageName());
        startActivity(intent);
    }

    private void loadWallpapers(){
        arrayList = new ArrayList<>();
        progressBarLoading.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Config.jsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray mData = jsonObject.getJSONArray("wallpapers");

                            for (int i = 0; i < mData.length(); i++) {
                                JSONObject c = mData.getJSONObject(i);

                                String wallImage = c.getString("wallpaper_image");
                                String wallName = c.getString("wallpaper_name");


                                arrayList.add(new Wallpapers(i, wallName, wallImage));

                            }
                            wallpaperAdapter.setData(arrayList);
                            progressBarLoading.setVisibility(View.GONE);


                        } catch (final JSONException e) {
                            progressBarLoading.setVisibility(View.GONE);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  println("에러 -> " + error.getMessage());
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
        mRequestQueue.add(request);
    }


    //inti ads
    private void intAds() {
        myAds = new MyAds(getActivity(), new MyAds.CallbackInitialization() {
            @Override
            public void onInitializationComplete() {

            }

            @Override
            public void onInitializationFailed(String error) {

            }
        });
    }

    //build Inter Ad
    private void buildInterAd(){
        //create inter ad
        myAds.createInterstitialAd(new MyAds.CallbackInterstitial() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {
                goToDetails();
            }

            @Override
            public void onAdFailedToLoad(String error) {

            }
        });

    }


    @Override
    public void onResume() {
        myAds.onAdResume();
        super.onResume();
        buildInterAd();
    }

    @Override
    public void onPause() {
        myAds.onAdPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        myAds.onAdDestroy();
        super.onDestroy();
    }



}