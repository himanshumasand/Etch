package com.hmasand.etch.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hmasand.etch.R;
import com.hmasand.etch.adapters.HomeListAdapter;
import com.hmasand.etch.models.Entry;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvHomeList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Get data from database
        final List<Entry> entries = new ArrayList<>();
        DatabaseReference entriesRef = FirebaseDatabase.getInstance().getReference().child("entries");
        entriesRef.addValueEventListener(new ValueEventListener() {

            @Override
             public void onDataChange(DataSnapshot snapshot) {
                 for(DataSnapshot entrySnapshot: snapshot.getChildren()) {
                     Entry entry = new Entry(entrySnapshot.getValue().toString());
                     Log.d("DEBUG", entry.getUrl());
                     entries.add(entry);
                 }
                mAdapter.notifyDataSetChanged();
             }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        // specify an adapter
        mAdapter = new HomeListAdapter(entries);
        mRecyclerView.setAdapter(mAdapter);
    }
}
