package at.droelf.travellogapp.ui

import java.io.File

import android.content.Context
import android.graphics.{BitmapFactory, Bitmap}
import android.net.Uri
import android.view.{LayoutInflater, ViewGroup, View}
import android.widget.{TextView, GridView, ImageView, BaseAdapter}
import at.droelf.travellogapp.{DateTimeUtils, R}
import at.droelf.travellogapp.backend.Settings
import com.squareup.picasso.Picasso

class ImageGridAdapter(context: Context, layoutInflater: LayoutInflater) extends BaseAdapter {

  var images = GuiImageService.getImages(Settings.imageBaseDirectory)

  override def getCount: Int = images.length

  override def getItemId(p1: Int): Long = 0L

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {

    val view = initView(convertView, parent)
    val guiImage = getItem(position)
    val url = Uri.fromFile(new File(guiImage.imageFile.path))

    Picasso.`with`(context)
      .load(url)
      .placeholder(R.drawable.ic_launcher)
      .resizeDimen(R.dimen.image_width, R.dimen.image_height)
      .centerCrop()
      .into(findView[ImageView](R.id.imageView, view))

    findView[TextView](R.id.image_text_1, view).setText(s"Queu: ${guiImage.queuedImage}\n${guiImage.imageFile.dateTime}")
    findView[TextView](R.id.image_text_2, view).setText(s"Uploaded: ${guiImage.uploadedImage}")

    view

  }

  private def initView(convertView: View, parent: ViewGroup): View = {
    if (convertView == null) {
      layoutInflater.inflate(R.layout.grid_image, parent, false)
    } else {
      convertView
    }
  }


  //TODO
  private def findView [WidgetType] (id : Int, view: View) : WidgetType = {
    view.findViewById(id).asInstanceOf[WidgetType]
  }


  override def getItem(position: Int): GuiImage = images(position)


  def update() = {
    images = GuiImageService.getImages(Settings.imageBaseDirectory)
    notifyDataSetChanged()
  }

}
