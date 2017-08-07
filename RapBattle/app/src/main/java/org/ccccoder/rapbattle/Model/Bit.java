package org.ccccoder.rapbattle.Model;

import io.realm.RealmObject;

/**
 * Created by Sangdeok on 2017-07-06.
 */

public class Bit extends RealmObject{
    //@PrimaryKey
    private long bit_id;
    private String title_name;
    private String lyrics;
    private String music_url;
    private boolean isLikes; // 서버에서 어레이 불러와서 true, false
    private long count_likes;
    private long register_datetime;

    public long getBit_id(){return bit_id;}
    public void setBit_id(long record_id){this.bit_id = bit_id;}

    public String getTitleName() {return title_name;}
    public void setTitleName(String title_name) {this.title_name = title_name;}

    public String getLyrics(){return lyrics;}
    public void setLyrics(String lyrics) {this.lyrics = lyrics;};

    public String getMusic_url(){return  music_url;}
    public void setMusic_url(String music_url){this.music_url = music_url;}

    public long getCount_likes(){return count_likes;}
    public void setCount_likes(long count_likes){this.count_likes = count_likes;}

    public boolean getIsLikes(){return isLikes;}
    public void setIsLikes(boolean isLikes){this.isLikes = isLikes;}

    public long getRegister_datetime(){return register_datetime;}
    public void setRegister_datetime(long register_datetime){this.register_datetime = register_datetime;}
}
