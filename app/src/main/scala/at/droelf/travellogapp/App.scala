package at.droelf.travellogapp;

import android.app.Application;
import android.util.Log;

class App extends Application {

    override def onCreate() {
        super.onCreate();

        AppStatics.context = getApplicationContext();

        Log.d("foobar", "application init");
    }
}
