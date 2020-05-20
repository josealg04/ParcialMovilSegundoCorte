package com.example.parcial2corte;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

    private String mName;
    private String mArtist;
    private String mDuration;

    public Track(String name, String artist, String duration) {
        this.mName = name;
        this.mArtist = artist;
        this.mDuration = duration;
    }

    protected Track(Parcel in) {
        mName = in.readString();
        mArtist = in.readString();
        mDuration = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public String getName() {
        return mName;
    }

    public String getArtist(){return mArtist;}

    public String getDuration() {
        return mDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mArtist);
        dest.writeString(mDuration);
    }
}
