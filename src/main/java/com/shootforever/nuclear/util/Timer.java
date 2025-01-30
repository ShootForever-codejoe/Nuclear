package com.shootforever.nuclear.util;

public class Timer {
    public long lastMS = System.currentTimeMillis();

    public Timer() {
        reset();
    }

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public void resetMS() {
        lastMS = System.nanoTime();
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis() - lastMS > time) {
            if (reset) {
                reset();
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean delay(long time) {
        return System.currentTimeMillis() - lastMS >= time;
    }

    public boolean passedS(double s) {
        return getMs(System.nanoTime() - lastMS) >= (long) (s * 1000.0);
    }

    public boolean passedMs(long ms) {
        return getMs(System.nanoTime() - lastMS) >= ms;
    }

    public boolean hasTimeElapsed(long time) {
        return System.currentTimeMillis() - lastMS > time;
    }

    public boolean hasTimeElapsed(double time) {
        return hasTimeElapsed((long) time);
    }

    public long getTime() {
        return System.currentTimeMillis() - lastMS;
    }

    public void setTime(long time) {
        lastMS = time;
    }

    public long getPassedTimeMs() {
        return getMs(System.nanoTime() - lastMS);
    }

    public long getMs(long time) {
        return time / 1000000L;
    }
}
