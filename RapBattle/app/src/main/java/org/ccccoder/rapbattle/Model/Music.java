package org.ccccoder.rapbattle.Model;

import android.net.Uri;

/**
 * Created by Sangdeok on 2017-08-04.
 */

public class Music {
    Uri musicUri;
    String albumUri;
    String musicTitle;
    String singer;

    public Uri getMusicUri() {
        return musicUri;
    }
    public void setMusicUri(Uri musicUri) {
        this.musicUri = musicUri;
    }
    public String getAlbumUri() {
        return albumUri;
    }
    public void setAlbumUri(String albumUri) {
        this.albumUri = albumUri;
    }
    public String getMusicTitle() {
        return musicTitle;
    }
    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }
    public String getSinger() {
        return singer;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
}