package com.todo.todolist.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DeleteTaskDialogFragment extends DialogFragment {

    private MyDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (MyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int position = 0;
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
        Activity activity = getActivity();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int finalPosition = position;
        builder.setMessage("Delete task?")
                .setPositiveButton("Yes", (dialog, id) -> {

                    mListener.onDialogPositiveClick(DeleteTaskDialogFragment.this, finalPosition);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    // User cancelled the dialog
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface MyDialogListener {
        void onDialogPositiveClick(DialogFragment dialogFragment, int position);
    }
}
