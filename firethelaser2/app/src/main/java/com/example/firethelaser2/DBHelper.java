package com.example.firethelaser2;


import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.mongodb.client.model.Filters.eq;
/**
 * Created by Darshan on 5/4/15.
 */
public class DBHelper {

    //Database name held on the MongoLab server
    private static final String db_name = "firethelaser";
    //API Key from MongoLab
    private static final String api_key = "kbhWpGB_TvRdDtI8OY8wVNX7HB0Ndf1F";
    /*
     * Collection listed within the DB
     * Collections are the "tables" of the db to store information in the form of
     * a JSON object
     */
    private static String user_collection = "Users";

    /**
     * Return the database name
     */
    public String getDb_name () {
        return db_name;
    }

    /**
     * Return the API from MongoLab
     */
    public String getAPI() {
        return api_key;
    }

    /**
     * Constructs the full URL to connect to the Collection "Users"
     * on the MongoLab server.
     */
    public String getURL() {
        //Create the base of the URL
        String dbURL = "https://api.mongolab.com/api/1/databases/";
        //add the section which leads to the collection of USER documents
        dbURL += db_name+"collections/"+user_collection;
        //add the API key to the URL
        dbURL += api_key;
        return dbURL;
    }

    /**
     * Construct the INSERT string for a new USER into the db
     * Includes:
     *    -fName : first name
     *    -lName : last name
     *    -Password
     *    -Role : to be decided from the insert call, can have student or teacher
     */
    public boolean insertUser (String email, String password, String role) {

        Client myClient = Client.getInstance();
        myClient.clientConnect();
        JSONObject userDoc = new JSONObject();
        try {
            userDoc.put("email", email);
            userDoc.put("password", password);
            userDoc.put("role", role);
        } catch (JSONException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("JSONException: ", sw.toString());
        }
        if (userDoc != null) {
            myClient.sendMessage("queryUser", userDoc);

            try {
                JSONObject mess = null;
                while ((mess = myClient.getdataMessage()) == null) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (myClient.isClientConnected()) {
                myClient.clientDisconnect();
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Query the DB using the given name and password to return a
     * Document containing the User's information
     *
     * Returns NULL when the user doesn't exist or a failed login attempt
     */
    public JSONObject getUser (String email, String pwd) {
        Client myClient = Client.getInstance();
        myClient.clientConnect();
        JSONObject userDoc = new JSONObject();
        try {
            userDoc.put("email", email);
            userDoc.put("password", pwd);
        } catch (JSONException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("JSONException: ", sw.toString());
        }
        if (userDoc != null) {
            myClient.sendMessage("queryUser", userDoc);

            try {
                JSONObject mess = null;
                while ((mess = myClient.getdataMessage()) == null) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (myClient.isClientConnected()) {
                myClient.clientDisconnect();
            }

            return userDoc;
        } else {
            return null;
        }
    }

    /**
     * Given the email address of the user when they attempt to register a new account,
     * will query the database and return whether the desired email exists or not.
     */
    public boolean doesEmailExist(String email) {
        boolean exists = false;
        try {//Create the MongoClient to access the DB
            MongoClient myClient = new MongoClient(new MongoClientURI("mongodb://general:firethelaser@ds053139.mongolab.com:53139/firethelaser"));
            //Connect the firethelaser database to gain access to the collections
            MongoDatabase userDB = myClient.getDatabase("firethelaser");
            //Access the USERS collection to gain access to each document
            MongoCollection<Document> userCollection = userDB.getCollection("Users");

            //Query the database in order to see if the user exists
            Document testUser = userCollection.find(eq("email", email)).first();
            //Since we only need to check if a user exists for one return document
            //There is no need for a cursor
            if ( testUser != null ) {
                exists = true;
            }
            //close the MongoClient
            if (myClient != null) {
                myClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }
}
