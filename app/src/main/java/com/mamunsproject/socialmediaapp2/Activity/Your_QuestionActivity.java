package com.mamunsproject.socialmediaapp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.mamunsproject.socialmediaapp2.QuestionMember;
import com.mamunsproject.socialmediaapp2.R;
import com.mamunsproject.socialmediaapp2.ViewHolder_Question;

import org.jetbrains.annotations.NotNull;

public class Your_QuestionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference,AllQuestions, UserQuestions;
    QuestionMember member;
    DocumentReference documentReference;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserID = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_question);

        recyclerView = findViewById(R.id.rv_related);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //documentReference = firebaseFirestore.collection("Users").document(currentUserID);

        AllQuestions = firebaseDatabase.getReference("All Questions");
        UserQuestions = firebaseDatabase.getReference("User Questions").child(currentUserID);




        databaseReference = firebaseDatabase.getReference("favouriteList").child(currentUserID);


        FirebaseRecyclerOptions<QuestionMember> options = new FirebaseRecyclerOptions.Builder<QuestionMember>()
                .setQuery(databaseReference, QuestionMember.class)
                .build();


        FirebaseRecyclerAdapter<QuestionMember, ViewHolder_Question> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<QuestionMember, ViewHolder_Question>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull ViewHolder_Question holder, int position, @NonNull @NotNull QuestionMember model) {


                        final String postKey = getRef(position).getKey();


                        holder.setItemDeleted(getApplication(), model.getName(), model.getUrl(), model.getUserid(), model.getKey(),
                                model.getQuestion(), model.getPrivacy(), model.getTime());

                        String time = getItem(position).getTime();
                        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deletes(time);
                            }
                        });



                    }

                    @NonNull
                    @NotNull
                    @Override
                    public ViewHolder_Question onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_item, parent, false);


                        return new ViewHolder_Question(view);
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


    }

    private void deletes(String time) {

        Query query = UserQuestions.orderByChild("time").equalTo(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    dataSnapshot.getRef().removeValue();


                    Toast.makeText(Your_QuestionActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        Query query1 = AllQuestions.orderByChild("time").equalTo(time);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    dataSnapshot.getRef().removeValue();


                    Toast.makeText(Your_QuestionActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }



}