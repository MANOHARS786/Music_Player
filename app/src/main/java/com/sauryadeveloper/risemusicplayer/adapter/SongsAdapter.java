package com.sauryadeveloper.risemusicplayer.adapter;

import android.util.Log;
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
import com.sauryadeveloper.risemusicplayer.helper.SongHelper;
import com.sauryadeveloper.risemusicplayer.inter.SongsClick;
import com.sauryadeveloper.risemusicplayer.model.SongModel;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsHolder> {

    List<SongModel> list;
    SongsClick songsClick;

    public SongsAdapter(List<SongModel> list, SongsClick songsClick) {
        this.list = list;
        this.songsClick = songsClick;
    }

    @NonNull
    @Override
    public SongsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_songs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongsHolder holder, int position) {

        SongModel songModel = list.get(position);

        String duration = String.valueOf(songModel.getDuration());

        holder.title.setText(songModel.getTitle());
        holder.duration.setText(duration);

        if (position == 0) {
            Log.d("checkccccccc", "title : " + songModel.getTitle());
            Log.d("checkccccccc", "artist: " + songModel.getArt());
            Log.d("checkccccccc", "album: " + songModel.getAlbum());
        }


        Glide.with(holder.itemView.getContext())
                .load(SongHelper.getAlbumImage(songModel.getPath()))
                .placeholder(R.drawable.ic_disc)
                .error(R.drawable.ic_disc)
                .transition(new DrawableTransitionOptions())
                .into(holder.cover);


        holder.itemView.setOnClickListener(v -> {
            songsClick.click(list.get(position));
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SongsHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title, duration;


        public SongsHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.item_music_icon);
            title = itemView.findViewById(R.id.item_music_title);
            duration = itemView.findViewById(R.id.item_music_duration);

        }
    }
}
