package com.todo.todolist.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DeleteTaskDialogFragment extends DialogFragment {

    private MyDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (MyDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int position;
        position = getArguments().getInt("position");
        Activity activity = getActivity();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int finalPosition = position;
        builder.setMessage("Delete task?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    // FIRE ZE MISSILES!
                    //Toast.makeText(getContext(), String.valueOf(finalPosition), Toast.LENGTH_LONG).show();
                    //notifyDialog
                    //(333, finalPosition);

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
