package com.example.firethelaser2;

import org.json.JSONObject;

/**
 * Created by Darshan on 5/4/15.
 */
public abstract class Observer {
    protected Client cl;

    public abstract void setJSONObject(JSONObject obj);
}
