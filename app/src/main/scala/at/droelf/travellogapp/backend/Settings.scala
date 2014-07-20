package at.droelf.travellogapp.backend

import _root_.android.preference.PreferenceManager
import at.droelf.travellogapp.{R, AppStatics}

object Settings {
  val context = AppStatics.context
  val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

  def serverBaseUrl = Option(sharedPreferences.getString(context.getString(R.string.setting_key_server_base_url), null))


}