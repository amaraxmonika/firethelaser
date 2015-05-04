package com.example.firethelaser2;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ClientThread implements Runnable {
    private volatile double x, y, z;
    private volatile double pax, pay, paz;
    private Client cl;
    private volatile boolean send;

    public ClientThread(){
        this.x = (double) 0.0;
        this.y = (double) 0.0;
        this.z = (double) 0.0;
        cl = Client.getInstance();
        cl.clientConnect();
        this.send = false;
    }

    public void setX(double ax){
            this.x = ax;
    }


    public void setY(double ay){
            this.y = ay;
    }

    public void setZ(double az) {
        this.z = az;
    }

    public void toggleSend(){
        this.send = true;
    }

    public void sendJSONMouseEvent(double x, double y){
        Long cx,cy;
        cx = Math.round(x*50);
        cy = Math.round(y*50);
        JSONObject coordinates = new JSONObject();
		try {
				coordinates.put("x", cx);
				coordinates.put("y", cy);
			}catch (JSONException e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				Log.e("JSONException: ", sw.toString());
		}
    }
    @Override
    public void run() {

    }

}
