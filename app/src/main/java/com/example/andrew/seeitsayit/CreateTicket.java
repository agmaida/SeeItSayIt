package com.example.andrew.seeitsayit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTicket extends AppCompatActivity {

    private static final String TAG = "CallCamera";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
    private static String TITLE;
    private static String DESCRIPTION;
    private static String LOCATAION;
    private static String CATEGORY;
    private static String LONGITUDE;
    private static Double LATITUDE;

   // Uri fileUri = null;
    ImageView photoImage = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    TITLE = titleContents.getText().toString();
                    DESCRIPTION = descriptionContents.getText().toString();
                    LOCATAION = addressContents.getText().toString();

                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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


}
