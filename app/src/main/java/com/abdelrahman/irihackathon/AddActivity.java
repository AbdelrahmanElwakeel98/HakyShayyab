 package com.abdelrahman.irihackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdelrahman.irihackathon.Common.Constants;
import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Common.HttpsTrustManager;
import com.abdelrahman.irihackathon.QuestionSection.AddQuestionsActivity;
import com.abdelrahman.irihackathon.QuestionSection.QuestionsActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

 public class AddActivity extends AppCompatActivity {

    private Button upload, add;
    private ImageView back;
    private TextView uploadedName;
    private EditText edtTitle, edtDescription, edtLocation;

    private FilePickerDialog dialog;

    private String media;

     //a constant to track the file chooser intent
     private static final int PICK_IMAGE_REQUEST = 234;

     // instance for firebase storage and StorageReference
     private FirebaseStorage storage;
     private StorageReference storageReference;

     private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        upload = findViewById(R.id.btn_add);
        add = findViewById(R.id.btn_done_add);
        back = findViewById(R.id.back);

        uploadedName = findViewById(R.id.txt_uploaded);

        edtTitle = findViewById(R.id.edt_headline);
        edtDescription = findViewById(R.id.edt_detailed_description);
        edtLocation = findViewById(R.id.edt_location);


        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (Global.categoryManual.equals("Experience")){
            edtLocation.setVisibility(View.VISIBLE);
        }

        auth = FirebaseAuth.getInstance();

        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        properties.show_hidden_files = false;

        dialog = new FilePickerDialog(AddActivity.this,properties);
        dialog.setTitle(R.string.select_file);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                // Defining the child of storageReference

                final StorageReference ref
                        = storageReference
                        .child(UUID.randomUUID().toString());

                Uri file = Uri.fromFile(new File(files[0]));

                files = new String[0];

                 // adding listeners on upload
                // or failure of image
                ref.putFile(file)
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        media = taskSnapshot.getMetadata().toString();

                                        // get the image Url of the file uploaded
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // getting image uri and converting into string
                                                Uri downloadUrl = uri;
                                                media = downloadUrl.toString();

                                                edtTitle.setText(media);


                                            }
                                        });

                                        }
                                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(AddActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                edtTitle.setText(e.getMessage());
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Toast.makeText(AddActivity.this, String.valueOf(progress), Toast.LENGTH_LONG).show();
                    }});
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.chooseManual.equals("Manual")){
                    if (!TextUtils.isEmpty(edtTitle.getText().toString()) &&
                            !TextUtils.isEmpty(edtDescription.getText().toString()))
                        addManual(edtTitle.getText().toString()
                                , Global.categoryManual, auth.getCurrentUser().getUid(), media
                                , edtDescription.getText().toString());
                } else if (Global.chooseManual.equals("Experience")){
                    if (!TextUtils.isEmpty(edtTitle.getText().toString()) &&
                            !TextUtils.isEmpty(edtDescription.getText().toString())
                    && !TextUtils.isEmpty(edtLocation.getText().toString()))
                        addExperience(edtTitle.getText().toString()
                                , Global.categoryManual, auth.getCurrentUser().getUid(), media
                                , edtDescription.getText().toString(), edtLocation.getText().toString());
                }

            }
        });



    }

     private void addManual(final String title, final String categoryID, final String addedBy, final String media, final String body){
         HttpsTrustManager.allowAllSSL();
         String url = Constants.API_URL + "blogs/addBlog";
         StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 Toast.makeText(AddActivity.this, response, Toast.LENGTH_LONG).show();
                 Log.e("Error", response);

                 if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_songs))){
                     startActivity(new Intent(AddActivity.this, SongsActivity.class));
                     finish();
                 } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_medicine))){
                     startActivity(new Intent(AddActivity.this, MedicineActivity.class));
                     finish();
                 } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_cooking))){
                     startActivity(new Intent(AddActivity.this, CookingActivity.class));
                     finish();
                 } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_clothing))){
                     startActivity(new Intent(AddActivity.this, ClothingActivity.class));
                     finish();
                 } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_handcrafting))){
                     startActivity(new Intent(AddActivity.this, CarpetActivity.class));
                     finish();
                 }

             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(AddActivity.this, error+"", Toast.LENGTH_LONG).show();
                 Log.e("Error", error.toString());
             }
         }){
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 HashMap<String, String> params = new HashMap<>();
                 params.put("title", title);
                 params.put("categoryID", categoryID);
                 params.put("body", body);
                 params.put("addedBy", addedBy);
                 params.put("media", media);
                 return params;
             }
         };
         RequestQueue requestQueue = Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
     }

     private void addExperience(final String title, final String categoryID, final String addedBy, final String media
             , final String body, final String location){
         HttpsTrustManager.allowAllSSL();
         String url = Constants.API_URL + "experiences/addExperience";
         StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 Toast.makeText(AddActivity.this, response, Toast.LENGTH_LONG).show();
                 Log.e("Error", response);

                 if (Global.categoryExperience.equals(getResources().getString(R.string.adventure))){
                     startActivity(new Intent(AddActivity.this, AdventureActivity.class));
                     finish();
                 } else if (Global.categoryExperience.equals(getResources().getString(R.string.cooking))){
                     startActivity(new Intent(AddActivity.this, BedouinQuestionsActivity.class));
                     finish();
                 }

             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(AddActivity.this, error+"", Toast.LENGTH_LONG).show();
                 Log.e("Error", error.toString());
             }
         }){
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 HashMap<String, String> params = new HashMap<>();
                 params.put("title", title);
                 params.put("categoryID", categoryID);
                 params.put("body", body);
                 params.put("addedBy", addedBy);
                 params.put("media", media);
                 params.put("location", location);
                 return params;
             }
         };
         RequestQueue requestQueue = Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
     }

     @Override
     public void onBackPressed() {
         if (Global.chooseManual.equals("Manual")){

             if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_songs))){
                 startActivity(new Intent(AddActivity.this, SongsActivity.class));
                 finish();
             } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_medicine))){
                 startActivity(new Intent(AddActivity.this, MedicineActivity.class));
                 finish();
             } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_cooking))){
                 startActivity(new Intent(AddActivity.this, CookingActivity.class));
                 finish();
             } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_clothing))){
                 startActivity(new Intent(AddActivity.this, ClothingActivity.class));
                 finish();
             } else if (Global.categoryManual.equals(getResources().getString(R.string.manual_dashboard_handcrafting))){
                 startActivity(new Intent(AddActivity.this, CarpetActivity.class));
                 finish();
             }
         } else if (Global.chooseManual.equals("Experience")){

             if (Global.categoryExperience.equals(getResources().getString(R.string.adventure))){
                 startActivity(new Intent(AddActivity.this, AdventureActivity.class));
                 finish();
             } else if (Global.categoryExperience.equals(getResources().getString(R.string.cooking))){
                 startActivity(new Intent(AddActivity.this, BedouinQuestionsActivity.class));
                 finish();
             }
         }
     }

     //Add this method to show Dialog when the required permission has been granted to the app.
     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
         switch (requestCode) {
             case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                 if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     if(dialog!=null)
                     {   //Show dialog if the read permission has been granted.
                         dialog.show();
                     }
                 }
                 else {
                     //Permission has not been granted. Notify the user.
                     Toast.makeText(AddActivity.this,"Permission is Required for getting list of files",Toast.LENGTH_SHORT).show();
                 }
             }
         }
     }
}
