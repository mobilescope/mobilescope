package com.mobilescope.asynctask;

import java.util.ArrayList;

import com.mobilescope.MobileScopeMain;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.leader.Leaderboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LeaderboardAsyncTask extends AsyncTask<Object,Void,String> {

	String status1="false";
	ProgressDialog progressDialog1;
	Context _context;
	
	public LeaderboardAsyncTask(Context _context) {
		super();
		this._context = _context;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		status1="true";
		getStatus();
		progressDialog1.cancel();
	}

	public String getStatus1() {
		return status1;
	}

	public void setStatus(String status1) {
		this.status1 = status1;
	}

	ArrayList<String> leaderdefault;
	Leaderboard lb = new Leaderboard();
	String urlString;
	
	@Override
	protected String doInBackground(final Object... params) {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {
	           @Override
	           public void run() {
	        	   		try{
            				 
	        	   			    leaderdefault = lb.dbleaderdefaultCategory((DBUserInfo)params[0]);
	        	   			 
            		            urlString = lb.processURLString(leaderdefault);
            		           lb.processLeaderboard((DBUserInfo)params[0],lb.urlString);
            		            lb.processLeader((DBUserInfo)params[0],urlString);
//            		            MobileScopeMain.this.getHandler().sendEmptyMessage(1000);
            		            status1="true";
            		            System.out.println("LoginAsycnTask: execuitng"+urlString);
            			} catch (Exception error) {
//                			MobileScopeMain.this.getHandler().sendEmptyMessage(1001);
            			} finally {
						}
	           }	
                });
	
     thread.start();	
    
		return status1;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog1 = new ProgressDialog(_context);
		progressDialog1
				.setMessage("Getting tournament details, please wait for mobilescope to get all the data");
		progressDialog1.setCancelable(false);
		progressDialog1.show();
	}

}
