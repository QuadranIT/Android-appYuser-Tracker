package eu.quadran.androidappyusertrackerlibrary.utils;

import android.app.Activity;
import android.os.Build;

import eu.quadran.androidappyusertrackerlibrary.BuildConfig;

public class Info {

    public Info(boolean internetPermission) {
        this.internetPermission = internetPermission;
    }

    boolean internetPermission;

    public String getPackageName(Activity activity) { return activity.getApplicationContext().getPackageName(); }

    public String getActivityName(Activity activity){
        return activity.getClass().getSimpleName();
    }

    public String getOSType() {
        return "Android";
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getSDKVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public String getApplicationVersion(){ return BuildConfig.VERSION_NAME; }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

    public String getDeviceManufacturingDate() {
        return  String.valueOf(Build.TIME / 1000);
    }

    public String getCPUNumber(){ return String.valueOf(Runtime.getRuntime().availableProcessors()); }

    public String getTotalMemory(){
        long totalSize = 0L;
        try {
            Runtime info = Runtime.getRuntime();
            totalSize = info.totalMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(totalSize);
    }

    public String getAvailableMemory(){
        long freeSize = 0L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(freeSize);
    }

    public boolean isInternetPermission() {return internetPermission;}

    public void setInternetPermission(boolean internetPermission) {this.internetPermission = internetPermission;}

    public String getNetworkInformation(Activity activity){
        // TODO : Getting network types w/out permission ?
        return "";
    }
}
