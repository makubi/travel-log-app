package at.droelf.travellogapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.{Menu, MenuItem, View}
import android.widget.{AdapterView, GridView}
import at.droelf.travellogapp._
import at.droelf.travellogapp.backend.ImageUploadService
import at.droelf.travellogapp.backend.android.ImageUploadAndroidService
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView
import org.joda.time.LocalDateTime

class MainActivity extends Activity with FindView {

  private val imageUploadService: ImageUploadService = ImageUploadService.getInstance
  private lazy val imageGridAdapter: ImageGridAdapter = new ImageGridAdapter(this, getLayoutInflater)

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val gridView = findView[StickyGridHeadersGridView](R.id.images)
    gridView.setAdapter(imageGridAdapter)

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long) {
        val image = imageGridAdapter.getItem(position)

        //imageUploadService.queueImageUpload(image.imageFile.name, image.imageFile.dateTime, "+02:00", image.imageFile.path)

        GuiImageService.resetImage(image)

        imageGridAdapter.update()
      }
    })


  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    return true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id: Int = item.getItemId
    id match {
      case R.id.action_settings => {
        startActivity(new Intent(this, classOf[SettingsActivity]))
        return true
      }
      case R.id.refresh => {
        imageGridAdapter.update()
        return true

      }
    }

    return super.onOptionsItemSelected(item)
  }

  override protected def onStart {
    super.onStart
    //imageUploadService.queueImageUpload("fooBarBlub", new LocalDateTime, "+02:00", "/storage/emulated/legacy/Pictures/Screenshots/Screenshot_2014-07-27-20-26-55.png")
    Log.d("foobar", DateTimeUtils.localDateTimeToIsoString(new LocalDateTime))
  }
}
