package com.wikitude.samples.util.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wikitude.samples.util.TileItem;
import com.wikitude.sdksamples.R;

import java.util.List;

/**
 * Created by Mahesh on 11/25/2017.
 */


public class TilesAdapter extends RecyclerView.Adapter<TilesAdapter.MyViewHolder> {
    private Context mContext;
    private List<TileItem> tileList;
    private TileAdapterListner listener;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            cardView = view.findViewById(R.id.card_view);

            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }


    public TilesAdapter(Context mContext, List<TileItem> tileList, TileAdapterListner listner) {
        this.mContext = mContext;
        this.tileList = tileList;
        this.listener= listner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.basic_image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TileItem album = tileList.get(position);
        holder.title.setText(album.getName());
        Log.i("test",album.getbackGroundColor());
        holder.cardView.setCardBackgroundColor(Color.parseColor(album.getbackGroundColor()));

       Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardSelected(position, holder.thumbnail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return tileList.size();
    }

    public interface TileAdapterListner {
        void onAddToFavoriteSelected(int position);

        void onPlayNextSelected(int position);

        void onCardSelected(int position, ImageView thumbnail);
    }
}
