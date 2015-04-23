package com.beardownforwhat.touchforlaser;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Client {

    private static Client cl;
    private static Socket ws;
    private float cmpSize = 1040;
    private float androidSize;

    private Client(){
        try {
            ws = IO.socket("http://104.236.77.85/");  //needs the URI
        }catch(URISyntaxException e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("URIException: ", sw.toString());
        }
    }

    public static Client getInstance(){
        if (cl == null){
            cl = new Client();
        }
        return cl;
    }

    public void clientConnect(){
        connect();
    }

    public void clientDisconnect(){
        closeWebSocket();
    }

    public void setAndroidSize(float num){
        this.androidSize = num;
    }

    public void sendMessage(float x, float y){
        Log.d("Sending Message:", "x: " + x + " y: " + y + "Connection: " + ws.connected());
        ws.emit("data", "hi");
        if(!ws.connected()){
            connect();
        }
        float cx = (x/androidSize) * cmpSize;
        float cy = (y/androidSize) * cmpSize;
        //send cx and cy as JSON objects
        JSONObject coordinates = new JSONObject();
        try {
            //coordinates.put("x", cx);
            coordinates.put("x:", new Float(x));
            coordinates.put("y:", new Float(y));
        } catch (JSONException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("JSONException: ", sw.toString());
        }
        ws.emit("data", coordinates);
    }


    private void connect() {
        try{
            ws.connect();
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("ConnectionException: ", sw.toString());
        }
        Log.d("Connection:", "" + ws.connected());
        if(ws.connected()){
            Log.d("WebSocketConnection: ", "Connected!");
            ws.emit("data", "connected");
            ws.on("data", onNewMessage); }
        else{
            Log.e("Connection: ", "Not Connected!");
        }
    }

    private void closeWebSocket(){
        ws.disconnect();
        ws.close();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener(){

        @Override
        public void call(Object... args) {
            for(Object arg: args){
                Log.d("arguments: " , arg.toString());
            }
        }
    };

}
