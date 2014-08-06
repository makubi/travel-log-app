package at.droelf.travellogapp.backend.android

import android.app.Notification.Builder
import android.app.{Activity, NotificationManager, Notification, PendingIntent}
import android.content.{Context, Intent}
import at.droelf.travellogapp.AppStatics
import at.droelf.travellogapp.ui.NotificationActivity

class NotificationService {

  def hideNotification(notificationId: Int) {
    mNotificationManager.cancel(notificationId)
  }

  def showNotification(notificationId: Int, title: String, message: String, currentProgress: Int, maxProgress: Int) = {
    val mBuilder: Notification.Builder = new Notification.Builder(context)
      .setSmallIcon(android.R.drawable.stat_sys_upload)
      .setContentTitle(title)
      .setContentText(message)
      .setProgress(maxProgress, currentProgress, false)

    val resultIntent: Intent = new Intent(context, classOf[NotificationActivity])
    val resultPendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    mBuilder.setContentIntent(resultPendingIntent)
    val build: Notification = mBuilder.build

    mNotificationManager.notify(notificationId, build)

    mBuilder
  }

  def showNotification(notificationId: Int, title: String, message: String) = {
    val mBuilder: Notification.Builder = new Notification.Builder(context).setSmallIcon(android.R.drawable.stat_notify_error).setContentTitle(title).setContentText(message)

    val resultIntent: Intent = new Intent(context, classOf[NotificationActivity])
    val resultPendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    mBuilder.setContentIntent(resultPendingIntent)
    val build: Notification = mBuilder.build

    mNotificationManager.notify(notificationId, build)

    mBuilder
  }

  private final val context: Context = AppStatics.context
  private final val mNotificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
}