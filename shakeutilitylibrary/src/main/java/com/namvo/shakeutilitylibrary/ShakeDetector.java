package com.namvo.shakeutilitylibrary;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;

/**
 * Created by namvo on 3/28/15.
 */
class ShakeDetector implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;

    private static AbstractTrigger mAbstractTrigger;
    private long mShakeTimestamp;
    private static Context mContext;
    private static SensorManager mSensorManager;
    private static Sensor mAccelerometer;
    private final static ShakeDetector mInstance = new ShakeDetector();

    public static void start(Context context, AbstractTrigger trigger) {
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAbstractTrigger = trigger;
        mSensorManager.registerListener(mInstance, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public static void stop() {
        mSensorManager.unregisterListener(mInstance);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mAbstractTrigger != null) {
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

                mAbstractTrigger.onTriggerListener(mContext);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
