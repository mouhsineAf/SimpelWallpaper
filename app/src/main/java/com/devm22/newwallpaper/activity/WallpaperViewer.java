package com.devm22.newwallpaper.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devm.ads.MyAds;
import com.devm22.newwallpaper.Config;
import com.devm22.newwallpaper.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WallpaperViewer extends AppCompatActivity {

    ImageView imageViewWallpaper;
    LinearLayout btnShare, btnSetWallpaper, btnDownload;
    ImageView imageDownload;
    TextView textDownload;
    ImageView btnBack;

    private String single_choice_selected;

    private String wallpaperName;

    private boolean isUrl = true;

    FrameLayout layoutBannerAd;

    MyAds myAds;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_viewer);

        imageViewWallpaper = findViewById(R.id.image_wallpaper);
        btnShare = findViewById(R.id.btn_share);
        btnSetWallpaper = findViewById(R.id.btn_set_wallpaper);
        btnDownload = findViewById(R.id.btn_download);
        imageDownload = findViewById(R.id.image_download);
        textDownload = findViewById(R.id.text_download);
        btnBack = findViewById(R.id.btn_back);
        layoutBannerAd = findViewById(R.id.layout_banner_ad);


        intAds();
        buildInterAd();
        buildBannerAd();

        setData();

        btnBack.setOnClickListener(view -> {
            back();
        });
        
        
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            isUrl = bundle.getBoolean("IsUrl");
            if (isUrl){
                Picasso.get()
                        .load(bundle.getString("WallpaperUrl"))
                        .placeholder(R.drawable.image_loading_wallpaper)
                        .error(R.drawable.error_photo)
                        .into(imageViewWallpaper);
            }else {
                Bitmap bmImg = BitmapFactory.decodeFile(bundle.getString("WallpaperPath"));
                imageViewWallpaper.setImageBitmap(bmImg);

                textDownload.setText(getResources().getString(R.string.text_delete));
                imageDownload.setImageResource(R.drawable.ic_delete);
            }


            wallpaperName = bundle.getString("WallpaperName");

            btnShare.setOnClickListener(view -> {
                if (verifyPermissions(1001)){
                    shareWallpaper();
                }
            });

            btnSetWallpaper.setOnClickListener(view -> {
                if (Build.VERSION.SDK_INT >= 24) {
                    dialogOptionSetWallpaper();
                } else {
                    setWallpaper();
                }
                myAds.showInterstitialAd();
            });

            btnDownload.setOnClickListener(view -> {
                if (isUrl){
                    if (verifyPermissions(1002)){
                        downloadWallpaper();
                        myAds.showInterstitialAd();
                    }
                }else {
                    final BottomSheetDialog dialogSortBy = new BottomSheetDialog(this, R.style.SheetDialog);
                    dialogSortBy.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogSortBy.setContentView(R.layout.dialog_delete);
                    dialogSortBy.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {

                        }
                    });
                    dialogSortBy.show();
                    TextView textViewDelete = dialogSortBy.findViewById(R.id.textViewDelete);
                    textViewDelete.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (deleteImage(wallpaperName)){
                                Snackbar.make(imageViewWallpaper, "Image Deleted : " + wallpaperName, Snackbar.LENGTH_SHORT).show();
                            }
                            dialogSortBy.dismiss();
                            back();

                        }
                    });

                    TextView textViewCancel = dialogSortBy.findViewById(R.id.textViewCancel);
                    textViewCancel.setOnClickListener(new View.OnClickListener() {
                        public final void onClick(View view) {
                            dialogSortBy.dismiss();
                        }
                    });
                }


            });


        }
    }

    private void back(){
        finish();
    }


    private void shareWallpaper(){
        Bitmap bitmap = ((BitmapDrawable) imageViewWallpaper.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        setAction(imageInByte, Config.SHARE);
    }


    private void setWallpaper(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.msg_preparing_wallpaper));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Bitmap bitmap = getBitmapFromView(imageViewWallpaper);
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            wallpaperManager.setBitmap(bitmap);
            progressDialog.setMessage(getString(R.string.msg_apply_wallpaper));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();

                }
            }, Config.DELAY_SET);
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(imageViewWallpaper, getString(R.string.snack_bar_failed), Snackbar.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    @TargetApi(Build.VERSION_CODES.N)
    public void dialogOptionSetWallpaper() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        String[] items = getResources().getStringArray(R.array.dialog_set_wallpaper);
        single_choice_selected = items[0];
        int itemSelected = 0;
        new AlertDialog.Builder(WallpaperViewer.this)
                .setTitle(R.string.dialog_set_title)
                .setSingleChoiceItems(items, itemSelected, (dialogInterface, i) -> single_choice_selected = items[i])
                .setPositiveButton(R.string.dialog_option_ok, (dialogInterface, i) -> {

                    progressDialog.setMessage(getString(R.string.msg_preparing_wallpaper));
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if (single_choice_selected.equals(getResources().getString(R.string.set_home_screen))) {
                        setWallpaperTwo(progressDialog, Config.HOME_SCREEN);
                        progressDialog.setMessage(getString(R.string.msg_apply_wallpaper));
                    } else if (single_choice_selected.equals(getResources().getString(R.string.set_lock_screen))) {
                        setWallpaperTwo(progressDialog, Config.LOCK_SCREEN);
                        progressDialog.setMessage(getString(R.string.msg_apply_wallpaper));
                    } else if (single_choice_selected.equals(getResources().getString(R.string.set_both))) {
                        setWallpaperTwo(progressDialog, Config.BOTH);
                        progressDialog.setMessage(getString(R.string.msg_apply_wallpaper));
                    }

                })
                .setNegativeButton(R.string.dialog_option_cancel, null)
                .show();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void setWallpaperTwo(ProgressDialog progressDialog, String setAs){
        Bitmap bitmap = getBitmapFromView(imageViewWallpaper);
        switch (setAs) {
            case Config.HOME_SCREEN:
                try {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                    progressDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar.make(imageViewWallpaper, getString(R.string.snack_bar_failed), Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                break;

            case Config.LOCK_SCREEN:
                try {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                    progressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar.make(imageViewWallpaper, getString(R.string.snack_bar_failed), Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                break;

            case Config.BOTH:
                try {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                    wallpaperManager.setBitmap(bitmap);
                    progressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar.make(imageViewWallpaper, getString(R.string.snack_bar_failed), Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                break;
        }
    }

    private void downloadWallpaper(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.snack_bar_saving));
        progressDialog.setCancelable(false);
        progressDialog.show();

        Bitmap bitmap = ((BitmapDrawable) imageViewWallpaper.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        setAction(imageInByte, Config.DOWNLOAD);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Snackbar.make(imageViewWallpaper, getString(R.string.snack_bar_saved), Snackbar.LENGTH_SHORT).show();
            //  updateDownload(wallpaper.image_id);
            progressDialog.dismiss();
        }, Config.DELAY_SET);

    }



    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            // if we unable to get background drawable then we will set white color as wallpaper
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void setAction(byte[] bytes, String action) {
        try {
            File dir;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.app_name));

            } else {
                dir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

            }
            boolean success = true;
            if (!dir.exists()) {
                success = dir.mkdirs();
            }
            if (success) {
                File imageFile = new File(dir, wallpaperName + ".jpg");
                FileOutputStream fileWriter = new FileOutputStream(imageFile);
                fileWriter.write(bytes);
                fileWriter.flush();
                fileWriter.close();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(imageFile.getAbsolutePath());
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                switch (action) {
                    case Config.DOWNLOAD:
                        //do nothing
                        break;

                    case Config.SHARE:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        // share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text) + "\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + imageFile.getAbsolutePath()));
                        startActivity(Intent.createChooser(share, "Share Image"));
                        break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean verifyPermissions(int what) {
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, what);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    shareWallpaper();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 1002: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadWallpaper();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    public boolean deleteImage(String photoName){
        boolean isDelete = false;

        File fDelete;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            fDelete = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.app_name)  + "/"+ photoName + ".jpg");


        } else {
            fDelete = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name)  + "/"+ photoName + ".jpg");


        }

        //  File fileStorage = Environment.getExternalStorageDirectory();
        //  File fDelete = new File(fileStorage.getAbsolutePath() + "/DCIM/"+ getContext().getResources().getString(R.string.app_name) + "/"+ photoName + ".jpg");

        // Set up the projection (we only need the ID)
        String[] projection = {MediaStore.Images.Media._ID};

        // Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{fDelete.getAbsolutePath()};

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);

            isDelete = true;

        } else {


        }
        c.close();

        return isDelete;
    }


    //inti ads
    private void intAds() {
        myAds = new MyAds(WallpaperViewer.this, new MyAds.CallbackInitialization() {
            @Override
            public void onInitializationComplete() {

            }

            @Override
            public void onInitializationFailed(String error) {

            }
        });
    }

    //build Banner
    private void buildBannerAd(){
        //create Banner ad
        myAds.createBannerAd(new MyAds.CallbackBanner() {
            @Override
            public void onAdLoaded(View adView) {
                layoutBannerAd.removeAllViews();
                layoutBannerAd.addView(adView);
            }

            @Override
            public void onAdFailedToLoad(String error) {

            }
        });
        myAds.showBannerAd(true);
    }

    private void buildInterAd(){
        //create inter ad
        myAds.createInterstitialAd(new MyAds.CallbackInterstitial() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(String error) {

            }
        });

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