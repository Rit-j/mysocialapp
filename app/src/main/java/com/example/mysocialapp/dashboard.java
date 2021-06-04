package com.example.mysocialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.mysocialapp.model.filemodel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashboard extends AppCompatActivity
{
    RecyclerView recview;
    DatabaseReference likereference;
    Boolean testclick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTitle("Social App");

        likereference=FirebaseDatabase.getInstance().getReference("likes");

        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        FirebaseRecyclerOptions<filemodel> options =
                new FirebaseRecyclerOptions.Builder<filemodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("myimages"), filemodel.class)
                        .build();

        FirebaseRecyclerAdapter<filemodel,myviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<filemodel, myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull filemodel model) {

                Glide.with(holder.setImage.getContext()).load(model.getUrl()).into(holder.setImage);

                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                final String userid=firebaseUser.getUid();
                final String postkey=getRef(position).getKey();

                holder.date.setText(model.getDate() + " " + model.getTime());

                holder.imgtitleview.setText(model.getTitle());

                holder.getlikebuttonstatus(postkey,userid);

                holder.like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        testclick=true;

                        likereference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(testclick==true)
                                {
                                    if(snapshot.child(postkey).hasChild(userid))
                                    {
                                        likereference.child(postkey).child(userid).removeValue();
                                        testclick=false;
                                    }
                                    else
                                    {
                                        likereference.child(postkey).child(userid).setValue(true);
                                        testclick=false;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

                holder.comment_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),commentpanel.class);
                        intent.putExtra("postkey",postkey);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
                return new myviewholder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recview.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.appmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout: FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(dashboard.this,MainActivity.class));
                finish();
                break;

            case R.id.manage_profile:startActivity(new Intent(dashboard.this,update_profile.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
