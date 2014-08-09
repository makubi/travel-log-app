package at.droelf.travellogapp.ui

import java.io.File

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.{BaseAdapter, ImageView, TextView}
import at.droelf.travellogapp.R
import at.droelf.travellogapp.backend.Settings
import com.squareup.picasso.Picasso


class ImageGridAdapter(context: Context, layoutInflater: LayoutInflater) extends BaseAdapter {

  //TODO fix this -.-
  var images: Seq[ImageItem] = GuiImageService.getImages.map(img => ImageItem(guiImage = img))

  override def getCount: Int = images.length

  override def getItemId(p1: Int): Long = getItem(p1).id

  override def getItem(position: Int): ImageItem = images(position)

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {

    val view = inflateViewIfNull(convertView, () => layoutInflater.inflate(R.layout.grid_image, parent, false))

    val imageItem = getItem(position)
    val guiImage = imageItem.guiImage

    val url = Uri.fromFile(new File(guiImage.imageFile.path))

    Picasso.`with`(context)
      .load(url)
      .placeholder(R.drawable.ic_launcher)
      .resizeDimen(R.dimen.image_width, R.dimen.image_height)
      .centerCrop()
      .into(findView[ImageView](R.id.imageView, view))

    findView[TextView](R.id.img_info, view).setText(
      s"Name: ${guiImage.imageFile.name}\n" +
      s"Path: ${guiImage.imageFile.path}\n" +
      s"Date: ${guiImage.imageFile.dateTime.toLocalDate}\n${guiImage.imageFile.dateTime.toLocalTime}"
    )

    (guiImage.queuedImage, guiImage.uploadedImage) match {
      case (Some(q), Some(u)) => findView[TextView](R.id.img_status, view).setText("{fa-check}")
      case (Some(q), None) => findView[TextView](R.id.img_status, view).setText("{fa-upload}")
      case (_, _) => findView[TextView](R.id.img_status, view).setText("")
    }

    imageItem.checked match {
      case true => {
        findView[TextView](R.id.img_selected, view).setText("{fa-check}")
      }
      case false => {
        findView[TextView](R.id.img_selected, view).setText("")
      }
    }

    view
  }


  override def isEnabled(position: Int): Boolean = {
    true
  }

  def isEnabledForUpload(position: Int): Boolean = {
    val img = getItem(position).guiImage

    (img.queuedImage, img.uploadedImage) match {
      case (Some(q),None) => false
      case (None,Some(u)) => false
      case (Some(q), Some(u)) => false
      case _ => true
    }
  }




  private def inflateViewIfNull[T <: View](convertView: View, f: () => T): T = {
    convertView match {
      case null => f()
      case _ => convertView.asInstanceOf[T]
    }
  }

  //TODO
  private def findView[WidgetType](id: Int, view: View): WidgetType = {
    view.findViewById(id).asInstanceOf[WidgetType]
  }

  def setItemChecked(pos: Int, checked: Boolean) {
    val image = getItem(pos)
    images = images.updated(pos, ImageItem(image.guiImage, image.id, checked))
    notifyDataSetChanged()
  }

  def clearSelection() = {
    images = images.map(img => ImageItem(img.guiImage,img.id,false))
    notifyDataSetChanged()
  }

  def update() = {
    images = GuiImageService.getImages.map(ImageItem(_))
    notifyDataSetChanged()
  }


  case class ImageItem(guiImage: GuiImage, id: Long = 0L, checked: Boolean = false)

}
