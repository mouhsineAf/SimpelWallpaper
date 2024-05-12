package com.devm22.newwallpaper.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devm22.newwallpaper.R;
import com.devm22.newwallpaper.activity.PrivacyPolicy;
import com.nostra13.universalimageloader.BuildConfig;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView imageViewIcon;
    RelativeLayout btnShare, btnRate, btnPrivacy;
    TextView textAppVersion, textAppDeveloper;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        imageViewIcon = view.findViewById(R.id.image_icon);
        btnShare = view.findViewById(R.id.btn_share);
        btnRate = view.findViewById(R.id.btn_rate);
        btnPrivacy = view.findViewById(R.id.btn_privacy);
        textAppVersion = view.findViewById(R.id.text_app_version);
        textAppDeveloper = view.findViewById(R.id.text_developer);

        Glide.with(getContext()).load(R.mipmap.ic_launcher).apply(RequestOptions.circleCropTransform()).into(imageViewIcon);
        textAppDeveloper.setText(getContext().getResources().getString(R.string.text_dev_name) + "  DevM22");
        String versionName = BuildConfig.VERSION_NAME;
        textAppVersion.setText(getContext().getResources().getString(R.string.text_app_version) + "  " + versionName);


        btnShare.setOnClickListener(view1 -> {
            shareApp();
        });

        btnRate.setOnClickListener(view1 -> {
            rateApp();
        });

        btnPrivacy.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), PrivacyPolicy.class);
            startActivity(intent);

        });


        return view;
    }

    public void shareApp(){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getContext().getResources().getString(R.string.app_name));
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            getContext().startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }


    public void rateApp() {
        try {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getContext().getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }
    }

}