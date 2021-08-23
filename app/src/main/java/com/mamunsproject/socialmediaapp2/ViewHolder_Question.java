package com.mamunsproject.socialmediaapp2;

import android.app.Application;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ViewHolder_Question extends RecyclerView.ViewHolder {

    ImageView imageView;
    public TextView time_result,name_result,question_result,deleteBtn;
    public ImageButton favourite_btn;
    DatabaseReference favouriteDatabaseReference;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String uID=user.getUid();



    public ViewHolder_Question(@NonNull @NotNull View itemView) {
        super(itemView);


    }



    public void setItem(FragmentActivity activity,String name,String url,String userId,String key,String question,String privacy,String time){

        imageView=itemView.findViewById(R.id.iv_question_item);
        time_result=itemView.findViewById(R.id.time_question_item_tv);
        name_result=itemView.findViewById(R.id.name_question_item_tv);
        question_result=itemView.findViewById(R.id.question_item_tv);
     //   favourite_btn=itemView.findViewById(R.id.fvrt_f2_item);

        Picasso.get().load(url).into(imageView);
        time_result.setText(time);
        name_result.setText(name);
        question_result.setText(question);
    }


    public void setItemRelated(Application activity, String name, String url, String userId, String key, String question, String privacy, String time){

        TextView timeTv=itemView.findViewById(R.id.related_time_question_item_tv);
        ImageView imageView=itemView.findViewById(R.id.related_iv_question_item);
        TextView nameTv=itemView.findViewById(R.id.related_name_question_item_tv);
        TextView questionTv=itemView.findViewById(R.id.related_question_item_tv);
        TextView replyBtn=itemView.findViewById(R.id.related_reply_item_question);

        Picasso.get().load(url).into(imageView);
        nameTv.setText(name);
        timeTv.setText(time);
        questionTv.setText(question);


    }


    public void setItemDeleted(Application activity, String name, String url, String userId, String key, String question, String privacy, String time){

        TextView timeTv=itemView.findViewById(R.id.your_question_time_question_item_tv);
        ImageView imageView=itemView.findViewById(R.id.your_question_iv_question_item);
        TextView nameTv=itemView.findViewById(R.id.your_question_name_question_item_tv);
        TextView questionTv=itemView.findViewById(R.id.your_question_question_item_tv);
         deleteBtn=itemView.findViewById(R.id.your_question_delete_item_question);

        Picasso.get().load(url).into(imageView);
        nameTv.setText(name);
        timeTv.setText(time);
        questionTv.setText(question);


    }

    public void favouriteChecker(String postKey) {

        favourite_btn=itemView.findViewById(R.id.fvrt_f2_item);
        favouriteDatabaseReference = firebaseDatabase.getReference("favourites");


        favouriteDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child(postKey).hasChild(uID)){
                    favourite_btn.setImageResource(R.drawable.ic_baseline_turned_in_24);
                }else {
                    favourite_btn.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}
