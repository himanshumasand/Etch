package com.hmasand.etch.dialogs;

import android.app.Dialog;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hmasand.etch.R;
import com.hmasand.etch.models.RichLinkPreviewData;
import com.squareup.picasso.Picasso;

/**
 * Created by hmasand on 8/29/16.
 */
public class CreateEtchDialog extends DialogFragment implements View.OnClickListener, RichLinkPreviewData.CreateRichLinkPreviewListener{

    private EditText mEtCreateEtch;
    private Button mBtEtch;
    private Button mBtCancel;
    private Button mBtConfirm;
    private LinearLayout mEtchItem;

    private RichLinkPreviewData rlpData;

    public CreateEtchDialog() {}

    public interface CreateEtchDialogListener {
        void onCreateEtchSuccess();
    }

    public static CreateEtchDialog newInstance() {
        CreateEtchDialog dialog = new CreateEtchDialog();
//        Bundle args = new Bundle();
//        args.putString("url", url);
//        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_etch, container, false);

        mEtCreateEtch = (EditText) view.findViewById(R.id.etCreateEtch);
        mBtEtch = (Button) view.findViewById(R.id.btEtch);
        mBtCancel = (Button) view.findViewById(R.id.btCancel);
        mBtConfirm = (Button) view.findViewById(R.id.btConfirm);
        mEtchItem = (LinearLayout) view.findViewById(R.id.llItem);

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if(clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            mEtCreateEtch.setText(clipboard.getPrimaryClip().getItemAt(0).getText());
            new RichLinkPreviewData(this, mEtCreateEtch.getText().toString());
        }
        mEtCreateEtch.requestFocus();

        mBtEtch.setOnClickListener(this);
        mBtCancel.setOnClickListener(this);
        mBtConfirm.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btEtch:
                new RichLinkPreviewData(this, mEtCreateEtch.getText().toString());
                break;

            case R.id.btConfirm:

                DatabaseReference entriesRef = FirebaseDatabase.getInstance().getReference().child("entries");
                entriesRef.push().setValue(rlpData);

                final CreateEtchDialogListener listener = (CreateEtchDialogListener) getActivity();
                listener.onCreateEtchSuccess();
                dismiss();

            case R.id.btCancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onCreateRichLinkPreviewSuccess(RichLinkPreviewData data) {
        TextView tvRichLinkTitle = (TextView) mEtchItem.findViewById(R.id.tvRichLinkTitle);
        TextView tvRichLinkDesc = (TextView) mEtchItem.findViewById(R.id.tvRichLinkDescription);
        ImageView ivRichLinkThumb = (ImageView) mEtchItem.findViewById(R.id.ivRichLinkThumbnail);

        rlpData = data;
        tvRichLinkTitle.setText(data.title);
        tvRichLinkDesc.setText(data.description);
        Picasso.with(getContext()).load(data.imageUrl).resize(100, 100).centerCrop().into(ivRichLinkThumb);

    }
}
