package com.example.firethelaser2;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

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

        try {
            //Connect to a single mongoClient
            MongoClient myClient = new MongoClient(new MongoClientURI("mongodb://general:firethelaser@ds053139.mongolab.com:53139/firethelaser"));
            //Declare the MongoDB to use to perform updates to the firethelaser db
            MongoDatabase userDB = myClient.getDatabase("firethelaser");
            //Get the USERS collection
            MongoCollection<Document> userCollection = userDB.getCollection("Users");

            /*
             * Uncomment this when creating the user_id along with the new user account.
            //Query DB to find the userCount
            Document dbUserCount = userCollection.find(eq("user_id","userCount")).first();
            int userCount = dbUserCount.getInteger("user_id");
            mesages//Using userCount
            //Using the userCollection, insert a new user document into the DB
            */
            //Create a newUser document to insert into the firethelaser Db
            Document newUser = new Document("user_id", "u00000")
                    .append("email",""+email)
                    .append("password",""+password)
                    .append("role", ""+role );

            userCollection.insertOne(newUser);
            if (myClient != null) {
                myClient.close();
            }
        } catch (Exception e) {
            System.out.println("Failed to connect via URI");
            return false;
        }
        return true;
    }

    /**
     * Query the DB using the given name and password to return a
     * Document containing the User's information
     *
     * Returns NULL when the user doesn't exist or a failed login attempt
     */
    public Document getUser (String email, String pwd) {
        //Format the URI to connect to the appropriate DB
        MongoClientURI ftlURI = new MongoClientURI("mongodb://general:firethelaser@ds053139.mongolab.com:53139/firethelaser");
        //Create the MongoClient to access the DB
        MongoClient myClient = new MongoClient(ftlURI);
        //Connect the firethelaser database to gain access to the collections
        MongoDatabase userDB = myClient.getDatabase("firethelaser");
        //Access the USERS collection to gain access to each document
        MongoCollection<Document> userCollection = userDB.getCollection("Users");

        //Query DB to find the desired user
        Document currUser = userCollection.find(eq("email", email)).first();
        if (myClient != null) {
            myClient.close();
        }
        if ( currUser != null && currUser.getString("password").equals(pwd)) {
            return currUser;
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
