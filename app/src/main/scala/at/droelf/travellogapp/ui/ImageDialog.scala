package at.droelf.travellogapp.ui

import java.io.File

import android.app.{Activity, DialogFragment}
import android.net.Uri
import android.os.Bundle
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.ImageView
import at.droelf.travellogapp.R
import com.squareup.picasso.Picasso

class ImageDialog extends DialogFragment {

  var imageAdapter: ImageGridAdapter = null
  var image: GuiImage = null


  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    image = imageAdapter.getItem(getArguments.getInt("POS_IN_ADAPTER")).guiImage
    val view = inflater.inflate(R.layout.image_dialog, container, false)

    val url = Uri.fromFile(new File(image.imageFile.path))

    Picasso.`with`(getActivity)
      .load(url)
      .placeholder(R.drawable.ic_launcher)
      .fit()
      .centerCrop()
      .into(findView[ImageView](R.id.dialog_image, view))

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