package at.droelf.travellogapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras() != null) {
            final ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetworkInfo = systemService.getActiveNetworkInfo();

            final boolean online = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

            Log.d(getClass().getSimpleName(), "Network status changed, online: " + online);

            if(online) {
                context.startService(new Intent(context.getApplicationContext(), ImageUploadAndroidService.class));
            }
        }
    }
}
