package at.droelf.travellogapp

import android.app.IntentService
import android.content.Intent
import org.springframework.web.client.RestClientException

class ImageUploadAndroidService extends IntentService("ImageUploadAndroidService") {

  protected def onHandleIntent(intent: Intent) {
    import scala.collection.JavaConversions._
    for (uploadImage <- imageUploadService.getQueuedImages) {
      try {
        val successfulUpload: Boolean = networkClient.uploadImage(uploadImage.dateTime, uploadImage.timezone, uploadImage.name, uploadImage.imagePath)
        if (successfulUpload) {
          imageUploadService.setImageUploaded(uploadImage)
        }
      }
      catch {
        case e: RestClientException => {
          e.printStackTrace
        }
      }
    }
  }

  private final val imageUploadService: ImageUploadService = ImageUploadService.getInstance
  private final val networkClient: NetworkClient = new NetworkClient
}