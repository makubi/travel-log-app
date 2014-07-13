package at.droelf.travellogapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.{Menu, MenuItem}
import at.droelf.travellogapp._
import at.droelf.travellogapp.backend.ImageUploadService
import at.droelf.travellogapp.backend.android.ImageUploadAndroidService
import org.joda.time.LocalDateTime

class NotificationActivity extends Activity {

  private val imageUploadService: ImageUploadService = ImageUploadService.getInstance

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    return true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id: Int = item.getItemId
    if (id == R.id.action_settings) {
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  protected override def onStart {
    super.onStart
    imageUploadService.queueImageUpload("fooBar2", new LocalDateTime, "+02:00", "/storage/emulated/0/DCIM/Camera/IMG_20140619_051510.jpg")
    Log.d("foobar", DateTimeUtils.localDateTimeToIsoString(new LocalDateTime))
    new Thread(new Runnable {
      def run {
        try {
          Thread.sleep(1000)
          startService(new Intent(AppStatics.context, classOf[ImageUploadAndroidService]))
        }
        catch {
          case e: InterruptedException => {
            e.printStackTrace
          }
        }
      }
    }).start
  }
}
