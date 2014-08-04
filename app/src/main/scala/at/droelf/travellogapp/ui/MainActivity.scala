package at.droelf.travellogapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.{Menu, MenuItem}
import android.widget.TextView
import at.droelf.travellogapp._
import at.droelf.travellogapp.backend.{UploadedImageService, ImageUploadService}
import at.droelf.travellogapp.backend.android.ImageUploadAndroidService
import org.joda.time.LocalDateTime

class MainActivity extends Activity with FindView {

  private val imageUploadService: ImageUploadService = ImageUploadService.getInstance


  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findView[TextView](R.id.foobar).setText(imageUploadService.getQueuedImages.zipWithIndex.map( i => s"${i._2 + 1}. ${i._1.name} ${i._1.imagePath}").mkString(System.getProperty("line.separator")))

    findView[TextView](R.id.uploadedImages).setText(UploadedImageService.getUploadedImages.mkString("\n"))
    // TODO show settings screen only if not configured
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    return true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id: Int = item.getItemId
    if (id == R.id.action_settings) {
      startActivity(new Intent(this, classOf[SettingsActivity]))
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  override protected def onStart {
    super.onStart
    //imageUploadService.queueImageUpload("fooBarBlub", new LocalDateTime, "+02:00", "/storage/emulated/legacy/Pictures/Screenshots/Screenshot_2014-07-27-20-26-55.png")
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
