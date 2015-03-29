package com.namvo.shakeutilitylibrary;

import android.content.Context;

/**
 * Created by namvo on 3/29/15.
 */
public class ShakeManagement {
    public static void startShakeDetector(Context context, AbstractTrigger abstractTrigger) {
        ShakeDetector.start(context, abstractTrigger);
    }

    public static void stopShakeDetector() {
        ShakeDetector.stop();
    }

    public static AbstractTrigger getScreenshotTrigger() {
        return ScreenshotTrigger.getInstance();
    }
}
