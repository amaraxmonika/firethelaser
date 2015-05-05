package com.example.firethelaser2;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class Client implements Emitter.Listener{

    private static Client cl;
    private static Socket ws;
    private static Observer obv;

    private Client() {
        try {
            ws = IO.socket("http://104.236.77.85/"); // needs the URI
        } catch (URISyntaxException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("URIException: ", sw.toString());
        }
    }

    public static Client getInstance() {
        if (cl == null) {
            cl = new Client();
        }
        return cl;
    }

    public void clientConnect() {
        connect();
    }

    public void clientDisconnect() {
        closeWebSocket();
    }

    public void sendMessage(String event, JSONObject obj) {
        if (!ws.connected())
            connect();
        ws.emit(event, obj.toString());
    }

    public void addThread(Observer obv) {
        this.obv = obv;
    }

    public boolean isClientConnected(){ return ws.connected();}


    private void connect() {
        try {
            ws.connect();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("ConnectionException: ", sw.toString());
        }
        if (ws.connected()) {
            Log.d("WebSocketConnection: ", "Connected!");
            ws.on("messageEvent", this);
        } else {
            Log.e("Connection: ", "Not Connected!");
        }
    }

    private void closeWebSocket() {
        ws.disconnect();
        ws.close();
    }

    @Override
    public void call(Object... args) {
        obv.setJSONObject((JSONObject) args[0]);
    }
}