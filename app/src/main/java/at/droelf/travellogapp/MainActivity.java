package at.droelf.travellogapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Partial;
import org.joda.time.Period;

import java.util.TimeZone;

import javax.xml.datatype.Duration;


public class MainActivity extends Activity {

    private final ImageUploadService imageUploadService = ImageUploadService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ColumnStringBuilder builder = new ColumnStringBuilder();

        imageUploadService.queueImageUpload("fooBar2", new LocalDateTime(), "+02:00", "/storage/emulated/0/DCIM/Camera/IMG_20140619_051510.jpg");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new NetworkClient().uploadImage(new UploadImage(new DateTime(), "fooBar", "/storage/emulated/0/DCIM/Camera/IMG_20140619_051510.jpg"));
//            }
//        }).start();

        Log.d("foobar", DateTimeUtils.localDateTimeToIsoString(new LocalDateTime()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    startService(new Intent(AppStatics.context, ImageUploadAndroidService.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
