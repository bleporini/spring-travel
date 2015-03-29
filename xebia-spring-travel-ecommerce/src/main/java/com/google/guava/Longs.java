package com.google.guava;

import java.util.concurrent.TimeUnit;

public class Longs {

    // do not use atomic equivalent so we do trigger hint in the lock view
    private static long value = 0;

    public static long valueOf() {
        long a = value++;
        if ((a % 200) == 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(a);
            } catch (InterruptedException e) {
                //
            }
        }
        return System.currentTimeMillis();
    }
}
