package com.sauryadeveloper.risemusicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.sauryadeveloper.risemusicplayer.R;
import com.sauryadeveloper.risemusicplayer.inter.FavouriteClick;
import com.sauryadeveloper.risemusicplayer.model.FavouriteModel;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {

    List<FavouriteModel> list;
    FavouriteClick favouriteClick;

    public FavouriteAdapter(List<FavouriteModel> list, FavouriteClick favouriteClick) {
        this.list = list;
        this.favouriteClick = favouriteClick;
    }

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {

        FavouriteModel model = list.get(position);

        String duration = String.valueOf(model.getDuration());

        holder.title.setText(model.getTitle());
        holder.duration.setText(duration);

        Glide.with(holder.itemView.getContext())
                .load(model.getAlbum())
                .placeholder(R.drawable.ic_disc)
                .error(R.drawable.ic_disc)
                .transition(new DrawableTransitionOptions())
                .into(holder.cover);

        holder.itemView.setOnClickListener(v -> {
            favouriteClick.click(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FavouriteHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title, duration;


        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.item_fav_icon);
            title = itemView.findViewById(R.id.item_fav_title);
            duration = itemView.findViewById(R.id.item_fav_duration);
        }
    }
}
