package com.example.andrew.seeitsayit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity  implements View.OnClickListener{

    EditText registerEmail, registerPassword, registerPasswordConfirm;
    Button registerSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerPasswordConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);

        registerSubmit = (Button) findViewById(R.id.registerSubmit);
        registerSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerSubmit:
                String email = registerEmail.getText().toString();
                String password1 = registerPassword.getText().toString();
                String password2 = registerPasswordConfirm.getText().toString();

                User user = new User(email, password1);

                if(!password1.equals(password2))
                {
                    showErrorMessage();
                }
                else
                {
                    if (email.contains("@knights.ucf.edu") && email.contains("@ucf.edu")){
                        registerUser(user);
                    }
                    else{
                        showErrorMessage2();
                    }
                }

                break;
        }
    }

    private void registerUser(User user)
    {
        ServerRequests serverRequests = new ServerRequests(this);
        //Get usercallback is used so we know when the server callback is done
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                Intent loginIntent = new Intent(Registration.this, LoginPage.class);
                startActivity(loginIntent);
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Registration.this);
        dialogBuilder.setMessage("Password do not match.");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void showErrorMessage2() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Registration.this);
        dialogBuilder.setMessage("Email must a UCF email.");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}
