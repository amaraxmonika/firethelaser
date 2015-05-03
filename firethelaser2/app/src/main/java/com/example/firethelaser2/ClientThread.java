package com.example.firethelaser2;

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

    @Override
    public void run() {
        if(send) {
            cl.sendMessage(this.x, this.y);
            send = false;
        }
    }

}
