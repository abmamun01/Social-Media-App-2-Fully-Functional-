package com.mamunsproject.socialmediaapp2.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.mamunsproject.socialmediaapp2.Activity.AskActivity;
import com.mamunsproject.socialmediaapp2.Activity.PrivecyActivity;
import com.mamunsproject.socialmediaapp2.Bottom_sheet_f2;
import com.mamunsproject.socialmediaapp2.QuestionMember;
import com.mamunsproject.socialmediaapp2.R;
import com.mamunsproject.socialmediaapp2.ViewHolder_Question;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class Ask_Fragment extends Fragment {


    FloatingActionButton floatingActionButton;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    CircleImageView imageView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, favourite_Database_refererence, favourite_Database_listReference;
    RecyclerView recyclerView;
    Boolean favouriteChecker = false;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentId = user.getUid();
    ImageButton favouriteBtn;


    QuestionMember member;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ask_, container, false);


        imageView = view.findViewById(R.id.iv_f2);
        recyclerView = view.findViewById(R.id.rv_f2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favouriteBtn = view.findViewById(R.id.fvrt_f2_item);


        databaseReference = firebaseDatabase.getReference("All Questions");

        member = new QuestionMember();
        favourite_Database_refererence = firebaseDatabase.getReference("favourites");
        favourite_Database_listReference = firebaseDatabase.getReference("favouriteList").child(currentId);


        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        documentReference = firebaseFirestore.collection("Users").document(currentId);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), AskActivity.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bottom_sheet_f2 bottom_sheet_f2 = new Bottom_sheet_f2();
                bottom_sheet_f2.show(getFragmentManager(), "bottom");
            }
        });
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    String url = task.getResult().getString("url");

                    Picasso.get().load(url).into(imageView);

                } else {
                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        FirebaseRecyclerOptions<QuestionMember> options = new FirebaseRecyclerOptions.Builder<QuestionMember>()
                .setQuery(databaseReference, QuestionMember.class)
                .build();


        FirebaseRecyclerAdapter<QuestionMember, ViewHolder_Question> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<QuestionMember, ViewHolder_Question>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull ViewHolder_Question holder, int position, @NonNull @NotNull QuestionMember model) {


                        final String postKey = getRef(position).getKey();


                        holder.setItem(getActivity(), model.getName(), model.getUrl(), model.getUserid(), model.getKey(),
                                model.getQuestion(), model.getPrivacy(), model.getTime());
                        String question = getItem(position).getQuestion();
                        String name = getItem(position).getName();
                        String url = getItem(position).getUrl();
                        String time = getItem(position).getTime();
                        String privacy = getItem(position).getPrivacy();
                        String userId = getItem(position).getUserid();


                        holder.favouriteChecker(postKey);
                        holder.favourite_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                favouriteChecker = true;
                                favourite_Database_refererence.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        if (favouriteChecker.equals(true)) {
                                            if (snapshot.child(postKey).hasChild(currentId)) {
                                                favourite_Database_refererence.child(postKey).child(currentId).removeValue();
                                                delete(time);
                                                favouriteChecker = false;
                                            } else {

                                                favourite_Database_refererence.child(postKey).child(currentId).setValue(true);
                                                member.setName(name);
                                                member.setTime(time);
                                                member.setPrivacy(privacy);
                                                member.setUserid(userId);
                                                member.setQuestion(question);

                                                String id = favourite_Database_listReference.push().getKey();
                                                favourite_Database_listReference.child(id).setValue(member);
                                                favouriteChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        });


                    }

                    @NonNull
                    @NotNull
                    @Override
                    public ViewHolder_Question onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);


                        return new ViewHolder_Question(view);
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


    }

    private void delete(String time) {

        Query query = favourite_Database_listReference.orderByChild("time").equalTo(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    dataSnapshot.getRef().removeValue();

                    Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}