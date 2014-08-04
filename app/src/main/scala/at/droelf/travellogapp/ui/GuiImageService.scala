package at.droelf.travellogapp.ui

import java.io.File

import android.media.ExifInterface
import at.droelf.travellogapp.{DateTimeUtils, UploadedImage, UploadImage}
import at.droelf.travellogapp.backend.{Settings, UploadedImageService, ImageUploadService}
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.{SuffixFileFilter, TrueFileFilter}
import org.joda.time.{LocalDateTime}
import scala.collection.JavaConversions._


object GuiImageService{

  val imageUploadService = ImageUploadService.getInstance
  val imageUploadedService = UploadedImageService


  private def loadImagesFromFileSystem: List[ImageFile] = {
    val files = FileUtils.listFiles(
      new File(Settings.imageBaseDirectory.getOrElse("/sdcard/")),
      new SuffixFileFilter(Array("jpg", "JPG")),
      TrueFileFilter.INSTANCE
    )
    files.map(file =>
      ImageFile(file.getAbsolutePath, file.getName, DateTimeUtils.parseExifDateToLocalDateTime(new ExifInterface(file.getAbsolutePath).getAttribute(ExifInterface.TAG_DATETIME)))
    ).toList
  }

  def getImages(path: Option[String]): List[GuiImage] = {
    val imagesFromFileSystem = loadImagesFromFileSystem
    val queudImages = imageUploadService.getQueuedImages
    val uploadedImages = imageUploadedService.getUploadedImages

    imagesFromFileSystem.map( imageFile =>{
        val queudImage = queudImages.filter(_.imagePath == imageFile.path).headOption
        val uploadedImage = queudImage.map(img => uploadedImages.filter(_.id == img.id).headOption).flatten
        GuiImage(imageFile, queudImage, uploadedImage)
    })
  }

}

case class ImageFile(path: String, name: String, dateTime: LocalDateTime)

case class GuiImage(imageFile: ImageFile, queuedImage: Option[UploadImage], uploadedImage: Option[UploadedImage])

