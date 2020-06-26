package eu.quadran.androidappyusertrackerlibrary.network;

import eu.quadran.androidappyusertrackerlibrary.utils.Timer;
import eu.quadran.androidappyusertrackerlibrary.utils.Info;
import eu.quadran.androidappyusertrackerlibrary.Tracker;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;

public class RequestHandler {

    private static final String TAG = "RequestHandler";

    Tracker tracker = Tracker.getInstance();

    String url = "";
    String applicationID = "";

    public RequestHandler() {
    }

    public void send(Activity activity, Info info, Timer timer, String applicationID, String startup) throws IOException {

        String parameters = "owa_timestamp=" + Double.valueOf(timer.getTimestamp()) +
                "&owa_page_url=http://" + info.getPackageName(activity) + "." + info.getActivityName(activity) + "/" + startup +
                "&owa_page_title=" + "[" + startup + " start up] " + info.getActivityName(activity) +
                "&owa_event_type=base.page_request" +
                "&owa_cv1=isfl%3Dfalse" +
                "&ttl=" + Double.valueOf(timer.getTotalTimeMillis()) +
                "&owa_site_id=" + applicationID +
                //METADATA
                "&owa_os=" + info.getOSType() +
                "&owa_osv=" + info.getOSVersion() +
                "&owa_sdk=" + info.getSDKVersion() +
                "&owa_appv=" + info.getApplicationVersion() +
                "&owa_dn=" + info.getDeviceName() +
                "&owa_dmd=" + info.getDeviceManufacturingDate() +
                "&owa_cpuc=" + info.getCPUNumber() +
                "&owa_tm=" + info.getTotalMemory() +
                "&owa_am=" + info.getAvailableMemory() +
                "&owa_startup=" + startup +
                "&android=true" +
                "&";

        process(parameters, info);
    }

    public void sendAjax(String className, String url, Info info, Timer timer) throws IOException {

        this.applicationID = tracker.getApplicationID();

        String parameters = "owa_timestamp=" + Double.valueOf(timer.getTimestamp()) +
                "&owa_page_url=" + url +
                "&owa_page_title=" + className +
                "&owa_event_type=base.page_request" +
                "&owa_cv1=isfl%3Dfalse" +
                "&owa_cv10=ajax%3Dtrue" +
                "&ttl=" + Double.valueOf(timer.getTotalTimeMillis()) +
                "&owa_site_id=" + applicationID +
                //METADATA
                "&owa_os=" + info.getOSType() +
                "&owa_osv=" + info.getOSVersion() +
                "&owa_sdk=" + info.getSDKVersion() +
                "&owa_appv=" + info.getApplicationVersion() +
                "&owa_dn=" + info.getDeviceName() +
                "&owa_dmd=" + info.getDeviceManufacturingDate() +
                "&owa_cpuc=" + info.getCPUNumber() +
                "&owa_tm=" + info.getTotalMemory() +
                "&owa_am=" + info.getAvailableMemory() +
                "&android=true" +
                "&";

        process(parameters, info);
    }

    public void process(String parameters, Info info) {

        if (info.isInternetPermission()) {
            url = "https://cloudflare-app.quadran.eu/qwa/log.php?" + parameters; //TODO : change domain to tracker.quadran.eu
            try {
                new HttpRequestTask(
                        new HttpRequest(url, HttpRequest.GET),
                        new HttpRequest.Handler() {
                            @Override
                            public void response(HttpResponse response) {
                            }
                        }).execute();
            } catch (Exception e) {
                Log.e(TAG, "error : " + e);
            }
        }
        else{
            Log.e(TAG, "Internet permission is missing. Please add permission to your AndroidManifest.xml file.");
        }
    }
}
