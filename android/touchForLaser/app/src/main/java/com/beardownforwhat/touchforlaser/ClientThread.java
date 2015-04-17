package com.beardownforwhat.touchforlaser;

/**
 * Created by Darshan on 4/16/15.
 */
public class ClientThread implements Runnable {
    private volatile float x, y, w, h;
    private Client cl;
    private volatile boolean send;

    public ClientThread(float x, float y){
        this.x = (float) 0.0;
        this.y = (float) 0.0;
        cl = Client.getInstance();
        cl.setAndroidSize(x*y);
        cl.clientConnect();
        this.send = false;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void toggleSend(){
        this.send = true;
    }

    @Override
    public void run() {
        if(send) {
            cl.sendMessage(this.x, this.y);
            send = false;
        }
    }
}
