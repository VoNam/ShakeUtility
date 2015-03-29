package com.namvo.shakeutilitylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by namvo on 3/29/15.
 */
class ScreenshotTrigger extends AbstractTrigger {
    private static final String TAG = ScreenshotTrigger.class.getSimpleName();

    private static final String TEMP_DIRECTORY = android.os.Environment.getExternalStorageDirectory() + "/screenshot";
    private static final String TEMP_FILE_NAME = "screenshot.png";

    private final static ScreenshotTrigger mInstance = new ScreenshotTrigger();

    public static ScreenshotTrigger getInstance() {
        return mInstance;
    }
    @Override
    public void onTriggerListener(Context context) {
        final View rootView = ((Activity)context).getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        final Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        File outputDir = new File(TEMP_DIRECTORY);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        File outputFile = new File(outputDir.getAbsolutePath(), TEMP_FILE_NAME);
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

        Uri imagePath = Uri.fromFile(outputFile);
        SkitchIntegration skitchIntegration = new SkitchIntegration();
        skitchIntegration.startSkitch(context, imagePath);
    }

    private class SkitchIntegration {
        private static final String SKITCH_PACKAGE = "com.evernote.skitch";

        private boolean isSkitchInstalled(Context context) {
            PackageManager pm = context.getPackageManager();
            try {
                pm.getPackageInfo(SKITCH_PACKAGE, 0);
                return true;
            } catch (PackageManager.NameNotFoundException nnfe) {
                Log.d(TAG, SKITCH_PACKAGE + " is not installed");
            }
            return false;
        }

        private Intent createShareIntent(Uri uri) {
            Intent shareIntent = new Intent(Intent.ACTION_EDIT);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("image/*");
            shareIntent.setPackage(SKITCH_PACKAGE);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            return shareIntent;
        }

        private void startDownloadSkitch(Context context) {
            Toast.makeText(context, "Please update or install Skitch", Toast.LENGTH_LONG).show();
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ SKITCH_PACKAGE));
            context.startActivity(marketIntent);
        }

        private void startSkitchEdit(Context context, Uri imagePath) {
            Intent sendIntent = createShareIntent(imagePath);
            context.startActivity(sendIntent);
        }

        private void startSkitch(Context context, Uri imagePath) {
            if(isSkitchInstalled(context)){
                startSkitchEdit(context, imagePath);
            } else {
                startDownloadSkitch(context);
            }
        }
    }
}
