package com.example.firethelaser2;

public class ClientThread implements Runnable {
    private volatile float x, y;
    private Client cl;
    private volatile boolean send;

    public ClientThread(){
        this.x = (float) 0.0;
        this.y = (float) 0.0;
        cl = Client.getInstance();
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
