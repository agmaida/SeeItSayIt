package com.example.andrew.seeitsayit;

/**
 * Created by andyd on 4/10/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRequests {
    ProgressDialog progressDialog;
    public static final String SERVER_ADDRESS = "http://andydng.com/work/seeitsayit/sqlqueries/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchTicketDataAsyncTask(JSONArray returnedTickets, GetTicketsCallback ticketsCallback){
        progressDialog.show();
        new fetchTicketDataAsyncTask(returnedTickets, ticketsCallback).execute();
    }

    public void StoreTicketDataAsyncTask(Ticket returnedTickets, GetTicketCallback ticketCallback){
        progressDialog.show();
        new StoreTicketDataAsyncTask(returnedTickets, ticketCallback).execute();
    }
    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                URL url = new URL(SERVER_ADDRESS + "Register.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); //input from server
                conn.setDoOutput(true); //output to server
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email",user.email)
                        .appendQueryParameter("password",user.password);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(query);

                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                InputStream in = conn.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//                StringBuilder result = new StringBuilder();
//                String line;
//                for(line = reader.readLine(); line!=null; line=reader.readLine())
//                {
//                    result.append(line);
//                }

                in.close();
                conn.disconnect();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            userCallBack.done(null);
        }

    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {

            User returnedUser = null;
            try
            {
                URL url = new URL(SERVER_ADDRESS + "FetchUserData.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); //input from server
                conn.setDoOutput(true); //output to server
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email",user.email)
                        .appendQueryParameter("password",user.password);
                //This builds a query we can write to the server.
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(query);

                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                //Now we pull data from the database and convert it to json
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder result = new StringBuilder();
                String line;
                for(line = reader.readLine(); line!=null; line=reader.readLine())
                {
                    result.append(line);
                }


                try
                {
                    //Nothing is returning from result.
                    JSONObject jObject = new JSONObject(result.toString());
                    if (jObject.length() != 0)
                    {
                        //Log.v("happened", "2");

                        returnedUser = new User(user.email, user.password);
                    }
                }
                catch(JSONException e)
                {
                    //Log.e("MYAPP", "unexpected JSON exception", e);
                    e.printStackTrace();
                }
                in.close();
                conn.disconnect();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }
    }

    public class fetchTicketDataAsyncTask extends AsyncTask<Void, Void, JSONArray> {
//        Ticket ticket;
        GetTicketsCallback ticketsCallback;
        JSONArray returnedTickets;

        public fetchTicketDataAsyncTask(JSONArray returnedTickets, GetTicketsCallback ticketsCallback) {
            this.returnedTickets = returnedTickets;
            this.ticketsCallback = ticketsCallback;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            JSONArray returnedTickets = null;
            //JSONObject ticket = null;
            try
            {
                URL url = new URL(SERVER_ADDRESS + "SelectAllTickets.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); //input from server
                conn.setDoOutput(true); //output to server
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                conn.connect();
                //Now we pull data from the database and convert it to json
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder result = new StringBuilder();
                String line;
                for(line = reader.readLine(); line!=null; line=reader.readLine())
                {
                    result.append(line);
                }

                String outcome = result.toString();
                Log.d("Pull from DB", outcome);

                try
                {
                    JSONArray jArray = new JSONArray(outcome);
                    if (jArray.length() != 0)
                    {
                        returnedTickets = jArray;
                    }
                }
                catch(JSONException e)
                {
                    //Log.e("MYAPP", "unexpected JSON exception", e);
                    e.printStackTrace();
                }
                in.close();
                conn.disconnect();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return returnedTickets;
        }
        @Override
        protected void onPostExecute(JSONArray returnedTickets) {
            super.onPostExecute(returnedTickets);
            progressDialog.dismiss();
            ticketsCallback.done(returnedTickets);
        }
    }

    public class StoreTicketDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Ticket ticket;
        GetTicketCallback ticketCallBack;

        public StoreTicketDataAsyncTask(Ticket ticket, GetTicketCallback ticketCallBack) {
            this.ticket = ticket;
            this.ticketCallBack = ticketCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                URL url = new URL(SERVER_ADDRESS + "SubmitTicket.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); //input from server
                conn.setDoOutput(true); //output to server
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                String UID = "" + ticket.user_id;
                String lat = "" + ticket.latitude;
                String lng = "" + ticket.longitude;

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("address",ticket.address)
                        .appendQueryParameter("category",ticket.category)
                        .appendQueryParameter("title", ticket.title)
                        .appendQueryParameter("description", ticket.description)
                        .appendQueryParameter("user_id", UID)
                        .appendQueryParameter("latitude",lat)
                        .appendQueryParameter("longitude", lng);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(query);

                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                InputStream in = conn.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//                StringBuilder result = new StringBuilder();
//                String line;
//                for(line = reader.readLine(); line!=null; line=reader.readLine())
//                {
//                    result.append(line);
//                }

                in.close();
                conn.disconnect();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            ticketCallBack.done(null);
        }

    }
}