package com.beardownforwhat.touchforlaser;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;
import android.widget.Button;
import android.webkit.WebView;
import android.util.Log;

public class MainActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private Button next;
    private Button prev;
    private ToggleButton laser;
    private WebView wv;
    private boolean tlzr;
    private ClientThread ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLayoutContents();
        setListeners();
        initialize();
    }

    private void initialize() {
        tlzr = laser.isChecked();
        ct = new ClientThread(wv.getWidth(), wv.getHeight());
        new Thread(ct).start();
    }

    private void getLayoutContents() {
        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.prev);
        laser = (ToggleButton) findViewById(R.id.toggle);
        wv = (WebView) findViewById(R.id.ppt);

    }

    private void setListeners() {
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        laser.setOnClickListener(this);
        wv.setOnTouchListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                //go to next slide
                break;
            case R.id.prev:
                //go to previous slide
                break;
            case R.id.toggle:
                tlzr = laser.isChecked();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (tlzr) {
            float x = event.getX();
            float y = event.getY();
            //send coordinates to websocket class along with webview Size
            ct.setX(x);
            ct.setY(y);
            ct.toggleSend();
            ct.run();
            float h = wv.getHeight();
            float w = wv.getWidth();
            //Log.d("touch event: ", "X: " + x + " Y: " + y + " Height: " + h + " Width: " + w);
            return true;
        }
        return false;
    }
}
