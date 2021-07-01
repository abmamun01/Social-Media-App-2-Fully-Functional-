package com.mamunsproject.socialmediaapp2.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mamunsproject.socialmediaapp2.Activity.CreateProfile;
import com.mamunsproject.socialmediaapp2.Activity.ImageActivity;
import com.mamunsproject.socialmediaapp2.Activity.UpdateProfile;
import com.mamunsproject.socialmediaapp2.BottomSheetMenu;
import com.mamunsproject.socialmediaapp2.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mamunsproject.socialmediaapp2.Activity.UpdateProfile.updateProfileBoolean;

public class Home_Fragment extends Fragment implements View.OnClickListener {

    CircleImageView imageView;
    TextView nameEt, professionEt, bioEt, emailEt, webSiteEt;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String currentUser_Uid = user.getUid();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static DocumentReference profile_update_reference;

    ImageButton imageButtonEdit, imageButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

        imageView = view.findViewById(R.id.iv_f1);
        nameEt = view.findViewById(R.id.tv_name_f1);
        professionEt = view.findViewById(R.id.tv_profession_f1);
        bioEt = view.findViewById(R.id.tv_bio_f1);
        emailEt = view.findViewById(R.id.tv_email_f1);
        webSiteEt = view.findViewById(R.id.tv_web_f1);
        imageButtonEdit = view.findViewById(R.id.ib_edit_f1);
        imageButton = view.findViewById(R.id.ib_menu_f1);

        Log.d("HNMD", "oncreate: ");


        profile_update_reference = firebaseFirestore.collection("Users").document(currentUser_Uid);
        profile_update_reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                    Picasso.get().load(url).into(imageView);
                    nameEt.setText(nameResult);
                    bioEt.setText(bioResult);
                    emailEt.setText(emailResult);
                    webSiteEt.setText(webResult);
                    professionEt.setText(profResult);


                } else {
                    startActivity(new Intent(getContext(), CreateProfile.class));
                }
            }
        });


        imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), UpdateProfile.class));
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                bottomSheetMenu.show(getFragmentManager(), "bottomsheet");
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ImageActivity.class));
            }
        });

        webSiteEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String url=webSiteEt.getText().toString();
                    Intent intent=new Intent(Intent.ACTION_VIEW );
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }catch (Exception e){

                    Toast.makeText(getContext(), "Invalid Url!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


/*
    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button=button.findViewById(R.id.button);

        button.setOnClickListener(this::onClick);
    }
*/

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onStart() {
        super.onStart();


        if (updateProfileBoolean) {
            updateProfileBoolean=false;
            Log.d("HNMD", "onStart: "+updateProfileBoolean);

            profile_update_reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                        Picasso.get().load(url).into(imageView);
                        nameEt.setText(nameResult);
                        bioEt.setText(bioResult);
                        emailEt.setText(emailResult);
                        webSiteEt.setText(webResult);
                        professionEt.setText(profResult);


                    } else {
                        startActivity(new Intent(getContext(), CreateProfile.class));
                    }
                }

            });

        }
    }
}