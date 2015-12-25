package com.garg.meha.app.spotify.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by meha on 8/22/15.
 */
public class TrackDto implements Parcelable {
    public long duration_ms;
    public String name;
    public String albumName;
    public String artistArtWorkUrl;
    public String preview_url;
    public int position;
    public List<TrackObj> trackObjList;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<TrackObj> getTrackObjList() {
        return trackObjList;
    }

    public void setTrackObjList(List<TrackObj> trackObjList) {
        this.trackObjList = trackObjList;
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
        dest.writeInt(this.position);
        dest.writeList(this.trackObjList);
    }

    public TrackDto(String trackName, Long durationMS, String albumName, String artistArtWorkUrl, String previewURL, int position, List<TrackObj> trackObjList) {
        this.name = trackName;
        this.duration_ms = durationMS;
        this.albumName = albumName;
        this.artistArtWorkUrl = artistArtWorkUrl;
        this.preview_url = previewURL;
        this.position = position;
        this.trackObjList = trackObjList;
    }

    protected TrackDto(Parcel in) {
        this.duration_ms = in.readLong();
        this.name = in.readString();
        this.albumName = in.readString();
        this.artistArtWorkUrl = in.readString();
        this.preview_url = in.readString();
        this.position = in.readInt();
        this.trackObjList = in.readArrayList(TrackObj.class.getClassLoader());
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