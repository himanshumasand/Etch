package com.hmasand.etch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hmasand.etch.R;
import com.hmasand.etch.models.RichLinkPreviewData;
import com.squareup.picasso.Picasso;

public class CreateEtchActivity extends AppCompatActivity implements View.OnClickListener, RichLinkPreviewData.CreateRichLinkPreviewListener {

    private Button mBtEtch;
    private Button mBtCancel;
    private LinearLayout mEtchItem;

    private RichLinkPreviewData rlpData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntentReceived();
    }

    private void handleIntentReceived() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null && "text/plain".equals(type)) {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (url != null) {
                new RichLinkPreviewData(this, url);
            }
        }
    }

    @Override
    public void onCreateRichLinkPreviewSuccess(RichLinkPreviewData data) {
        setContentView(R.layout.activity_create_etch);

        mEtchItem = (LinearLayout) findViewById(R.id.llItem);
        TextView tvRichLinkTitle = (TextView) mEtchItem.findViewById(R.id.tvRichLinkTitle);
        TextView tvRichLinkDesc = (TextView) mEtchItem.findViewById(R.id.tvRichLinkDescription);
        ImageView ivRichLinkThumb = (ImageView) mEtchItem.findViewById(R.id.ivRichLinkThumbnail);

        rlpData = data;
        tvRichLinkTitle.setText(data.title);
        tvRichLinkDesc.setText(data.description);
        Picasso.with(this).load(data.imageUrl).resize(100, 100).centerCrop().into(ivRichLinkThumb);

        mBtEtch = (Button) findViewById(R.id.btEtch);
        mBtEtch.setOnClickListener(this);

        mBtCancel = (Button) findViewById(R.id.btCancel);
        mBtCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btEtch:
                DatabaseReference entriesRef = FirebaseDatabase.getInstance().getReference().child("entries");
                entriesRef.push().setValue(rlpData);
                finish();
                break;

            case R.id.btCancel:
                finish();
                break;
        }
    }
}
