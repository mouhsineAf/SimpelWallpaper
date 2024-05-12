package com.devm22.newwallpaper.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devm22.newwallpaper.R;
import com.devm22.newwallpaper.model.Gallery;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {
    private static final String TAG = "GalleryAdapter";

    Context context;
    ArrayList<Gallery> GalleryArrayList = new ArrayList<>();
    ArrayList<Gallery> GalleryArrayListCopy = new ArrayList<>();

    public boolean isEnableSelectMode = false;
    public boolean isSelectAll = false;
    private List<Gallery> selectList = new ArrayList<>();

    public OnItemClickListener mListener;

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    public void addGalleries(ArrayList<Gallery> galleries) {
        GalleryArrayList.clear();
        GalleryArrayListCopy.clear();
        GalleryArrayList.addAll(galleries);
        GalleryArrayListCopy.addAll(galleries);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        GalleryArrayList.clear();
        if(text.isEmpty()){
            GalleryArrayList.addAll(GalleryArrayListCopy);
        } else{
            text = text.toLowerCase();
            for(Gallery item: GalleryArrayListCopy){
                if(item.getGalleryName().toLowerCase().contains(text)){
                    GalleryArrayList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Gallery arts, int position);
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gallery, parent, false);
        return new GalleryHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, @SuppressLint("RecyclerView") int position) {
        Gallery Gallery = GalleryArrayList.get(position);


        holder.textGalleryName.setText(Gallery.getGalleryName());
        Bitmap bmImg = BitmapFactory.decodeFile(Gallery.getGalleryPath());
        holder.imageGalleryArt.setImageBitmap(bmImg);

        if (Gallery.isSelected()){
            holder.imageSelected.setVisibility(View.VISIBLE);
            holder.layoutSelect.setVisibility(View.VISIBLE);
        }else {
            holder.imageSelected.setVisibility(View.GONE);
            holder.layoutSelect.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onItemClick(Gallery, position);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onItemLongClick(position);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return GalleryArrayList.size();
    }

    public static class GalleryHolder extends RecyclerView.ViewHolder {
        public TextView textGalleryName;
        public ImageView imageGalleryArt;
        public ImageView imageSelected;
        public RelativeLayout layoutSelect;

        public GalleryHolder(@NonNull View itemView) {
            super(itemView);

            textGalleryName = itemView.findViewById(R.id.text_gallery_name);
            imageGalleryArt = itemView.findViewById(R.id.image_gallery_art);
            imageSelected = itemView.findViewById(R.id.imageCheckbox);
            layoutSelect = itemView.findViewById(R.id.layout_select);


        }
    }

    public void selectItem(int position) {
        Gallery item = GalleryArrayList.get(position);
        if (!selectList.contains(item)) {
            selectList.add(item);
            item.setSelected(true);
        } else {
            selectList.remove(item);
            item.setSelected(false);

        }
        if (selectList.size() == 0){
            isEnableSelectMode = false;
        }
        notifyItemChanged(position);



    }

    public void selectAll(){
        if (selectList.size() == GalleryArrayList.size()) {
            isSelectAll = false;
            selectList.clear();
        } else {
            isSelectAll = true;

            selectList.clear();
            selectList.addAll(GalleryArrayList);
        }
        for (Gallery listArts: GalleryArrayList) {
            listArts.setSelected(isSelectAll);
        }

        notifyDataSetChanged();
    }

    public boolean isSelectionMode(){
        return isEnableSelectMode;
    }

    public int getSelectedItemCount() {
        return selectList.size();
    }

    public void clearSelection() {
        isEnableSelectMode = false;
        selectList.clear();
        for (Gallery listArts: GalleryArrayList) {
            listArts.setSelected(false);
        }
        notifyDataSetChanged();
    }

    public List<Gallery> getSelectedList(){
        return selectList;
    }
}
