package com.example.testproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class ConfirmCoordinates extends DialogFragment {
    
	static final int NONE = 0;
    static final int SEND = 1;
    static final int CANCEL = 2;
	private boolean isSend = true;
	private int MODE = NONE;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.share_message).setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                	   setIsSend(true);
                	   setMode(SEND);
                   }
               }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   setIsSend(false);
                	   setMode(CANCEL);
                   }
               });
        // Create the AlertDialog object and return it
        //Log.d("MapLog", "returnas då");
        return builder.create();
    }

	public boolean getIsSend() {
		return isSend;
	}

	public void setIsSend(boolean isSend) {
		this.isSend = isSend;
	}
	
	public void setMode(int MODE){
		this.MODE = MODE;
	}
	
	public int getMode(){
		return MODE;
	}

}