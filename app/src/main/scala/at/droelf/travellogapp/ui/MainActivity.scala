package at.droelf.travellogapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.{ActionMode, Menu, MenuItem, View}
import android.widget.{AbsListView, AdapterView, GridView}
import at.droelf.travellogapp._
import org.joda.time.LocalDateTime

class MainActivity extends Activity with FindView {

  //  private val imageUploadService: ImageUploadService = ImageUploadService.getInstance
  private lazy val imageGridAdapter: ImageGridAdapter = new ImageGridAdapter(this, getLayoutInflater)


  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val gridView = findView[GridView](R.id.images)

    gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL)

    gridView.setAdapter(imageGridAdapter)

    gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener {

      override def onItemCheckedStateChanged(mode: ActionMode, position: Int, id: Long, checked: Boolean): Unit = {
        if(imageGridAdapter.isEnabledForUpload(position)) {
          imageGridAdapter.setItemChecked(position, checked)
        }
      }

      override def onCreateActionMode(mode: ActionMode, menu: Menu): Boolean = {
        mode.getMenuInflater().inflate(R.menu.abs_image, menu);
        return true;
      }

      override def onDestroyActionMode(p1: ActionMode): Unit = {
        imageGridAdapter.clearSelection()
        gridView.clearChoices()
      }

      override def onActionItemClicked(mode: ActionMode, menuItem: MenuItem): Boolean = {
        menuItem.getItemId match {
          case R.id.uploadImage => {

            val positions = gridView.getCheckedItemPositions
            val index = List.range(0, positions.size())

            val images = (for{
              i <- index
              if(positions.get(positions.keyAt(i)) && imageGridAdapter.isEnabledForUpload(positions.keyAt(i)))
            } yield (positions.keyAt(i))).map(imageGridAdapter.getItem(_).guiImage)

            GuiImageService.queueImagesForUpload(images)
            gridView.clearChoices()
            mode.finish()
            imageGridAdapter.update()
            true

          }
          case _ => false
        }

      }

      override def onPrepareActionMode(p1: ActionMode, p2: Menu): Boolean = true



    })


    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long) {
        val image = imageGridAdapter.getItem(position).guiImage

        //imageUploadService.queueImageUpload(image.imageFile.name, image.imageFile.dateTime, "+02:00", image.imageFile.path)
        //GuiImageService.queueImagesForUpload(List(image))
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
