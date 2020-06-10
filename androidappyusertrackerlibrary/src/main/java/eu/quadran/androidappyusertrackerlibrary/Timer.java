package eu.quadran.androidappyusertrackerlibrary;

import java.util.concurrent.TimeUnit;

public class Timer {

    private long startTime = 0;
    private long endTime = 0;
    private long elapsedTime = 0;
    private long timestamp = 0;

    public Timer() {}

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setTimeStamp(long timestamp) {
        this.timestamp = timestamp;
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
