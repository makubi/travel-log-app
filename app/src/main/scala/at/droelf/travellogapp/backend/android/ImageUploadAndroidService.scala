package at.droelf.travellogapp.backend.android

import android.app.{PendingIntent, Notification, IntentService}
import android.content.{Context, Intent}
import android.util.Log
import at.droelf.travellogapp.AppStatics
import at.droelf.travellogapp.backend.ImageUploadService
import at.droelf.travellogapp.backend.network.NetworkClient
import at.droelf.travellogapp.ui.NotificationActivity
import org.springframework.http.HttpStatus

import scala.util.{Failure, Success, Try}

class ImageUploadAndroidService extends IntentService("ImageUploadAndroidService") {

  val NOTIFICATION_ID = 1

  val imageUploadService: ImageUploadService = ImageUploadService.getInstance
  val networkClient: NetworkClient = new NetworkClient
  val notificationService = new NotificationService

  override protected def onHandleIntent(intent: Intent) {
    val applicationName = getApplicationName(getApplicationContext)

    val queuedImages = imageUploadService.getQueuedImages
    val numOfQueuedImages = queuedImages.size

    var numErrors = 0
    queuedImages.zipWithIndex.map{ case (queuedImage, index) => {

      notificationService.showNotification(NOTIFICATION_ID, applicationName, s"Processing #${index + 1} of ${numOfQueuedImages} (${numErrors} errors)", index, numOfQueuedImages)

      Try[HttpStatus] {
        networkClient.uploadImage(queuedImage.dateTime, queuedImage.timezone, queuedImage.name, queuedImage.imagePath)
      } match {
        case Success(httpStatus) => if(httpStatus == HttpStatus.OK) imageUploadService.setImageUploaded(queuedImage.id)
        case Failure(e) => numErrors = numErrors + 1
      }
    }}

    notificationService.hideNotification(NOTIFICATION_ID)
  }

  def getApplicationName(context: Context) = {
    val stringId = context.getApplicationInfo().labelRes
    context.getString(stringId)
  }

}