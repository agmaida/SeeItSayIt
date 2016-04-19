package com.example.andrew.seeitsayit;

import android.app.AlertDialog;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //moves camera to UCF
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.602922, -81.199910), 14.0f));
        JSONArray jArray = null;
        showTickets(jArray);
    }

    public void addTicketMarker(float latitude, float longitude, String title, String description)
    {

        LatLng latlng= new LatLng(latitude,longitude);
        //mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions()
                .title(title)
                .snippet(description)
                .position(latlng));
    }

        private void showTickets(JSONArray jArray) {
            ServerRequests serverRequest = new ServerRequests(this);
            serverRequest.fetchTicketDataAsyncTask(jArray, new GetTicketCallback() {
            @Override
            public void done(JSONArray returnedTickets) {
                if (returnedTickets == null) {
                    showErrorMessage();
                } else {
                    showSomething(returnedTickets);
                }
            }
        });

    }
    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Map.this);
        dialogBuilder.setMessage("Unable to load map details.");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void showSomething(JSONArray returnedTickets){
        try {
            for(int i = 0; i < returnedTickets.length(); i++) {
                //Grab each ticket
                //Error around here trying to grab data.
                JSONObject ticket = returnedTickets.getJSONObject(i);

                for (int h = 0; h < ticket.length(); h++) {
                    //Break down each ticket
                    JSONObject ticketDeets = returnedTickets.getJSONObject(h);
                    String lat = ticketDeets.getString("latitude");
                    float latitude = Float.parseFloat(lat);
                    String lng = ticketDeets.getString("longitude");
                    float longitude = Float.parseFloat(lng);
                    String title = ticketDeets.getString("title");
                    String description = ticketDeets.getString("description");
                    addTicketMarker(latitude,longitude,title,description);
                }
            }
        }
        catch(JSONException e)
        {
            //Log.e("MYAPP", "unexpected JSON exception", e);
            e.printStackTrace();
        }

    }
}
