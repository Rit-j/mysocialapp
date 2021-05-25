package com.example.mysocialapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myviewholder extends RecyclerView.ViewHolder {

    TextView imgtitleview;
    ImageView like_btn;
    TextView like_text;
    ImageView comment_btn;
    DatabaseReference likereference;
    ImageView setImage;
    TextView date;

    public myviewholder(@NonNull View itemView) {
        super(itemView);
        imgtitleview = itemView.findViewById(R.id.imagetitle);

        setImage = itemView.findViewById(R.id.setImage);

        like_btn = (ImageView) itemView.findViewById(R.id.like_btn);
        like_text = (TextView) itemView.findViewById(R.id.like_text);
        comment_btn = (ImageView) itemView.findViewById(R.id.comment_btn);
        date = itemView.findViewById(R.id.date);
    }

    public void getlikebuttonstatus(final String postkey, final String userid) {
        likereference = FirebaseDatabase.getInstance().getReference("likes");
        likereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postkey).hasChild(userid)) {
                    int likecount = (int) snapshot.child(postkey).getChildrenCount();
                    like_text.setText(likecount + " likes");
                    like_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
                } else {
                    int likecount = (int) snapshot.child(postkey).getChildrenCount();
                    like_text.setText(likecount + " likes");
                    like_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
