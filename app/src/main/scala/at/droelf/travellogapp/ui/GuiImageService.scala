package at.droelf.travellogapp.ui

import java.io.File

import android.content.Intent
import android.media.ExifInterface
import at.droelf.travellogapp.backend.android.ImageUploadAndroidService
import at.droelf.travellogapp.{AppStatics, DateTimeUtils, UploadedImage, UploadImage}
import at.droelf.travellogapp.backend.{Settings, UploadedImageService, ImageUploadService}
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.{SuffixFileFilter, TrueFileFilter}
import org.joda.time.{LocalDateTime}
import scala.collection.JavaConversions._
import scala.util.Try


object GuiImageService{

  val imageUploadService = ImageUploadService.getInstance
  val imageUploadedService = UploadedImageService
  val defaultName = ""


  def getImages: List[GuiImage] = {
    val imagesFromFileSystem = loadImagesFromFileSystem
    val queudImages = imageUploadService.getQueuedImages
    val uploadedImages = imageUploadedService.getUploadedImages

    imagesFromFileSystem.map( imageFile =>{
      val queudImage = queudImages.filter(_.imagePath == imageFile.path).headOption
      val uploadedImage = queudImage.map(img => uploadedImages.filter(_.id == img.id).headOption).flatten
      GuiImage(imageFile, queudImage, uploadedImage)
    }).sortWith((d1,d2) => d1.imageFile.dateTime.isAfter(d1.imageFile.dateTime))
  }

  def queueImagesForUpload(images: List[GuiImage]){
    val timeZone = Settings.timeZone.getOrElse("+0000")
    images.foreach( image =>
      imageUploadService.queueImageUpload(image.imageFile.name, image.imageFile.dateTime, timeZone, image.imageFile.path)
    )
    AppStatics.context.startService(new Intent(AppStatics.context, classOf[ImageUploadAndroidService]))

  }

  def resetImage(image: GuiImage){
    (image.queuedImage, image.uploadedImage) match{
      case (Some(q), None) => imageUploadService.resetImage(q.id)
      case (None, Some(u)) => imageUploadedService.resetUploadedImage(u.id)
      case (Some(q), Some(u)) => {
        imageUploadedService.resetUploadedImage(u.id)
        imageUploadService.resetImage(q.id)
      }
      case _ => // trolololololol hans was here
    }
  }



  private def loadImagesFromFileSystem: List[ImageFile] = {
    val files = FileUtils.listFiles(
      new File(Settings.imageBaseDirectory.getOrElse("/sdcard/")),
      new SuffixFileFilter(Array("jpg", "JPG")),
      TrueFileFilter.INSTANCE
    )
    files.map(file =>{
      val dateTimeRaw = new ExifInterface(file.getAbsolutePath).getAttribute(ExifInterface.TAG_DATETIME)
      val dateTime = Try(DateTimeUtils.parseExifDateToLocalDateTime(dateTimeRaw)).getOrElse(LocalDateTime.now())
      ImageFile(file.getAbsolutePath, defaultName, dateTime)
    }).toList
  }

}

case class ImageFile(path: String, name: String, dateTime: LocalDateTime)

case class GuiImage(imageFile: ImageFile, queuedImage: Option[UploadImage], uploadedImage: Option[UploadedImage])

