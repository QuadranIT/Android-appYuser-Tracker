package eu.quadran.androidappyusertrackerlibrary;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class HttpTracker {

    private RequestHandler requestHandler = new RequestHandler();
    private Timer timer = new Timer();
    private Info info = new Info();

    // Handling okHttp requests start
    @Before("call(* okhttp3.Call.enqueue(..))")
    public void startRequestTimer(JoinPoint joinPoint) throws Throwable{
        try {
            timer.startTimer();
        } finally {}
    }

    //Handling okHttp requests end
    @After("execution(* okhttp3.Callback.onResponse(..))")
    public void endRequestTimer(JoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = "";
        String targetUrl = "";

        try {
            timer.stopTimer();

            Object[] signatureArgs = joinPoint.getArgs();
            for (Object signatureArg: signatureArgs) {
                if(signatureArg.getClass().getCanonicalName().equals("okhttp3.Response")){
                    targetUrl = signatureArg.toString().substring(signatureArg.toString().indexOf("url=") + 4, signatureArg.toString().indexOf("}"));
                }
            }
            className = extractClassName(methodSignature.getDeclaringType().getName());

            requestHandler.sendAjax(className, targetUrl, info, timer);

        } finally {}
    }

    public String extractClassName(String name){
        return name.substring(name.lastIndexOf(".") + 1, name.length()-2);
    }
}
