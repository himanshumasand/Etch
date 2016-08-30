package com.hmasand.etch.dialogs;

import android.app.Dialog;
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

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.hmasand.etch.R;

/**
 * Created by hmasand on 8/29/16.
 */
public class CreateEtchDialog extends DialogFragment implements View.OnClickListener {

    private EditText mEtCreateEtch;
    private Button mBtEtch;
    private Button mBtCancel;

    public CreateEtchDialog() {}

    public interface CreateEtchDialogListener {
        void onCreateEtchSuccess();
    }

    public static CreateEtchDialog newInstance() {
        CreateEtchDialog dialog = new CreateEtchDialog();
        Bundle args = new Bundle();
//        args.putString("url", url);
        dialog.setArguments(args);
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

        mBtEtch.setOnClickListener(this);
        mBtCancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btEtch:
                final CreateEtchDialogListener listener = (CreateEtchDialogListener) getActivity();

                DatabaseReference entriesRef = FirebaseDatabase.getInstance().getReference().child("entries");
                entriesRef.push().setValue(mEtCreateEtch.getText().toString());

                listener.onCreateEtchSuccess();
                dismiss();
                break;

            case R.id.btCancel:
                dismiss();
                break;
        }
    }
}
