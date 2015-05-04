package com.example.firethelaser2;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by Darshan on 4/30/15.
 */
public class CalibratingActivity extends Activity implements SensorEventListener, LocationListener {

    private static SensorManager sm;
    private static LocationManager lm;
    private Sensor sAccel, sMagnet;
    private float[] mGravity, mGeomagnetic;
    private double[] axis, magnitude;
    private boolean axisSetup, magnitudeSetup;
    private int countMagnitude;
    private Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrating);
        setup();
        getSensors();
        registerListeners();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        sm.unregisterListener(this);
        lm.removeUpdates(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        sm.unregisterListener(this);
        lm.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        loc = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
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
                setUpOrientation(orientation);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setup(){
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        loc = null;
        axisSetup = false;
        magnitudeSetup = false;
        countMagnitude = 0;
    }

    private void getSensors() {
        sAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sMagnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    private void registerListeners() {
        sm.registerListener(this, sAccel, SensorManager.SENSOR_DELAY_UI);
        sm.registerListener(this, sMagnet, SensorManager.SENSOR_DELAY_UI);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2500, 0, this);
    }

    private void setUpOrientation(float[] orientation){
        if(!axisSetup){
            this.axis=setAxis(orientation);
            //if dot is not centered then axisSetup should still be false
            axisSetup = false;
        }else {
            if (!magnitudeSetup) {
                setTextOnActivity("Move the red dot to the blue dots");
                this.magnitude=setMagnitude(orientation);
                countMagnitude++;
                if(countMagnitude == 4){
                    //TODO: Start MainActivity
                    sendMessage();
                }
            }
        }
    }

    private double[] setAxis(float[] orientation){
       //this allows to get a -180 to 180 range
       double azimuth = convertToReadableDegrees(orientation[0]);
       double pitch = convertToReadableDegrees(orientation[1]);
       double roll = convertToReadableDegrees(orientation[2]);
       return new double[]{azimuth,pitch,roll};
    }

    private double[] setMagnitude(float[] orientation){
        double azimuth = convertToReadableDegrees(orientation[0]);
        double pitch = convertToReadableDegrees(orientation[1]);
        double roll = convertToReadableDegrees(orientation[2]);
        return new double[]{azimuth/axis[0], pitch/axis[1], roll/axis[2]};
    }

    private void setTextOnActivity(String instruction){
        TextView mainText = (TextView) findViewById(R.id.instruction);
        mainText.setText(instruction);
    }

    private double convertToReadableDegrees(float rad){
        return (Math.toDegrees(rad)+360)%360;
    }

    private void sendMessage(){
        Intent nextIntent = new Intent(this, MainActivity.class);
        nextIntent.putExtra("Axis", axis);
        nextIntent.putExtra("Location", loc);
        nextIntent.putExtra("Magnitude", magnitude);
        startActivity(nextIntent);
    }
}
