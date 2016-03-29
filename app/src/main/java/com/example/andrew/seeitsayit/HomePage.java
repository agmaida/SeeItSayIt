package com.example.andrew.seeitsayit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //This code with the lambda statements is the code for navigation from the Home Page to the Create Ticket page via the Create Ticket button
        Button createTicket = (Button)findViewById(R.id.homeCreateTicket);
        createTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, CreateTicket.class));
            }
        });
    }
}
