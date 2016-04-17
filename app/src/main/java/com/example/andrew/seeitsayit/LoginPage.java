package com.example.andrew.seeitsayit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends AppCompatActivity implements View.OnClickListener{

    Button loginSignIn, loginRegisterNewAccount, loginRetrieveAccount;
    EditText loginUsername, loginPassword;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginSignIn = (Button) findViewById(R.id.loginSignIn);
        loginRegisterNewAccount = (Button) findViewById(R.id.loginRegisterNewAccount);
        loginRetrieveAccount = (Button) findViewById(R.id.loginRetrieveAccount);

        loginSignIn.setOnClickListener(this);
        loginRegisterNewAccount.setOnClickListener(this);
        loginRetrieveAccount.setOnClickListener(this);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginSignIn:
                String email = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();
                User user = new User(email, password);

//                String address = "qlfjlejf";
//                String category = "qlfjlejf";
//                String title = "qlfjlejf";
//                String description = "qlfjlejf";
//                int user_id = 2;
//                float latitude = 5.555555f;
//                float longtitude = 5.555555f;
//

//                Ticket ticket = new Ticket(address, category, title, description,
//                        user_id, latitude, longtitude);
//                showTickets();
                authenticate(user);

                break;
            case R.id.loginRegisterNewAccount:
                Intent registerIntent = new Intent(LoginPage.this, Registration.class);
                startActivity(registerIntent);
                break;
        }
    }

    private void authenticate(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

//    private void showTickets() {
//        ServerRequests serverRequest = new ServerRequests(this);
//        serverRequest.fetchTickets(new GetTicketCallback() {
//            @Override
//            public void done(Ticket returnedTicket) {
//                if (returnedTicket == null) {
//                    showErrorMessage();
//                } else {
//                    showFetchTickets(returnedTicket);
//                }
//            }
//        });
//    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginPage.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, HomePage.class)); //User goes to this when they log in
    }

    private void showFetchTickets(Ticket returnedTicket){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginPage.this);
        dialogBuilder.setMessage(returnedTicket.toString());
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();

    }
}
