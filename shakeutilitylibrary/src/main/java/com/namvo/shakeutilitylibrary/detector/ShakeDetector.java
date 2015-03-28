package com.namvo.shakeutilitylibrary.detector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;

import com.namvo.shakeutilitylibrary.trigger.AbstractTrigger;

/**
 * Created by namvo on 3/28/15.
 */
public class ShakeDetector implements SensorEventListener {
    /*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     */
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;

    private static AbstractTrigger mListener;
    private long mShakeTimestamp;
    private static Context mContext;
    private static SensorManager mSensorManager;
    private static Sensor mAccelerometer;
    private final static ShakeDetector mInstance = new ShakeDetector();

    public static void startShakeDetector(Context context, AbstractTrigger trigger) {
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mListener = trigger;
        mSensorManager.registerListener(mInstance, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public static void stopShakeDetector() {
        mSensorManager.unregisterListener(mInstance);
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                mShakeTimestamp = now;

                mListener.onTriggerListener(mContext);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
