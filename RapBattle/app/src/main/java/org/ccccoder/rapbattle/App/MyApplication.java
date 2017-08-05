package org.ccccoder.rapbattle.App;

import android.app.Application;


import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Sangdeok on 2017-08-04.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()//this 생략
                .name("Gokaton.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
}

