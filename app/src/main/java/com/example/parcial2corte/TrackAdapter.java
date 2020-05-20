package com.example.parcial2corte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private Context mContext;
    private ArrayList<Track> mTrackList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public TrackAdapter(Context context, ArrayList<Track> tracklist){
        this.mContext = context;
        this.mTrackList = tracklist;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.track_item, parent, false);
        return new TrackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track currentTrack = mTrackList.get(position);

        String name = currentTrack.getName();
        String artist = currentTrack.getArtist();
        String duration = currentTrack.getDuration();

        holder.mTextViewName.setText(name);
        holder.mTextViewArtist.setText(artist);
        holder.mTextViewDuration.setText(duration);
    }

    @Override
    public int getItemCount() {
        return mTrackList.size();
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextViewName;
        public TextView mTextViewArtist;
        public TextView mTextViewDuration;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewName = itemView.findViewById(R.id.name);
            mTextViewArtist = itemView.findViewById(R.id.artist);
            mTextViewDuration = itemView.findViewById(R.id.duration);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
