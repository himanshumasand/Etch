package com.hmasand.etch.activities;

import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hmasand.etch.R;
import com.hmasand.etch.adapters.HomeListAdapter;
import com.hmasand.etch.dialogs.CreateEtchDialog;
import com.hmasand.etch.models.RichLinkPreviewData;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements CreateEtchDialog.CreateEtchDialogListener{

    private RecyclerView mRvHomeList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton mFabAdd;

    private String mUrlString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupViewObjects();

        handleIntentReceived();
    }

    private void handleIntentReceived() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null && "text/plain".equals(type)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                mUrlString = sharedText;
                openCreateEtchDialog();
            }
        }
    }

    private void setupViewObjects() {
        mRvHomeList = (RecyclerView) findViewById(R.id.rvHomeList);

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

        mFabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateEtchDialog();
            }
        });
    }

    private String getUrlString() {
        String url = mUrlString;

        if(url.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                url = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
            }
        }

        return url;
    }

    public void openCreateEtchDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CreateEtchDialog createEtchDialog = CreateEtchDialog.newInstance(getUrlString());
        createEtchDialog.show(fm, "dialog_create_etch");
    }
    @Override
    public void onCreateEtchSuccess() {
        Toast.makeText(this, "Link etched!", Toast.LENGTH_LONG).show();
    }
}
