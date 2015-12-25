package com.garg.meha.app.spotify.model;

import java.io.Serializable;

/**
 * Created by meha on 12/23/15.
 */
public class TrackObj implements Serializable {

    public long duration_ms;
    public String trackName;
    public String albumName;
    public String artistArtWorkUrl;
    public String preview_url;
    public int trackPosition;

    public long getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(long duration_ms) {
        this.duration_ms = duration_ms;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String name) {
        this.trackName = name;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
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

    public int getTrackPosition() {
        return trackPosition;
    }

    public void setTrackPosition(int position) {
        this.trackPosition = position;
    }
}
