package at.droelf.travellogapp

import android.content.{Intent, Context, BroadcastReceiver}
import android.net.{NetworkInfo, ConnectivityManager}
import android.util.Log

class NetworkChangeListener extends BroadcastReceiver {
  def onReceive(context: Context, intent: Intent) {
    if (intent.getExtras != null) {
      val systemService: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE).asInstanceOf[ConnectivityManager]
      val activeNetworkInfo: NetworkInfo = systemService.getActiveNetworkInfo
      val online: Boolean = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
      Log.d(getClass.getSimpleName, "Network status changed, online: " + online)
      if (online) {
        context.startService(new Intent(context.getApplicationContext, classOf[ImageUploadAndroidService]))
      }
    }
  }
}