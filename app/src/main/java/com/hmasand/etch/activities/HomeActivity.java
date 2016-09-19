package com.hmasand.etch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hmasand.etch.R;
import com.hmasand.etch.adapters.HomeListAdapter;
import com.hmasand.etch.models.RichLinkPreviewData;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRvHomeList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText mEtCreateEtch;
    private Button mBtEtch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupViewObjects();
    }

    private void setupViewObjects() {
        mRvHomeList = (RecyclerView) findViewById(R.id.rvHomeList);
        mRvHomeList.requestFocus();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRvHomeList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRvHomeList.setLayoutManager(mLayoutManager);

        //Get data from database
        final List<RichLinkPreviewData> entries = new ArrayList<>();
        DatabaseReference entriesRef = FirebaseDatabase.getInstance().getReference().child("entries");
        entriesRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                entries.clear();
                for(DataSnapshot entrySnapshot: snapshot.getChildren()) {
                    entries.add(entrySnapshot.getValue(RichLinkPreviewData.class));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        // specify an adapter
        mAdapter = new HomeListAdapter(this, entries);
        mRvHomeList.setAdapter(mAdapter);

        mEtCreateEtch = (EditText) findViewById(R.id.etCreateEtch);
        mBtEtch = (Button) findViewById(R.id.btEtch);
        mBtEtch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CreateEtchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Intent.EXTRA_TEXT, mEtCreateEtch.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
