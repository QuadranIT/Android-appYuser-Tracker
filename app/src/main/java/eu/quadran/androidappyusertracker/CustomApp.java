package eu.quadran.androidappyusertracker;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import eu.quadran.androidappyusertrackerlibrary.Tracker;

public class CustomApp extends Application {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {

        super.onCreate();

        Tracker tracker = Tracker.getInstance();
        tracker.init(this, "2b7d79890b3d01736d82de8b1a664533");
    }
}
