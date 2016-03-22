package com.mobilescope.asynctask;

import com.mobilescope.processdata.ProcessPlayerStatus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;

public class PlayerStatusAsyncTask extends AsyncTask<Object,Void,Object> {

	ProgressDialog progressDialog;
	Context mContext;
	ProcessPlayerStatus processPlayerStatus;
	String _signID;
	boolean noOpt=false;
	public boolean isNoOpt() {
		return noOpt;
	}

	public void setNoOpt(boolean noOpt) {
		this.noOpt = noOpt;
	}

	protected void onPostExecute(Object result) {
		 if(!processPlayerStatus.getPlayerd().get_isOptValue()){
//      	   progressDialog.dismiss();
   		   
  	   }else{
//  		   progressDialog.setMessage("This player opted out, please select different player");
  		  
//  		   progressDialog.dismiss();
  		   
  	   }
		// TODO Auto-generated method stub
		 getProcessPlayerStatus();
	
	}

	public ProcessPlayerStatus getProcessPlayerStatus() {
		return processPlayerStatus;
	}

	public void setProcessPlayerStatus(ProcessPlayerStatus processPlayerStatus) {
		this.processPlayerStatus = processPlayerStatus;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		 
	}

	@Override
	protected Object doInBackground(Object... params) {
		
		mContext = (Context)params[0];
		processPlayerStatus = (ProcessPlayerStatus)params[1];
		_signID = (String)params[2];

		  Thread thread = new Thread(new Runnable() {
	           @Override
	           public void run() {
//	        	   Looper.prepare();
//	                	   progressDialog = ProgressDialog.show(mContext, "", "Getting user information wait for few seconds...", true);
	                             
	               
	               processPlayerStatus.processPlayer();
	           }
		  });
		  thread.start();
		return processPlayerStatus;
	}

	
}
