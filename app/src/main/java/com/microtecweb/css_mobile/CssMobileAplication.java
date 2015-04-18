package com.microtecweb.css_mobile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import entity.EConstant;
import entity.ETask;

/**
 * Created by HieuHT on 04/17/2015.
 */
public class CssMobileAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(ETask.class);
        Parse.initialize(this, getResources().getString(R.string.key_application_id) , getResources().getString(R.string.key_client));
        ParseInstallation.getCurrentInstallation().saveInBackground();

        SharedPreferences sharedpreferences = this.getApplicationContext().getSharedPreferences(EConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
        String userToDoList = EConstant.getUserToDoList(sharedpreferences);
        String passwordToDoList = EConstant.getPasswordToDoList(sharedpreferences);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            ParseUser.logInInBackground(userToDoList, passwordToDoList , new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                }
            });
        }
    }
}

