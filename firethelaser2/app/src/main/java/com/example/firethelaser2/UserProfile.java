package com.example.firethelaser2;

/**
 * Created by Darshan on 5/4/15.
 */
public class UserProfile {

        private String user_email;
        private String user_password;
        private String user_role;

        //Constructor to initialize a user with their email and password
        public UserProfile (String email, String pwd, String role) {

            user_email = email;
            user_password = pwd;
            user_role = role;
        }

        /**
         * Change a User's email or password
         *
         * NOTE: Can be added/implemented later if we ever allow a user to
         * change their profile settings
         *
         public void changeEmail (String email) {
         user_email = email;
         }
         public void changePassword (String pwd) {
         user_password = pwd;
         }
         */

        /**
         * Returns the Users current email
         */
        public String getUser_email () {
            return user_email;
        }
        /**
         * Returns the Users email password
         */
        public String getUser_password () {
            return user_password;
        }
        /**
         * Returns this User's role
         */
        public String getUser_role () {
            return user_role;
        }
}
