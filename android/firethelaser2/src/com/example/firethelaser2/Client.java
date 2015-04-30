package com.example.firethelaser2;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class Client {

	private static Client cl;
	private static Socket ws;
	private float prevX;
	private float prevY;
	private static final String sx = "x";
	private static final String sy = "y";

	private Client() {
		try {
			ws = IO.socket("http://104.236.77.85/"); // needs the URI
		} catch (URISyntaxException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			Log.e("URIException: ", sw.toString());
		}
		prevX = Float.NaN;
		prevY = Float.NaN;
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


	public void sendMessage(float x, float y) {
		if (!ws.connected()) {
			connect();
		}
		if (prevX != Float.NaN && prevY != Float.NaN) {
			Log.d("Sending Message:", "x: " + x + " y: " + y + "Connection: "
					+ ws.connected());
			Float cx = prevX - x;
			Float cy = prevY - y;
			prevX = x;
			prevY = y;
			
			JSONObject coordinates = new JSONObject();
			try {
				// coordinates.put("x", cx);
				coordinates.put(sx, cx);
				coordinates.put(sy, cy);
			} catch (JSONException e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				Log.e("JSONException: ", sw.toString());
			}
			ws.emit("moveCursorEvent", coordinates.toString());
		} else {
			prevX = x;
			prevY = y;
			Log.d("Initial touch:", "not sending message: " + x + " " + y);
		}
	}

	private void connect() {
		try {
			ws.connect();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			Log.e("ConnectionException: ", sw.toString());
		}
		Log.d("Connection:", "" + ws.connected());
		if (ws.connected()) {
			Log.d("WebSocketConnection: ", "Connected!");
			ws.emit("data", "connected");
			ws.on("data", onNewMessage);
		} else {
			Log.e("Connection: ", "Not Connected!");
		}
	}

	private void closeWebSocket() {
		ws.disconnect();
		ws.close();
	}

	private Emitter.Listener onNewMessage = new Emitter.Listener() {

		@Override
		public void call(Object... args) {
			for (Object arg : args) {
				Log.d("arguments: ", arg.toString());
			}
		}
	};

}
