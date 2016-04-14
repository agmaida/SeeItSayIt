package com.example.andrew.seeitsayit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity {

    Button registerSubmit;
    EditText regUsername, regEmail, regPassword, regPasswordConfirm;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //registerSubmit = (Button) findViewById(R.id.registerSubmit);

        //registerSubmit.setOnClickListener(this);

        //regUsername = (EditText) findViewById(R.id.registerUsername);
        //regEmail = (EditText) findViewById(R.id.registerEmail);
        //regPassword = (EditText) findViewById(R.id.registerPassword);
        //regPasswordConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);


        //userLocalStore = new UserLocalStore(this);
    }


}
