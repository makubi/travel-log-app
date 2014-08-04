package at.droelf.travellogapp.ui

import android.app.Activity
import android.view.View

trait FindView extends Activity {
  def findView [WidgetType] (id : Int) : WidgetType = {
    findViewById(id).asInstanceOf[WidgetType]
  }
  def findView [WidgetType] (id : Int, view: View) : WidgetType = {
    view.findViewById(id).asInstanceOf[WidgetType]
  }
}
