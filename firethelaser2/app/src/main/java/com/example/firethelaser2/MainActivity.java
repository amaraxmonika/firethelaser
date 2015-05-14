package com.example.firethelaser2;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements SensorEventListener, View.OnClickListener {

	private static SensorManager sm;
	private Sensor sAccel, sMagnet;
	private float[] mGravity, mGeomagnetic;
	private double prevAzimuth, prevPitch, prevRoll;
    private double centerAzimuth;
	private ClientThread ct;
    private Thread thread;
    private Button mouse;
    private Button click;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setup();
		getSensors();
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
        mouse.setOnClickListener(null);
        click.setOnClickListener(null);
	}

    @Override
    protected void onDestroy(){
        super.onDestroy();
        sm.unregisterListener(this);
        thread.interrupt();
    }

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (mGravity == null) {
				mGravity = event.values;
			} else {
				final float alpha = (float) 0.0001;

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
        double[] axisData = (double[]) this.getIntent().getDoubleArrayExtra("Axis");
		prevAzimuth = centerAzimuth=axisData[0];
		prevPitch = axisData[1];
		prevRoll = axisData[2];
        mouse = (Button) findViewById(R.id.toggle);
        click = (Button) findViewById(R.id.next);
		ct = ClientThread.getInstance(this);
		thread = new Thread(ct);
        thread.start();

	}

	private void getSensors() {
		sAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sMagnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	private void registerListeners() {
		sm.registerListener(this, sAccel, SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(this, sMagnet, SensorManager.SENSOR_DELAY_UI);
        mouse.setOnClickListener(this);
        click.setOnClickListener(this);
	}

	private void doSomeMagic(float[] orientation) {
		if(checkThreshold(orientation)) {
            Log.d("Sensors" , "PrevAzimuth: " + prevAzimuth + "centerAzimuth: " + centerAzimuth);
            prevAzimuth = convertToReadableDegrees(orientation[0]);
            prevPitch = convertToReadableDegrees(orientation[1]);
            prevRoll = convertToReadableDegrees(orientation[2]);
            ct.sendJSONMouseEvent("moveCursorEvent",Math.sin(Math.toRadians(Math.abs(prevRoll))), Math.sin(Math.toRadians(Math.abs(prevPitch))));
        }
    }
	
	private boolean checkThreshold(float[] orientation){
		boolean b_azimuth = (Math.abs((prevAzimuth - convertToReadableDegrees(orientation[0]))) > 2);
		boolean b_pitch = (Math.abs((prevPitch - convertToReadableDegrees(orientation[1]))) > 2);
		boolean b_roll = (Math.abs((prevRoll - convertToReadableDegrees(orientation[2]))) > 2);
		return b_azimuth || b_pitch || b_roll;
	}

    private double convertToReadableDegrees(float rad){
        return (Math.toDegrees(rad)+360)%360;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toggle){
            ct.sendJSONMouseEvent("leftClick",Math.sin(Math.toRadians(Math.abs(prevRoll))), Math.sin(Math.toRadians(Math.abs(prevPitch))));
        }
        if(v.getId() == R.id.next){
            ct.sendButtonMessage("changeCursor");
        }

    }
}
