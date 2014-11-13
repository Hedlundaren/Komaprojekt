package com.example.testproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class ConfirmCoordinates extends DialogFragment {
    
	Context mContext;

	public ConfirmCoordinates() {
	    mContext = getActivity();
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.share_message).setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                   }
               }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        //Log.d("MapLog", "returnas då");
        return builder.create();
    }
	/*
	public void showDialog(){
		ConfirmCoordinates dialog = new ConfirmCoordinates();
		dialog.show(getSupportFragmentManager(),  "NoticeDialogFragment");
		
	} */
}