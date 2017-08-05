package org.ccccoder.rapbattle.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sangdeok on 2017-07-06.
 */

public class Record extends RealmObject{
    //@PrimaryKey
    private long record_id;
    private String title_name;
    private String user_id;
    private String user_nickname;
    private long user_Lv;
    private boolean isLikes; // 서버에서 어레이 불러와서 true, false
    private boolean isCustom;
    private String user_title;
    private String user_lyrics;
    private long music_url;
    private long count_likes;
    private long register_datetime;

    public long getRecord_id(){return record_id;}
    public void setRecord_id(long record_id){this.record_id = record_id;}

    public long getUser_Lv(){return  user_Lv;}
    public void setUser_Lv(long user_Lv){this.user_Lv = user_Lv;}

    public String getTitleName() {return title_name;}
    public void setTitleName(String title_name) {this.title_name = title_name;}

    public String getUser_id(){return user_id;}
    public void setUser_id(String user_id) {this.user_id = user_id;};

    public String getUser_nickname(){return user_nickname;}
    public void setUser_nickname(String user_nickname) {this.user_nickname = user_nickname;}

    public boolean getIsLikes(){return isLikes;}
    public void setIsLikes(boolean isLikes){this.isLikes = isLikes;}

    public boolean getIsCustom(){return isCustom;}
    public void setIsCustom(boolean isCustom){this.isCustom = isCustom;}

    public String getUser_title(){return user_title;}
    public void setUser_title(String user_title) {this.user_title = user_title;};

    public String getUser_lyrics(){return user_lyrics;}
    public void setUser_lyrics(String user_lyrics) {this.user_lyrics = user_lyrics;};

    public long getMusic_url(){return  music_url;}
    public void setMusic_url(long music_url){this.music_url = music_url;}

    public long getCount_likes(){return count_likes;}
    public void setCount_likes(long count_likes){this.count_likes = count_likes;}

    public long getRegister_datetime(){return register_datetime;}
    public void setRegister_datetime(long register_datetime){this.register_datetime = register_datetime;}

}
