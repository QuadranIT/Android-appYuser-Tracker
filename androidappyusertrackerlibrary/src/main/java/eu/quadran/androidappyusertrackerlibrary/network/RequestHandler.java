package eu.quadran.androidappyusertrackerlibrary.network;

import eu.quadran.androidappyusertrackerlibrary.utils.Timer;
import eu.quadran.androidappyusertrackerlibrary.utils.Info;
import eu.quadran.androidappyusertrackerlibrary.Tracker;

import android.app.Activity;
import java.io.IOException;

public class RequestHandler {

    //OkHttpClient httpClient = new OkHttpClient();
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

        process(parameters);
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

        process(parameters);
    }

    public void process(String parameters) throws IOException {

        url = "https://cloudflare-app.quadran.eu/qwa/log.php?" + parameters; //TODO : change domain to tracker.quadran.eu

        //OKHTTP
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        httpClient.newCall(request).execute();

        try {
            new HttpRequestTask(
                    new HttpRequest(url, HttpRequest.GET),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                //Code goes here
                                System.out.println("success");
                            } else {
                                //Code goes here
                                System.out.println("error");
                            }
                        }
                    }).execute();
        } catch (Exception e){
            System.out.println("error : " + e);
        }

    }
}
