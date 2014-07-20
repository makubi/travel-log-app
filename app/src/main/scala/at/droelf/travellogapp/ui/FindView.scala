package at.droelf.travellogapp.ui

import android.app.Activity

trait FindView extends Activity {
  def findView [WidgetType] (id : Int) : WidgetType = {
    findViewById(id).asInstanceOf[WidgetType]
  }
}
