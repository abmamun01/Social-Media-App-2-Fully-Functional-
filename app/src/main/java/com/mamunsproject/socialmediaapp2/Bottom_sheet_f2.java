package com.mamunsproject.socialmediaapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mamunsproject.socialmediaapp2.Activity.Related_Activity;
import com.mamunsproject.socialmediaapp2.Activity.Your_QuestionActivity;

import org.jetbrains.annotations.NotNull;

public class Bottom_sheet_f2 extends BottomSheetDialogFragment {

    CardView cardView, cardView2;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_f2, null);

        cardView = view.findViewById(R.id.related_f2);
        cardView2 = view.findViewById(R.id.your_question_f2);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Related_Activity.class));
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Your_QuestionActivity.class));
            }
        });

        return view;
    }
}
