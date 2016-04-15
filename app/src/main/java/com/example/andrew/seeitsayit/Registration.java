package com.example.andrew.seeitsayit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    Button registerSubmit;
    EditText registerEmail, registerPassword, registerPasswordConfirm;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerSubmit = (Button) findViewById(R.id.registerSubmit);
        registerSubmit.setOnClickListener(this);

        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerPasswordConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);


        userLocalStore = new UserLocalStore(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.registerPasswordConfirm:
                String email = registerEmail.getText().toString();
                String password1 = registerPassword.getText().toString();
                String password2 = registerPasswordConfirm.getText().toString();

                if (password1 == password2) {
                    User user = new User(email, password1);
                    createUser(user);
                }
                else{
                    showErrorMessage();
                }


                break;

        }
    }

    private void createUser(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {

                saveUser(returnedUser);
            }
        });
    }

    private void saveUser(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, HomePage.class)); //User goes to this when they log in
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Registration.this);
        dialogBuilder.setMessage("Passwords do not match");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}
