package com.example.lab5authenticationiqbal;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class addAchievements extends AppCompatActivity {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    Spinner sport;
    EditText Event_name, title, location, stringsport;
    Button buttonAchieve, btncamera, btngallery;
    ImageView imgachievement;
    FirebaseDatabase Details;
    DatabaseReference DetailsReference;
    StorageReference storageReference;
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achievements);
        Details = FirebaseDatabase.getInstance();
        DetailsReference = Details.getReference("Achievements");
        storageReference = FirebaseStorage.getInstance().getReference();

        Event_name = findViewById(R.id.tournament);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        sport = findViewById(R.id.sport);
        buttonAchieve = findViewById(R.id.btn_add_achievement);
        btncamera = findViewById(R.id.btnCamera);
        btngallery= findViewById(R.id.btnGallery);
        imgachievement=findViewById(R.id.imgachievement);

        List<String> categories = new ArrayList<>();
        categories.add(0, "choose sport");
        categories.add("Rugby");
        categories.add("Cycling");
        categories.add("Badminton");
        categories.add("Running");
        categories.add("Triathlon");
        categories.add("Vollyball");
        categories.add("Football");
        categories.add("Petanque");
        categories.add("Tennis");
        categories.add("Bowling");
        categories.add("Takraw");
        categories.add("Gymnastic");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sport.setAdapter(dataAdapter);

        btncamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                askCameraPermissions();

            }
        });

        btngallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(addAchievements.this,"Image uploaded from gallery", Toast.LENGTH_LONG).show();
            }
        });

        buttonAchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Get all the values
                String AName = Event_name.getText().toString();
                String ATittle = title.getText().toString();
                String Alocation = location.getText().toString();
                String ASport = sport.getSelectedItem().toString();




                if(!TextUtils.isEmpty(AName) & !TextUtils.isEmpty(ATittle) &
                        !TextUtils.isEmpty(Alocation) & !TextUtils.isEmpty(ASport)) {
                    String id = DetailsReference.push().getKey();
                    addAchievementsGetSet achievementDetails = new addAchievementsGetSet(id,AName, ATittle, Alocation, ASport);
                    DetailsReference.child(id).setValue(achievementDetails);
                    Toast.makeText(addAchievements.this, "Achievement has been added",Toast.LENGTH_LONG).show();
                    finish();
                    Intent i = new Intent(getApplicationContext(),Achievements.class);
                    startActivity(i);


                }
                else{

                    Toast.makeText(addAchievements.this, "Please add all the details",Toast.LENGTH_LONG).show();

                }
            }
        });//Register Button method end
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else
        {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults)
    {
        if(requestCode == CAMERA_PERM_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakePictureIntent();
            }
            else
            {
                Toast.makeText(this,"Camera permission is required to use camera",Toast.LENGTH_LONG).show();
            }
        }
    }

    /*private void openCamera()
    {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data)
    {
        if(requestCode == CAMERA_REQUEST_CODE)
        {

                if(resultCode == Activity.RESULT_OK)
                {
                    File f = new File(currentPhotoPath);
                    imgachievement.setImageURI(Uri.fromFile(f));
                    
                    Uri contentUri = Uri.fromFile(f);
                    uploadImageToFirebase(f.getName(), contentUri);
                }


        }
    }

    private void uploadImageToFirebase(final String name, final Uri contentUri) {
        final StorageReference image = storageReference.child("achievements/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag","onSuccess: Uploaded Image URL is "+uri.toString());
                        HashMap hashMap = new HashMap();
                        hashMap.put("AchievementName",name);
                        hashMap.put("ImageUrl",image.toString());


                    }

                });
                Toast.makeText(addAchievements.this, "Image is uploaded",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addAchievements.this, "Failed to upload",Toast.LENGTH_LONG).show();
            }
        });
    }


    private File createImageFile() throws IOException
    {
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

}
