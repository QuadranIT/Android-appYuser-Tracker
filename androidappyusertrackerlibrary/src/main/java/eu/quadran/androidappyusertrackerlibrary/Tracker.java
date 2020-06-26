package eu.quadran.androidappyusertrackerlibrary;

import eu.quadran.androidappyusertrackerlibrary.network.RequestHandler;
import eu.quadran.androidappyusertrackerlibrary.utils.Timer;
import eu.quadran.androidappyusertrackerlibrary.utils.Info;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.IOException;


public class Tracker implements Application.ActivityLifecycleCallbacks {

    private String applicationID = "";

    private RequestHandler requestHandler = new RequestHandler();
    private Timer timer = new Timer();
    private Info info = new Info(true);

    private boolean applicationInitFlag = false; // Cold startTimer flag
    private boolean activityCreatedFlag = false; // Warm startTimer flag
    private boolean activityStartedFlag = false; // Hot startTimer flag

    private int activeActivities = 0;

    private static final Tracker ourInstance = new Tracker();

    public static Tracker getInstance() {
        return ourInstance;
    }

    private Tracker() {
    }

    public void init(Application application, String appID) {
        if (!checkInternetPermission(application)) info.setInternetPermission(false);

        // Cold startup
        timer.startTimer();

        applicationInitFlag = true;

        setApplicationID(appID);
        application.registerActivityLifecycleCallbacks(this);
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        // Warm startup
        if (activeActivities == 0) {
            if (!activityCreatedFlag && !applicationInitFlag) {
                timer.startTimer();
                activityCreatedFlag = true;
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // Hot startup
        if (activeActivities == 0) {
            if (!activityStartedFlag && !applicationInitFlag) {
                timer.startTimer();
                activityStartedFlag = true;
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

        // onResume for a cold startTimer
        if (applicationInitFlag) {
            applicationInitFlag = false;
            timer.stopTimer();

            try {
                requestHandler.send(activity, info, timer, applicationID, "Cold");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // onResume for a hot startTimer
        if (activeActivities == 0) {
            if (activityStartedFlag) {
                activityStartedFlag = false;
                timer.stopTimer();

                try {
                    requestHandler.send(activity, info, timer, applicationID, "Hot");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // onResume for a warm startTimer
        if (activeActivities == 0) {
            if (activityCreatedFlag) {
                activityCreatedFlag = false;
                timer.stopTimer();

                try {
                    requestHandler.send(activity, info, timer, applicationID, "Warm");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        activeActivities++;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activeActivities > 0) activeActivities--;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public boolean checkInternetPermission(Application application){
        return ContextCompat.checkSelfPermission(application, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }
}