package eu.quadran.androidappyusertrackerlibrary.utils;

import java.util.concurrent.TimeUnit;

public class Timer {

    private long startTime = 0;
    private long endTime = 0;
    private long elapsedTime = 0;
    private long timestamp = 0;

    public Timer() {}

    public long getTimestamp() {
        return timestamp;
    }

    private void resetTimer() {
        startTime = 0;
        endTime = 0;
        elapsedTime = 0;
        timestamp = 0;
    }

    public void startTimer() {
        resetTimer();
        startTime = System.nanoTime();
        timestamp = saveCurrentTimestamp();
    }

    public void stopTimer() {
        if (startTime != 0) {
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
        } else resetTimer();
    }

    public long getTotalTimeMillis() {
        return (elapsedTime != 0) ? TimeUnit.NANOSECONDS.toMillis(endTime - startTime) : 0;
    }

    public long saveCurrentTimestamp() {return System.currentTimeMillis() / 1000;}
}
