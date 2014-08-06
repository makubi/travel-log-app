package at.droelf.travellogapp.ui

import java.io.File

import android.content.Context
import android.graphics.{BitmapFactory, Bitmap}
import android.net.Uri
import android.util.Log
import android.view.{LayoutInflater, ViewGroup, View}
import android.widget.{TextView, GridView, ImageView, BaseAdapter}
import at.droelf.travellogapp.{DateTimeUtils, R}
import at.droelf.travellogapp.backend.Settings
import com.squareup.picasso.Picasso
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter

class ImageGridAdapter(context: Context, layoutInflater: LayoutInflater) extends BaseAdapter with StickyGridHeadersBaseAdapter {

  var images = GuiImageService.getImages(Settings.imageBaseDirectory)
  var headers = images.map(_.imageFile.dateTime.toLocalDate).distinct

  override def getCount: Int = images.length

  override def getItemId(p1: Int): Long = 0L

  override def getItem(position: Int): GuiImage = images(position)

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {

    val view = inflateViewIfNull(convertView, () => layoutInflater.inflate(R.layout.grid_image, parent, false))

    val guiImage = getItem(position)
    val url = Uri.fromFile(new File(guiImage.imageFile.path))

    Picasso.`with`(context)
      .load(url)
      .placeholder(R.drawable.ic_launcher)
      .resizeDimen(R.dimen.image_width, R.dimen.image_height)
      .centerCrop()
      .into(findView[ImageView](R.id.imageView, view))

    findView[TextView](R.id.img_info, view)

    (guiImage.queuedImage, guiImage.uploadedImage) match{
      case (Some(q),Some(u)) => findView[TextView](R.id.img_status, view).setText("{fa-check}")
      case (Some(q), None) => findView[TextView](R.id.img_status, view).setText("{fa-upload}")
      case (_,_) => findView[TextView](R.id.img_status, view).setText("")
    }

    view
  }

  override def getHeaderView(position: Int, convertView: View, parent: ViewGroup): View = {
    val view = inflateViewIfNull(convertView, () => layoutInflater.inflate(R.layout.grid_image_header, parent, false))
    findView[TextView](R.id.textView,view).setText(headers(position).toString)
    view
  }

  override def getCountForHeader(header: Int): Int = images.filter(_.imageFile.dateTime.toLocalDate.isEqual(headers(header))).size

  override def getNumHeaders: Int = headers.size


  private def inflateViewIfNull[T <: View](convertView: View, f: () => T): T = {
    convertView match{
      case null => f()
      case _ => convertView.asInstanceOf[T]
    }
  }


  //TODO
  private def findView [WidgetType] (id : Int, view: View) : WidgetType = {
    view.findViewById(id).asInstanceOf[WidgetType]
  }


  def update() = {
    images = GuiImageService.getImages(Settings.imageBaseDirectory)
    notifyDataSetChanged()
  }

}
