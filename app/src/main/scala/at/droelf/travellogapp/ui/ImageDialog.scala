package at.droelf.travellogapp.ui

import java.io.File

import android.app.{Activity, DialogFragment}
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.OnClickListener
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.{Button, TextView, ImageView}
import at.droelf.travellogapp.R
import com.squareup.picasso.Picasso

class ImageDialog extends DialogFragment {

  var imageAdapter: ImageGridAdapter = null
  var image: GuiImage = null


  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val pos = getArguments.getInt("POS_IN_ADAPTER")
    image = imageAdapter.getItem(pos).guiImage
    val view = inflater.inflate(R.layout.image_dialog, container, false)

    val url = Uri.fromFile(new File(image.imageFile.path))

    Picasso.`with`(getActivity)
      .load(url)
      .placeholder(R.drawable.ic_launcher)
      .fit()
      .centerCrop()
      .into(findView[ImageView](R.id.dialog_image, view))


    val imgString = s"Image: ${image.imageFile.name}, ${image.imageFile.path}, ${image.imageFile.dateTime.toString()}"
    val qString = image.queuedImage.map(img=> s"QueuedImage: ${img.name}, ${img.timezone}, ${img.dateTime.toString()}, ${img.imagePath}").getOrElse("QueuedImage:")
    val uString = image.uploadedImage.map(img => s"UploadedImage: ${img.uploadTimeStamp.toString()}").getOrElse("UploadedImage:")
    findView[TextView](R.id.dialog_image_info, view).setText(s"${imgString}\n${qString}\n${uString}")

    (image.queuedImage, image.uploadedImage) match {
      case (Some(q), _) => {
        findView[Button](R.id.dialog_image_reset, view).setVisibility(View.VISIBLE)
        findView[View](R.id.dialog_image_name_container, view).setVisibility(View.GONE)
      }
      case (None, None) => {
        findView[Button](R.id.dialog_image_reset, view).setVisibility(View.GONE)
        findView[View](R.id.dialog_image_name_container, view).setVisibility(View.VISIBLE)
      }
    }

    findView[Button](R.id.dialog_image_reset, view).setOnClickListener(new OnClickListener {
      override def onClick(p1: View): Unit = {
        GuiImageService.resetImage(image)
        imageAdapter.update()
        getDialog.dismiss()
      }
    })

    findView[Button](R.id.dialog_image_set_name,view).setOnClickListener(new OnClickListener {
      override def onClick(p1: View): Unit = {
        imageAdapter.setItemName(pos, findView[TextView](R.id.dialog_image_name, view).getText.toString)
        getDialog.dismiss()
      }
    })

    view
  }

  override def onAttach(activity: Activity): Unit = {
    imageAdapter = activity.asInstanceOf[MainActivity].imageGridAdapter
    super.onAttach(activity)
  }

  //TODO
  def findView [WidgetType] (id : Int, view: View) : WidgetType = {
    view.findViewById(id).asInstanceOf[WidgetType]
  }

}

object ImageDialog{
  def newInstance(posInAdapter: Int): ImageDialog = {
    val fragment = new ImageDialog

    val bundle = new Bundle
    bundle.putInt("POS_IN_ADAPTER", posInAdapter)

    fragment.setArguments(bundle)
    fragment
  }
}