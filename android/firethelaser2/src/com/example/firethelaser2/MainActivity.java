package com.example.firethelaser2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager sm;
	private Sensor sAccel;
	private Sensor sMagnet;
	private float[] mGravity, mGeomagnetic;
	private float prevAzimuth, prevPitch, prevRoll;
	private ClientThread ct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setup();
		getSensors();
		registerListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerListeners();

	}

	@Override
	protected void onPause() {
		super.onPause();
		sm.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (mGravity == null) {
				mGravity = event.values;
			} else {
				final float alpha = (float) 0.001;

				// Isolate the force of gravity with the low-pass filter.
				mGravity[0] = alpha * mGravity[0] + (1 - alpha)
						* event.values[0];
				mGravity[1] = alpha * mGravity[1] + (1 - alpha)
						* event.values[1];
				mGravity[2] = alpha * mGravity[2] + (1 - alpha)
						* event.values[2];
			}
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			mGeomagnetic = event.values;
		}

		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				doSomeMagic(orientation);
			}
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	
	private void setup(){
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		prevAzimuth = Float.NaN;
		prevPitch = Float.NaN;
		prevRoll = Float.NaN;
		ct = new ClientThread();
		new Thread(ct).start();
	}

	private void getSensors() {
		sAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sMagnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	private void registerListeners() {
		sm.registerListener(this, sAccel, SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(this, sMagnet, SensorManager.SENSOR_DELAY_UI);
	}

	private void doSomeMagic(float[] orientation) {
		if(Float.isNaN(prevAzimuth)) {
			prevAzimuth = orientation[0];
			prevPitch = orientation[1];
			prevRoll = orientation[2];
			Log.d("Sensor:", "azimuth: " + orientation[0]);
			Log.d("Sensor:", "pitch: " + orientation[1]);
			Log.d("Sensor:", "roll: " + orientation[2]);
		}
		else
		{
			if(checkThreshhold(orientation)){
				prevAzimuth = orientation[0];
				prevPitch = orientation[1];
				prevRoll = orientation[2];
				ct.setX(prevPitch);
	            ct.setY(prevRoll);
	            ct.toggleSend();
	            ct.run();
				Log.d("Sensor:", "azimuth: " + orientation[0]);
				Log.d("Sensor:", "pitch: " + orientation[1]);
				Log.d("Sensor:", "roll: " + orientation[2]);
			}
		}
	}
	
	private boolean checkThreshhold(float[] orientation){
		//Log.w("change in azimuth: ", ""+ prevAzimuth + " " + orientation[0]);
		boolean bazimuth = (Math.abs((prevAzimuth - orientation[0])) > 0.05);
		boolean bpitch = (Math.abs((prevPitch - orientation[1])) > 0.05);
		boolean broll = (Math.abs((prevRoll - orientation[2])) > 0.05);
		return bazimuth || bpitch || broll;
	}

}