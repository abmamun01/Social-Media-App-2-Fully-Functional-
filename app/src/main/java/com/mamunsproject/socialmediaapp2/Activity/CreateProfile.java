package com.mamunsproject.socialmediaapp2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mamunsproject.socialmediaapp2.MainActivity;
import com.mamunsproject.socialmediaapp2.Model.All_User_Member;
import com.mamunsproject.socialmediaapp2.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfile extends AppCompatActivity {

    EditText etName, etBio, etProfession, etEmail, etWeb;
    Button button;
    CircleImageView imageView;
    ProgressBar progressBar;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String currentUserID;
    private static final int PICK_IMAGE = 1;
    All_User_Member member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        member = new All_User_Member();


        imageView = findViewById(R.id.iv_cp);
        etName = findViewById(R.id.et_name_cp);
        etBio = findViewById(R.id.et_bio_cp);
        etProfession = findViewById(R.id.et_profession_cp);
        etEmail = findViewById(R.id.et_email_cp);
        etWeb = findViewById(R.id.et_web_cp);
        button = findViewById(R.id.btn_cp);
        progressBar = findViewById(R.id.progressbar_cp);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = user.getUid();

        documentReference = db.collection("Users").document(currentUserID);
        storageReference = FirebaseStorage.getInstance().getReference("Profile Images");
        databaseReference = firebaseDatabase.getReference("All Users");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);

            }
        });


    }

    private void uploadData() {
        String name =etName.getText().toString();
        String bio =etBio.getText().toString();
        String web =etWeb.getText().toString();
        String prof =etProfession.getText().toString();
        String email =etEmail.getText().toString();


        if(!TextUtils.isEmpty(name)||!TextUtils.isEmpty(bio)||!TextUtils.isEmpty(prof)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(web)||imageUri!=null){
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            uploadTask=reference.putFile(imageUri);

            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {

                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();

                        Map<String, String> profile=new HashMap<>();
                        profile.put("name",name);
                        profile.put("prof",prof);
                        profile.put("url",downloadUri.toString());
                        profile.put("email",email);
                        profile.put("web",web);
                        profile.put("bio",bio);
                        profile.put("privacy","Public");


                        member.setName(name);
                        member.setProf(prof);
                        member.setUid(currentUserID);
                        member.setUrl(downloadUri.toString());


                        databaseReference.child(currentUserID).setValue(member);

                        documentReference.set(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(CreateProfile.this, "Profile Created!", Toast.LENGTH_SHORT).show();

                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(CreateProfile.this, MainActivity.class));
                                            }
                                        },2000);
                                    }
                                });

                    }
                }
            });


        }else {
            Toast.makeText(this, "Please Fill All The Fields!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {

            if (requestCode == PICK_IMAGE || requestCode == RESULT_OK || data != null || data.getData() != null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(imageView);
             //   imageView.setRotation(-85);

            }

        } catch (Exception e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri ));
    }


}
