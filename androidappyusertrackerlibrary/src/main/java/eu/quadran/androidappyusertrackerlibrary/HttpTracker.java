package eu.quadran.androidappyusertrackerlibrary;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eu.quadran.androidappyusertrackerlibrary.network.RequestHandler;
import eu.quadran.androidappyusertrackerlibrary.utils.Info;
import eu.quadran.androidappyusertrackerlibrary.utils.Timer;

@Aspect
public class HttpTracker {

    private static final String TAG = "Httptracker";

    private RequestHandler requestHandler = new RequestHandler();
    private Info info = new Info(true);
    HashMap<String, Timer> timersMap = new HashMap<>();

    // OKHTTP 3 Requests handling
    @Before("call(* okhttp3.OkHttpClient.newCall(..))")
    public void startRequestTimerOkHttp3(JoinPoint joinPoint) throws Throwable{
        startOkHttpTimer(joinPoint);
    }
    @After("execution(* okhttp3.Callback.onResponse(..))")
    public void endRequestTimerOkHttp3(JoinPoint joinPoint) throws Throwable{
        stopOkHttpTimer(joinPoint);
    }

    // OKHTTP Requests handling
    @Before("call(* com.squareup.okhttp.OkHttpClient.newCall(..))")
    public void startRequestTimerOkHttp(JoinPoint joinPoint) throws Throwable{
        startOkHttpTimer(joinPoint);
    }
    @After("call(* com.squareup.okhttp.Callback.onResponse(..))")
    public void endRequestTimerOkHttp(JoinPoint joinPoint) throws Throwable{
        stopOkHttpTimer(joinPoint);
    }

    public void startOkHttpTimer(JoinPoint joinPoint){
        try {
            Timer timer = new Timer();
            timer.startTimer();

            String targetUrl = "";

            Object[] signatureArgs = joinPoint.getArgs();

            for (Object signatureArg : signatureArgs) {
                if (signatureArg.getClass().getCanonicalName().equals("okhttp3.Request") || signatureArg.getClass().getCanonicalName().equals("com.squareup.okhttp.Request")) {
                    targetUrl = extractUrl(signatureArg);
                    if(!targetUrl.isEmpty()) timersMap.put(targetUrl, timer);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error : " + e);
        }
    }

    public void stopOkHttpTimer(JoinPoint joinPoint) throws IOException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = "";
        String targetUrl = "";

        try {

            Object[] signatureArgs = joinPoint.getArgs();
            for (Object signatureArg : signatureArgs) {
                if (signatureArg.getClass().getCanonicalName().equals("okhttp3.Response") || signatureArg.getClass().getCanonicalName().equals("com.squareup.okhttp.Response")) {
                    targetUrl = extractUrl(signatureArg);
                }
            }

            Iterator<Map.Entry<String, Timer>> entryIt = timersMap.entrySet().iterator();

            while (entryIt.hasNext()) {
                Map.Entry<String, Timer> entry = entryIt.next();
                if (targetUrl.equals(entry.getKey())) {
                    entry.getValue().stopTimer();

                    className = extractClassName(methodSignature.getDeclaringType().getName());
                    requestHandler.sendAjax(className, targetUrl, info, entry.getValue());

                    entryIt.remove();
                    break;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error : " + e);
        }
    }

    public String extractClassName(String name){
        return name.substring(name.lastIndexOf(".") + 1);
    }

    public String extractUrl(Object signatureArg){
        String string = signatureArg.toString();
        String packageName = signatureArg.getClass().getCanonicalName();

        if (packageName.equals("okhttp3.Request")) return string.substring(string.indexOf("url=") + 4, string.indexOf("}"));
        else if (packageName.equals("okhttp3.Response")) return string.substring(string.indexOf("url=") + 4, string.indexOf("}"));
        else if (packageName.equals("com.squareup.okhttp.Request")) return string.substring(string.indexOf("url=") + 4, string.indexOf(", tag="));
        else if (packageName.equals("com.squareup.okhttp.Response")) return string.substring(string.indexOf("url=") + 4, string.indexOf("}"));

        return "";
    }
}
