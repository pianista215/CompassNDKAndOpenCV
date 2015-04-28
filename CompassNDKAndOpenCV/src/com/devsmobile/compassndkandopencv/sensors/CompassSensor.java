package com.devsmobile.compassndkandopencv.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class CompassSensor implements SensorEventListener {

	private static final String TAG = "CompassNDK::CompassSensor";

	/**
	 * Sensor manager
	 */
	private SensorManager mSensorManager;

	private Sensor accelerometer;
	private Sensor magnetometer;

	/**
	 * Singleton
	 */
	private static CompassSensor instance;

	/**
	 * Access to singleton
	 * 
	 * @param ctx
	 *            Main context
	 * @return
	 */
	public static CompassSensor getInstance(Context ctx) {
		if (instance == null) {
			instance = new CompassSensor(ctx);
		}
		return instance;
	}

	/**
	 * Hide constructor
	 * 
	 * @param ctx
	 */
	private CompassSensor(Context ctx) {
		mSensorManager = (SensorManager) ctx
				.getSystemService(Context.SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	/**
	 * Start to capture events
	 */
	public void start() {
		Log.d(TAG, "Starting listener for compass and accelerometer");
		mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_UI);
	}

	/**
	 * Stop to capture events
	 */
	public void stop() {
		Log.d(TAG,"Stopping listener for compass and accelerometer");
		mSensorManager.unregisterListener(this);
	}
	
	/**
	 * Last gravity retrieved
	 */
	private float[] mGravity;
	
	/**
	 * Last geomagnetic retrieved
	 */
	private float[] mGeomagnetic;

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		//Thanks http://www.codingforandroid.com/2011/01/using-orientation-sensors-simple.html
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			Log.d(TAG,"Event from accelerometer");
			mGravity = event.values;
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			Log.d(TAG,"Event from compass");
			mGeomagnetic = event.values;
		}
		
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				float azimuthInDegress = (float)Math.toDegrees(orientation[0]);
				if (azimuthInDegress < 0.0f) {
				    azimuthInDegress += 360.0f;
				}
				Log.d(TAG,"Azimut:"+azimuthInDegress);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(TAG, "Accuracy changed:" + accuracy);
	}

}
