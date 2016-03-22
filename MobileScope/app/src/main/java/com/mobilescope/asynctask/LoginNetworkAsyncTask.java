package com.mobilescope.asynctask;

import com.beatme.TestMobileScope;
import com.mobilescope.database.auth.DBUserInfo;

import android.os.AsyncTask;

public class LoginNetworkAsyncTask extends AsyncTask<Object,Void,String>{
    String Error=null;
	@Override
	protected void onPostExecute(String Error) {
		// TODO Auto-generated method stub
//		super.onPostExecute(Error);
		getError();
	}

	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	@Override
	protected String doInBackground(Object... params) {
		// TODO Auto-generated method stub
		
	
		String signid = (String)params[0];
		String pwid = (String)params[1];
		DBUserInfo _DBUserInfo = (DBUserInfo)params[2];
		boolean DBEXIST = (Boolean)params[3];
		System.out.println("Signid:"+signid);
		com.beatme.TestMobileScope mobliesignup = new TestMobileScope(signid,pwid,_DBUserInfo);
        Error= mobliesignup.LoginStatus((boolean)DBEXIST);
       
		return Error;
	}

}
