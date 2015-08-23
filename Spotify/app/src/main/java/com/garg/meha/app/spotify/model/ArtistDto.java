package com.garg.meha.app.spotify.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by meha on 8/22/15.
 */
public class ArtistDto implements Parcelable {
//    public Map<String, String> external_urls;
//    public String href;
//    public String id;
    public String name;
//    public String type;
//    public String uri;


    public ArtistDto(String artistName) {
        this.name = artistName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeMap(this.external_urls);
//        dest.writeString(this.href);
//        dest.writeString(this.id);
        dest.writeString(this.name);
//        dest.writeString(this.type);
//        dest.writeString(this.uri);
    }

    protected ArtistDto(Parcel in) {
//        this.external_urls = in.readHashMap(Map.class.getClassLoader());
//        this.href = in.readString();
//        this.id = in.readString();
        this.name = in.readString();
//        this.type = in.readString();
//        this.uri = in.readString();
    }

    public static final Creator<ArtistDto> CREATOR = new Creator<ArtistDto>() {
        public ArtistDto createFromParcel(Parcel source) {
            return new ArtistDto(source);
        }

        public ArtistDto[] newArray(int size) {
            return new ArtistDto[size];
        }
    };
}
