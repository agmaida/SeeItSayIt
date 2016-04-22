package com.example.andrew.seeitsayit;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTicket extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "CallCamera";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
    private static String TITLE;
    private static String DESCRIPTION;
    private static String LOCATAION;
    private static String CATEGORY;
    private static double LONGITUDE;
    private static double LATITUDE;

   // Uri fileUri = null;
    ImageView photoImage = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private LocationManager mLocManager;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    @Override
    //@TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = mLocManager.getBestProvider(criteria, true);

//        try {
//            Location location = null;
//            mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//            if (location == null) {
//                location = mLocManager.getLastKnownLocation(provider);
//            } else {
//                LONGITUDE = location.getLongitude();
//                LATITUDE = location.getLatitude();
//            }
//        }
//        catch (SecurityException e){
//
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        photoImage = (ImageView) findViewById(R.id.photoImage);
        photoImage.setImageDrawable(null);

        Button callCameraButton = (Button) findViewById(R.id.btnAddPhoto);
        callCameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                //startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ);
                dispatchTakePictureIntent();
            }
        });

        Button submitButton = (Button) findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                EditText titleContents = (EditText)findViewById(R.id.createTitle);
                EditText addressContents = (EditText)findViewById(R.id.createAddress);
                EditText descriptionContents = (EditText)findViewById(R.id.createDescription);


                //popup error box "Not all fields are filled in"
                if(titleContents.getText().toString().equals("") || addressContents.getText().toString().equals("") || descriptionContents.getText().toString().equals(""))
                {
                    AlertDialog.Builder notAllFieldsFilledIn = new AlertDialog.Builder(CreateTicket.this);
                    notAllFieldsFilledIn.setMessage("Not all fields are filled in");
                    notAllFieldsFilledIn.setPositiveButton("Ok", null);
                    notAllFieldsFilledIn.show();
                }
                //send to database
                else
                {
                    Spinner createCategory = (Spinner) findViewById(R.id.createCategory);
                    //Ticket t = new Ticket(addressContents.getText().toString(), createCategory.getSelectedItem().toString(), titleContents.getText().toString(), descriptionContents.getText().toString(), 1, LATITUDE, LONGITUDE);

                    try {
//                        JSONObject jsonTicket = new JSONObject();
//                        jsonTicket.put("address", addressContents.getText().toString());
//                        jsonTicket.put("category", createCategory.getSelectedItem().toString());
//                        jsonTicket.put("title", titleContents.getText().toString());
//                        jsonTicket.put("description", descriptionContents.getText().toString());
//                        jsonTicket.put("user_id", 1);
//                        jsonTicket.put("latitude", (float)LATITUDE);
//                        jsonTicket.put("longitude", (float)LONGITUDE);

                        String address = addressContents.getText().toString();
                        String category = createCategory.getSelectedItem().toString();
                        String title = titleContents.getText().toString();
                        String description = descriptionContents.getText().toString();
                        int user_id = 1;
                        //datecreated
                        //dateclosed
                        double latitude =(float)LATITUDE;
                        double longitude = (float)LONGITUDE;
                        Ticket ticket = new Ticket(address, category, title, description, user_id, latitude, longitude);
                        submitTicket(ticket);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        catch(SecurityException e){

        }
        if (mLastLocation != null) {
            LATITUDE = mLastLocation.getLatitude();
            LONGITUDE = mLastLocation.getLongitude();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int x){

    }

    @Override
    public void onConnectionFailed(ConnectionResult r){

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }
            catch (IOException ex){
                //Failed
            }

            //Continue only if the File was saved successfully
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoImage.setImageBitmap(imageBitmap);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location loc){
            LATITUDE = loc.getLatitude();
            LONGITUDE = loc.getLongitude();
            //Do some stuff
        }

        @Override
        public void onProviderDisabled(String provider){
            //do some stuff when the GPS is disabeled

            Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT);
        }

        @Override
        public void onProviderEnabled(String provider){
            Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
            //Empty
        }
    }

    private void submitTicket(Ticket ticket)
    {
        ServerRequests serverRequests = new ServerRequests(this);
        //Get ticketcallback is used so we know when the server callback is done
        serverRequests.StoreTicketDataAsyncTask(ticket, new GetTicketCallback() {
            @Override
            public void done(Ticket returnedUser) {
                Intent mapIntent = new Intent(CreateTicket.this, Map.class);
                startActivity(mapIntent);
            }
        });
    }
}
