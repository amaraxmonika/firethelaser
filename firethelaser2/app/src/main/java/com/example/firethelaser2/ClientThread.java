package com.example.firethelaser2;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ClientThread extends Observer implements Runnable {
    private Client cl;
    private JSONObject serverMessage;
    private boolean readyToSend, receivedMessage, buttonMessage;
    private JSONObject clientMessage;
    private static Activity currentAct;
    private static ClientThread ct;
    private String eventMessage;

    private ClientThread(Activity act){
        cl = Client.getInstance();
        if(!cl.isClientConnected()){
            cl.clientConnect();
        }
        cl.addThread(this);
        readyToSend = receivedMessage = buttonMessage = false;
        currentAct = act;
    }

    public static ClientThread getInstance(Activity act){
        if(ct == null){
            ct = new ClientThread(act);
        }
        else{
            setActivity(act);
        }
        return ct;
    }

    public static void setActivity(Activity act){
        currentAct = act;
    }


    public void sendJSONMouseEvent(String event, double x, double y){
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
        clientMessage = coordinates;
        readyToSend = true;
        eventMessage = event;
    }

    public void sendButtonMessage(String event){
        eventMessage = event;
        buttonMessage = true;
    }

    @Override
    public void run() {
        while(true){
            if(readyToSend){
                cl.sendMessage(eventMessage, clientMessage);
                readyToSend = false;
            }
            if(receivedMessage) {
                if(currentAct instanceof MainActivity){
                    //TODO: Do something with Server JSON Object
                }
            }
            if(buttonMessage){
                cl.emitEvent(eventMessage);
                buttonMessage = false;
            }
        }
    }


    @Override
    public void setJSONObject(JSONObject obj) {
        serverMessage = obj;
        receivedMessage = true;
    }
}
