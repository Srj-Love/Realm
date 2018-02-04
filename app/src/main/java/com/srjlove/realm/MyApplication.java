package com.srjlove.realm;


import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
/*
the application instantiate this class first in the process of installing app
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		Realm.init(this);

		// configuration for realm
		RealmConfiguration configuration = new RealmConfiguration.Builder()
				.name("myFirstRealm.realm") // By default the name of db is "default.realm"
				.build();

		Realm.setDefaultConfiguration(configuration);
	}
}
