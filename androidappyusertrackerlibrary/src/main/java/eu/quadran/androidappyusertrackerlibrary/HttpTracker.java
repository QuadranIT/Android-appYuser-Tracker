package eu.quadran.androidappyusertrackerlibrary;

import android.util.Log;

import eu.quadran.androidappyusertrackerlibrary.network.RequestHandler;
import eu.quadran.androidappyusertrackerlibrary.utils.Timer;
import eu.quadran.androidappyusertrackerlibrary.utils.Info;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.IOException;

@Aspect
public class HttpTracker {

    private static final String TAG = "Httptracker";

    private RequestHandler requestHandler = new RequestHandler();
    private Timer timer = new Timer();
    private Info info = new Info();

    // OKHTTP 3 Requests handling
    @Before("call(* okhttp3.Call.enqueue(..))")
    public void startRequestTimerOkHttp3(JoinPoint joinPoint) throws Throwable{
        startOkHttpTimer();
    }
    @After("execution(* okhttp3.Callback.onResponse(..))")
    public void endRequestTimerOkHttp3(JoinPoint joinPoint) throws Throwable{
        stopOkHttpTimer(joinPoint);
    }

    // OKHTTP Requests handling
    @Before("call(* com.squareup.okhttp.Call.enqueue(..))")
    public void startRequestTimerOkHttp(JoinPoint joinPoint) throws Throwable{
        startOkHttpTimer();
    }
    @After("call(* com.squareup.okhttp.Callback.onResponse(..))")
    public void endRequestTimerOkHttp(JoinPoint joinPoint) throws Throwable{
        stopOkHttpTimer(joinPoint);
    }

    public void startOkHttpTimer(){
        try {
            timer.startTimer();
        } catch (Exception e){
            Log.e(TAG, "error : " + e);
        }
    }

    public void stopOkHttpTimer(JoinPoint joinPoint) throws IOException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = "";
        String targetUrl = "";

        try {
            timer.stopTimer();

            Object[] signatureArgs = joinPoint.getArgs();
            for (Object signatureArg: signatureArgs) {
                if(signatureArg.getClass().getCanonicalName().equals("okhttp3.Response") || signatureArg.getClass().getCanonicalName().equals("com.squareup.okhttp.Response")){
                    targetUrl = signatureArg.toString().substring(signatureArg.toString().indexOf("url=") + 4, signatureArg.toString().indexOf("}"));
                }
            }
            className = extractClassName(methodSignature.getDeclaringType().getName());
            requestHandler.sendAjax(className, targetUrl, info, timer);

        } catch (Exception e){
            Log.e(TAG, "error : " + e);
        }
    }

    public String extractClassName(String name){
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
