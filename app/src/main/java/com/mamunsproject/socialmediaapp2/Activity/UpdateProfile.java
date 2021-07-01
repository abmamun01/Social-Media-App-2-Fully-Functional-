package com.mamunsproject.socialmediaapp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.mamunsproject.socialmediaapp2.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class UpdateProfile extends AppCompatActivity {

    ImageView imageView;
    public static boolean updateProfileBoolean = false;
    TextView nameEt, professionEt, bioEt, emailEt, webSiteEt;
    Button button;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String currentUser_Uid = user.getUid();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DocumentReference documentReference = firebaseFirestore.collection("Users").document(currentUser_Uid);
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        documentReference.collection("Users").document(currentUser_Uid);

        initialization();
        clickListener();


    }

    private void clickListener() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String name = nameEt.getText().toString();
        String bio = bioEt.getText().toString();
        String prof = professionEt.getText().toString();
        String web = webSiteEt.getText().toString();
        String email = emailEt.getText().toString();


        final DocumentReference sfDocRef = db.collection("Users").document(currentUser_Uid);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                //   DocumentSnapshot snapshot = transaction.get(sfDocRef);

                transaction.update(sfDocRef, "name", name);
                transaction.update(sfDocRef, "prof", prof);
                transaction.update(sfDocRef, "email", email);
                transaction.update(sfDocRef, "web", web);
                transaction.update(sfDocRef, "bio", bio);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d("TAG", "Transaction success!");


                Toast.makeText(UpdateProfile.this, "Updated!", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UpdateProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initialization() {

        nameEt = findViewById(R.id.et_name_up);
        professionEt = findViewById(R.id.et_profession_up);
        bioEt = findViewById(R.id.et_bio_up);
        emailEt = findViewById(R.id.et_email_up);
        webSiteEt = findViewById(R.id.et_web_up);
        button = findViewById(R.id.btn_up);

    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()) {

                    //FETCHING DATA FOR USERS PROFILE
                    String nameResult = task.getResult().getString("name");
                    String bioResult = task.getResult().getString("bio");
                    String emailResult = task.getResult().getString("email");
                    String webResult = task.getResult().getString("web");
                    String url = task.getResult().getString("url");
                    String profResult = task.getResult().getString("prof");

                    //RETRIEVING DATA
                    nameEt.setText(nameResult);
                    bioEt.setText(bioResult);
                    emailEt.setText(emailResult);
                    webSiteEt.setText(webResult);
                    professionEt.setText(profResult);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Profile Exist!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Toast.makeText(UpdateProfile.this, "No Profile Exist!", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Profile Exist!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateProfileBoolean=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateProfileBoolean=true;

    }
}