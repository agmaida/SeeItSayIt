package com.example.andrew.seeitsayit;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by andyd on 3/24/2016.
 */
public class UserLocalStore {

    public static final String SP_EMAIL = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context)
    {
        userLocalDatabase = context.getSharedPreferences(SP_EMAIL, 0);
    }

    public void storedUserData(User user)
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("email", user.email);
        userLocalDatabaseEditor.putString("password", user.password);
        userLocalDatabaseEditor.apply(); /*apply() commits without returning boolean*/
    }

    public void setuserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn",loggedIn);
        userLocalDatabaseEditor.apply();
    }

    public void clearUserData()
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.apply();
    }

    public User getLoggedInUser()
    {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        String email = userLocalDatabase.getString("email", "");
        String password = userLocalDatabase.getString("password", "");

        User user = new User(email, password);
        return user;
    }
}
