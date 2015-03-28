package com.namvo.shakeutility.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.namvo.shakeutility.R;
import com.namvo.shakeutilitylibrary.detector.ShakeDetector;
import com.namvo.shakeutilitylibrary.trigger.ScreenshotTrigger;

public class MainActivity extends ActionBarActivity {
        private static final String TAG = MainActivity.class.getSimpleName();
        private ShakeDetector.OnShakeListener mShakeListener = new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(final int count) {


            }
        };

        private RelativeLayout mRootRelativeLayout;
        private ImageView mScreenShotImageView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mRootRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutRoot);
            mScreenShotImageView = (ImageView) findViewById(R.id.imageViewScreenshot);
        }

        @Override
        protected void onResume() {
            super.onResume();
            ShakeDetector.startShakeDetector(MainActivity.this, ScreenshotTrigger.getInstance());
        }

        @Override
        protected void onPause() {
            super.onPause();
            ShakeDetector.stopShakeDetector();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
