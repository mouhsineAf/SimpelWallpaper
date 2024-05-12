package com.devm22.newwallpaper.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devm22.newwallpaper.R;
import com.devm22.newwallpaper.model.Wallpapers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WallpaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "WallpaperAdapter";

    private Context mContext;

    private ArrayList<Object> data = new ArrayList<>();

    private OnItemClickListener mListener;

    public WallpaperAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<Wallpapers> data) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
        Log.e(TAG, "setData" + data.size());

    }

    public interface OnItemClickListener {
        void onItemClick(int ps);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View menuItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_wallpaper, parent, false);
        return new MainViewHolder(menuItemLayoutView);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MainViewHolder vh = (MainViewHolder) holder;

        Wallpapers currentItem = (Wallpapers) data.get(position);

        vh.wallpaperName.setText(currentItem.getImageName());

        Picasso.get()
                .load(currentItem.getImageUrl())
                .placeholder(R.drawable.image_loading_wallpaper)
                .error(R.drawable.error_photo)
                .into(vh.wallpaperImage);


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Wallpapers currentItem = (Wallpapers) data.get(position);
                mListener.onItemClick(currentItem.getImageId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class MainViewHolder extends RecyclerView.ViewHolder{
        ImageView wallpaperImage;
        TextView wallpaperName;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            wallpaperImage = itemView.findViewById(R.id.wallpaper_image);
            wallpaperName = itemView.findViewById(R.id.wallpaper_name);
        }

    }

}

