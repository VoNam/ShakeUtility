package com.namvo.shakeutilitylibrary.trigger;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by namvo on 3/29/15.
 */
public class ScreenshotTrigger extends AbstractTrigger {
    private static final String TAG = ScreenshotTrigger.class.getSimpleName();
    private final static ScreenshotTrigger mInstance = new ScreenshotTrigger();

    public static ScreenshotTrigger getInstance() {
        return mInstance;
    }
    @Override
    public void onTriggerListener(Context context) {
        Log.d(TAG, "Shake");
        final View rootView = ((Activity)context).getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        final Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        File outputDir = new File(android.os.Environment.getExternalStorageDirectory(), "screenshot");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        File outputFile = new File(outputDir.getAbsolutePath(), "screenshot.png");
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "DONE");
    }
}
