package com.mamunsproject.socialmediaapp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.mamunsproject.socialmediaapp2.R;

import org.jetbrains.annotations.NotNull;

public class PrivecyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] status = {"Choose Any One", "Public", "Private"};

    TextView status_tv;
    Spinner spinner;
    Button button;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privecy);


        button = findViewById(R.id.btn_privacy);
        status_tv = findViewById(R.id.tv_status);
        spinner = findViewById(R.id.spinner);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUID = user.getUid();

        documentReference = firebaseFirestore.collection("Users").document(currentUID);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, status);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savePrivacy();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Toast.makeText(this, "Please Select A value!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    String privacy_result=task.getResult().getString("privacy");
                    status_tv.setText(privacy_result);
                }else {
                    Toast.makeText(PrivecyActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savePrivacy() {

        final  String value=spinner.getSelectedItem().toString();
        if (value=="Choose Any One"){
            Toast.makeText(this, "Please Select a value!", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String currentUID = user.getUid();




            final DocumentReference sfDocRef = firebaseFirestore.collection("Users").document(currentUID);

            firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    //   DocumentSnapshot snapshot = transaction.get(sfDocRef);

                    transaction.update(sfDocRef, "privacy", value);


                    // Success
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Log.d("TAG", "Transaction success!");


                    Toast.makeText(PrivecyActivity.this, "Status Updated!", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(PrivecyActivity.this, "Staus Failed Update!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}