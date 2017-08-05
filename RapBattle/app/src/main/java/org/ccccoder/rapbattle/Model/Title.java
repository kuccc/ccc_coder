package org.ccccoder.rapbattle.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sangdeok on 2017-07-06.
 */

public class Title extends RealmObject{
    @PrimaryKey
    private String name;
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
