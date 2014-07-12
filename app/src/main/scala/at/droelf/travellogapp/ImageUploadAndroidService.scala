package at.droelf.travellogapp

import android.app.IntentService
import android.content.Intent
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestClientException

import scala.util.{Failure, Success, Try}

class ImageUploadAndroidService extends IntentService("ImageUploadAndroidService") {

  val imageUploadService: ImageUploadService = ImageUploadService.getInstance
  val networkClient: NetworkClient = new NetworkClient

  protected def onHandleIntent(intent: Intent) {
    imageUploadService.getQueuedImages.map( queuedImage => {

      Try[HttpStatus] {
        networkClient.uploadImage(queuedImage.dateTime, queuedImage.timezone, queuedImage.name, queuedImage.imagePath)
      } match {
        case Success(httpStatus) => if(httpStatus == HttpStatus.OK) imageUploadService.setImageUploaded(queuedImage.id)
        case Failure(e) => e.printStackTrace()
      }

    })
  }

}