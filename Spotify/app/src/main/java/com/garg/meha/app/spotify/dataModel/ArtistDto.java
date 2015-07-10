package com.garg.meha.app.spotify.dataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistDto implements Parcelable {
    private List<Artist> mData;

    public List<Artist> getData() {
        return mData;
    }

    public void setData(List<Artist> mData) {
        this.mData = mData;
    }

    public ArtistDto(Parcel in) {
        in.readList(mData, List.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(mData);
    }


    public static final Parcelable.Creator<ArtistDto> CREATOR
            = new Parcelable.Creator<ArtistDto>(){
        public ArtistDto createFromParcel(Parcel in) {
            return new ArtistDto(in);
        }

        public ArtistDto[] newArray(int size) {
            return new ArtistDto[size];
        }
    };
    
}
