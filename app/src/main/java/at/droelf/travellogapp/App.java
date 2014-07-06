package at.droelf.travellogapp;

import android.app.Application;
import android.util.Log;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppStatics.context = getApplicationContext();

        Log.d("foobar", "application init");
    }
}
