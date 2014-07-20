package at.droelf.travellogapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.{PreferenceActivity, PreferenceFragment}
import android.util.Log
import android.view.{Menu, MenuItem}
import android.widget.TextView
import at.droelf.travellogapp._
import at.droelf.travellogapp.backend.ImageUploadService
import at.droelf.travellogapp.backend.android.ImageUploadAndroidService
import org.joda.time.LocalDateTime

class SettingsActivity extends PreferenceActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    addPreferencesFromResource(R.xml.settings)
  }
}
