package at.droelf.travellogapp.backend

import _root_.android.preference.PreferenceManager
import at.droelf.travellogapp.{R, AppStatics}

object Settings {
  val context = AppStatics.context
  val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

  def serverBaseUrl = Option(sharedPreferences.getString(context.getString(R.string.setting_key_server_base_url), null))
  def imageBaseDirectory = Option(sharedPreferences.getString(context.getString(R.string.setting_key_image_base_path), null))
  def timeZone = Option(sharedPreferences.getString(context.getString(R.string.setting_key_timezone), null))

  def user = Option(sharedPreferences.getString(context.getString(R.string.setting_key_user), null))
  def password = Option(sharedPreferences.getString(context.getString(R.string.setting_key_password), null))

}