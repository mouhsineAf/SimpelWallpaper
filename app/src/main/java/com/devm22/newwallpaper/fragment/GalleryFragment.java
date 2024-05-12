package com.devm22.newwallpaper.fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devm.ads.MyAds;
import com.devm22.newwallpaper.R;
import com.devm22.newwallpaper.activity.WallpaperViewer;
import com.devm22.newwallpaper.adapter.GalleryAdapter;
import com.devm22.newwallpaper.model.Gallery;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ArrayList<Gallery> galleryModelArrayList = new ArrayList<>();
    RecyclerView recyclerViewGallery;
    GalleryAdapter galleryAdapter;

    EditText editTextSearch;

    LinearLayout layoutNoGallery;

    RelativeLayout layoutSearchTop;

    View layoutToolbarRecycle;
    ImageView toolbarBtnBack;
    ImageView toolbarBtnSelectAll;
    ImageView toolbarBtnEdit;
    ImageView toolbarBtnDelete;
    ImageView toolbarBtnShare;
    TextView toolbarTextSelectCount;

    FrameLayout layoutSnackBar;

    MyAds myAds;

    String wallpaperName, wallpaperPath;


    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
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
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);


        recyclerViewGallery = view.findViewById(R.id.recyclerViewGallery);
        layoutNoGallery = view.findViewById(R.id.layout_no_gallery);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        layoutSearchTop = view.findViewById(R.id.layout_search);

        layoutToolbarRecycle = view.findViewById(R.id.toolbar_recycle);
        toolbarBtnBack = view.findViewById(R.id.btn_back);
        toolbarBtnSelectAll = view.findViewById(R.id.btn_select_all);
        toolbarBtnEdit = view.findViewById(R.id.btn_edit);
        toolbarBtnDelete = view.findViewById(R.id.btn_delete);
        toolbarBtnShare = view.findViewById(R.id.btn_share);
        toolbarTextSelectCount = view.findViewById(R.id.text_selected_count);
        layoutSnackBar = view.findViewById(R.id.layout_snack_bar);

        intAds();
        buildInterAd();


        buildRecycleViewGarrery();
        addGalleries();


        return view;
    }

    private void buildRecycleViewGarrery(){
        recyclerViewGallery.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewGallery.setLayoutManager(mLayoutManager);
        galleryAdapter = new GalleryAdapter(getContext());
        recyclerViewGallery.setAdapter(galleryAdapter);
        galleryAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Gallery arts, int position) {
                if (!galleryAdapter.isSelectionMode()) {
                    wallpaperName = arts.getGalleryName();
                    wallpaperPath = arts.getGalleryPath();
                    myAds.showDialogInterstitialAd(4, new MyAds.CallbackDialogLoadingInterstitial() {
                        @Override
                        public void onFinish(Boolean isLoaded) {
                            if (isLoaded){
                                myAds.showInterstitialAd();
                            }else {
                                goToDetails(wallpaperName, wallpaperPath);
                            }
                        }
                    });

                }else{
                    galleryAdapter.selectItem(position);
                    if (galleryAdapter.getSelectedItemCount() == 0){
                        showToolbarRecycle(false);
                    }

                    changeSelectedCount(galleryAdapter.getSelectedItemCount());

                    if (galleryAdapter.getSelectedItemCount() == 1){
                        toolbarBtnEdit.setVisibility(View.VISIBLE);
                    }else {
                        toolbarBtnEdit.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onItemLongClick(int position) {
                if (!galleryAdapter.isEnableSelectMode){
                    //when action mode is prepare
                    galleryAdapter.isEnableSelectMode = true;
                    //create method for item selection
                    galleryAdapter.selectItem(position);

                    showToolbarRecycle(true);

                }else {
                    galleryAdapter.selectItem(position);

                    if (galleryAdapter.getSelectedItemCount() == 0){
                        galleryAdapter.clearSelection();
                        showToolbarRecycle(false);
                    }
                }
                changeSelectedCount(galleryAdapter.getSelectedItemCount());

                if (galleryAdapter.getSelectedItemCount() == 1){
                    toolbarBtnEdit.setVisibility(View.VISIBLE);
                }else {
                    toolbarBtnEdit.setVisibility(View.GONE);

                }
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                galleryAdapter.filter(s.toString());
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                galleryAdapter.filter(s.toString());

            }
        });

    }

    private void addGalleries(){
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.app_name));

        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

        }

        File[] files = getImages(dir);

        if (files != null){
            galleryModelArrayList = new ArrayList<>();
            for (int i=0; i < files.length; i++){
                File file = files[i];
                String artName = file.getName().replace(".jpg", "");

                galleryModelArrayList.add(new Gallery(i, artName, file.getAbsolutePath()));

            }
            if (files.length > 0){
                layoutNoGallery.setVisibility(View.GONE);
            }

            galleryAdapter.addGalleries(galleryModelArrayList);


        }

    }


    public File[] getImages(File folder) {
        if(folder.exists()){
            return folder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".jpg"));
                }
            });

        }

        return null;
    }


    private void showToolbarRecycle(boolean show){
        if (show){
            layoutToolbarRecycle.setVisibility(View.VISIBLE);
            layoutToolbarRecycle.setEnabled(true);

            layoutSearchTop.setVisibility(View.INVISIBLE);
            layoutSearchTop.setEnabled(false);

        }else {
            layoutToolbarRecycle.setVisibility(View.GONE);
            layoutToolbarRecycle.setEnabled(false);

            layoutSearchTop.setVisibility(View.VISIBLE);
            layoutSearchTop.setEnabled(true);
        }

        toolbarBtnSelectAll.setOnClickListener(view -> {
            galleryAdapter.selectAll();
            changeSelectedCount(galleryAdapter.getSelectedItemCount());
            if (galleryAdapter.getSelectedItemCount() == 1){
                toolbarBtnEdit.setVisibility(View.VISIBLE);
            }else {
                toolbarBtnEdit.setVisibility(View.GONE);
            }
            if (galleryAdapter.getSelectedItemCount() == 0){
                galleryAdapter.clearSelection();
                showToolbarRecycle(false);
            }
        });
        toolbarBtnBack.setOnClickListener(view -> {
            galleryAdapter.clearSelection();
            showToolbarRecycle(false);
        });
        toolbarBtnEdit.setOnClickListener(view -> {
            List<Gallery> list = galleryAdapter.getSelectedList();
            if (list.size() == 1){
                goToDetails(list.get(0).getGalleryName(), list.get(0).getGalleryPath());
                galleryAdapter.clearSelection();
                showToolbarRecycle(false);
            }

        });

        toolbarBtnDelete.setOnClickListener(view -> {
            final BottomSheetDialog dialogSortBy = new BottomSheetDialog(getContext(), R.style.SheetDialog);
            dialogSortBy.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSortBy.setContentView(R.layout.dialog_delete);
            dialogSortBy.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    galleryAdapter.clearSelection();
                    showToolbarRecycle(false);
                }
            });
            dialogSortBy.show();
            TextView textViewDelete = dialogSortBy.findViewById(R.id.textViewDelete);
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    List<Gallery> list = galleryAdapter.getSelectedList();
                    for (int i=0; i < list.size(); i++) {
                        Gallery galleryArtsList = list.get(i);
                        if (deleteImage(galleryArtsList.getGalleryName())){
                            Snackbar.make(layoutSnackBar, "Image Deleted : " + galleryArtsList.getGalleryName(), Snackbar.LENGTH_SHORT).show();
                            galleryModelArrayList.remove(galleryArtsList);
                            galleryAdapter.notifyItemRemoved(i);
                        }


                    }

                    galleryAdapter.clearSelection();



                    galleryAdapter.addGalleries(galleryModelArrayList);

                    changeSelectedCount(galleryAdapter.getSelectedItemCount());
                    if (galleryAdapter.getSelectedItemCount() == 0){
                        showToolbarRecycle(false);
                    }

                    if (galleryAdapter.getSelectedItemCount() == 1){
                        toolbarBtnEdit.setVisibility(View.VISIBLE);
                    }else {
                        toolbarBtnEdit.setVisibility(View.GONE);

                    }

                    if (galleryModelArrayList.size() == 0){
                        layoutNoGallery.setVisibility(View.VISIBLE);
                    }


                    dialogSortBy.dismiss();
                }
            });

            TextView textViewCancel = dialogSortBy.findViewById(R.id.textViewCancel);
            textViewCancel.setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    dialogSortBy.dismiss();
                }
            });



        });
        toolbarBtnShare.setOnClickListener(view -> {
            List<Gallery> list = galleryAdapter.getSelectedList();
            ArrayList<Uri> files = new ArrayList<Uri>();
            for (int i=0; i < list.size(); i++) {
                Gallery galleryArtsList = list.get(i);

                File file = new File(galleryArtsList.getGalleryPath());
                Uri imageUri = FileProvider.getUriForFile(
                        getContext(),
                        getContext().getPackageName() + ".provider",
                        file);
                files.add(imageUri);

            }
            galleryAdapter.clearSelection();
            showToolbarRecycle(false);

            shareImages(files);
        });
    }

    private void changeSelectedCount(int count){
        toolbarTextSelectCount.setText(String.valueOf(count));
    }

    private void goToDetails(String name, String path){
        Intent intent = new Intent(getContext(), WallpaperViewer.class);
        intent.putExtra("IsUrl", false);
        intent.putExtra("WallpaperPath", path);
        intent.putExtra("WallpaperName", name);
        startActivity(intent);
    }

    private void shareImages(ArrayList<Uri> filesToSend){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some images.");
        intent.setType("image/*");

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filesToSend);
        startActivity(Intent.createChooser(intent, "Share Image"));
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
        ContentResolver contentResolver = getContext().getContentResolver();
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
                goToDetails(wallpaperName, wallpaperPath);
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
        addGalleries();
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