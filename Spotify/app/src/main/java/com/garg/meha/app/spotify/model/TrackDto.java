package com.garg.meha.app.spotify.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * Created by meha on 8/22/15.
 */
public class TrackDto implements Parcelable {
    public long duration_ms;
    public String name;
    public String albumName;
    public String artistArtWorkUrl;
    public String preview_url;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(long duration_ms) {
        this.duration_ms = duration_ms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistArtWorkUrl() {
        return artistArtWorkUrl;
    }

    public void setArtistArtWorkUrl(String artistArtWorkUrl) {
        this.artistArtWorkUrl = artistArtWorkUrl;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.duration_ms);
        dest.writeString(this.name);
        dest.writeString(this.albumName);
        dest.writeString(this.artistArtWorkUrl);
        dest.writeString(this.preview_url);
    }

    public TrackDto(String trackName, Long durationMS, String albumName, String artistArtWorkUrl, String previewURL) {
        this.name = trackName;
        this.duration_ms = durationMS;
        this.albumName = albumName;
        this.artistArtWorkUrl = artistArtWorkUrl;
        this.preview_url = previewURL;
    }

    protected TrackDto(Parcel in) {
        this.duration_ms = in.readLong();
        this.name = in.readString();
        this.albumName = in.readString();
        this.artistArtWorkUrl = in.readString();
        this.preview_url = in.readString();
    }

    public static final Creator<TrackDto> CREATOR = new Creator<TrackDto>() {
        public TrackDto createFromParcel(Parcel source) {
            return new TrackDto(source);
        }

        public TrackDto[] newArray(int size) {
            return new TrackDto[size];
        }
    };
}