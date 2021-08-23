package com.mamunsproject.socialmediaapp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mamunsproject.socialmediaapp2.QuestionMember;
import com.mamunsproject.socialmediaapp2.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AskActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference AllQuestions, UserQuestions;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    QuestionMember member;
    String name, url, privacy, uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();


        editText = findViewById(R.id.ask_et_question);
        button = findViewById(R.id.btn_submit);


        documentReference = firebaseFirestore.collection("Users").document(currentId);

        AllQuestions = firebaseDatabase.getReference("All Questions");
        UserQuestions = firebaseDatabase.getReference("User Questions").child(currentId);

        member = new QuestionMember();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = editText.getText().toString();

                Calendar cDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MMMM/yyyy");
                final String saveDate = currentDate.format(cDate.getTime());

                Calendar cTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                final String saveTime = currentTime.format(cTime.getTime());

                String time = saveDate + ":" + saveTime;


                if (question != null) {

                    member.setQuestion(question);
                    member.setName(name);
                    member.setPrivacy(privacy);
                    member.setUrl(url);
                    member.setUserid(uid);
                    member.setTime(time);

                    String id = UserQuestions.push().getKey();
                    UserQuestions.child(id).setValue(member);


                    String child = AllQuestions.push().getKey();
                    member.setKey(id);
                    AllQuestions.child(child).setValue(member);


                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }


                    Toast.makeText(AskActivity.this, "Submitted!", Toast.LENGTH_SHORT).show();
                    editText.setText("");

                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();

                        }
                    },1000);


                } else {

                    Toast.makeText(AskActivity.this, "Please Ask Something!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()) {

                    //FETCHING DATA FOR USERS PROFILE
                    name = task.getResult().getString("name");
                    url = task.getResult().getString("url");
                    privacy = task.getResult().getString("privacy");
                    uid = task.getResult().getString("uid");

                    //RETRIEVING DATA


                } else {
                    Toast.makeText(AskActivity.this, "Error!", Toast.LENGTH_SHORT).show();


                }

            }
        });

    }
}