package com.hmasand.etch.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hmasand.etch.R;
import com.hmasand.etch.models.RichLinkPreviewData;
import com.squareup.picasso.Picasso;

public class CreateEtchActivity extends AppCompatActivity implements RichLinkPreviewData.CreateRichLinkPreviewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        handleIntentReceived();
    }

    private void handleIntentReceived() {

        String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if (url != null) {
            new RichLinkPreviewData(this, url);
        }
    }

    @Override
    public void onCreateRichLinkPreviewSuccess(final RichLinkPreviewData data) {
        setContentView(R.layout.activity_create_etch);

        LinearLayout mEtchItem = (LinearLayout) findViewById(R.id.llItem);
        TextView tvRichLinkTitle = (TextView) mEtchItem.findViewById(R.id.tvRichLinkTitle);
        TextView tvRichLinkDesc = (TextView) mEtchItem.findViewById(R.id.tvRichLinkDescription);
        ImageView ivRichLinkThumb = (ImageView) mEtchItem.findViewById(R.id.ivRichLinkThumbnail);

        tvRichLinkTitle.setText(data.title);
        tvRichLinkDesc.setText(data.description);
        Picasso.with(this).load(data.imageUrl).resize(100, 100).centerCrop().into(ivRichLinkThumb);

        Button mBtEtch = (Button) findViewById(R.id.btEtch);
        mBtEtch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference entriesRef = FirebaseDatabase.getInstance().getReference().child("entries");
                entriesRef.push().setValue(data);
                Toast.makeText(getBaseContext(), "Link Etched!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
