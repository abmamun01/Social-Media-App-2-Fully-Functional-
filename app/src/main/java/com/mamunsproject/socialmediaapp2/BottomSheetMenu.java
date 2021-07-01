package com.mamunsproject.socialmediaapp2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mamunsproject.socialmediaapp2.Activity.Login_Activity;
import com.mamunsproject.socialmediaapp2.Activity.PrivecyActivity;

import org.jetbrains.annotations.NotNull;

public class BottomSheetMenu extends BottomSheetDialogFragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    CardView cv_privacy, cv_logout, cv_delete;
    FirebaseAuth mAuth;
    String url;
    DatabaseReference databaseReference;



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_menu, null);

        databaseReference= FirebaseDatabase.getInstance().getReference("All Users");
        cv_delete=view.findViewById(R.id.cv_delete);
        cv_logout=view.findViewById(R.id.cv_logOut);
        cv_privacy=view.findViewById(R.id.cv_privacy);
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();
        String currentUserID=user.getUid();


        documentReference=db.collection("Users").document(currentUserID);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            url=task.getResult().getString("url");
                        }else {

                            
                        }
                    }
                });

        cv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


        cv_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), PrivecyActivity.class));
            }
        });

        cv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context=getContext();
                AlertDialog.Builder builder=new AlertDialog.Builder(context);

                builder.setTitle("Delete")
                        .setMessage("Are you sure to Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                           documentReference.delete()
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {

                                           Query query=databaseReference.orderByChild("uid")
                                                   .equalTo(currentUserID);
                                           query.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                   for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                       dataSnapshot.getRef().removeValue();
                                                   }
                                               }

                                               @Override
                                               public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                               }
                                           });
                                           StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                                           storageReference.delete()
                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                           if(task.isSuccessful()){

                                                               Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                                                           }else {
                                                               Toast.makeText(context, "Failed To Delete!", Toast.LENGTH_SHORT).show();
                                                           }

                                                       }
                                                   });
                                       }
                                   });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create();
                builder.show();
            }
        });
        return view;
    }

    private void logOut() {

        Context context=getContext();
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        builder.setTitle("Logout")
                .setMessage("Are you sure logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mAuth.signOut();
                        startActivity(new Intent(getContext(), Login_Activity.class));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create();
        builder.show();
    }
}
