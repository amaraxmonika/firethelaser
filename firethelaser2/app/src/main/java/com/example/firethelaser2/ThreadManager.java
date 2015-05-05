package com.example.firethelaser2;

import java.util.List;
import java.util.ArrayList;
/**
 * Created by Darshan on 5/4/15.
 */
public class ThreadManager {

    private List<Thread> threads = new ArrayList<Thread>();
    private static ThreadManager tm;

    private ThreadManager(){
        tm = new ThreadManager();
    }

    public static ThreadManager getInstance(){
        if (tm==null){
            tm = new ThreadManager();
        }
        return tm;
    }

    public void attachRunnable(Runnable obj){
        threads.add(new Thread(obj));
    }

    public void start(){
        for (Thread t: threads){
            t.start();
        }
    }
}
