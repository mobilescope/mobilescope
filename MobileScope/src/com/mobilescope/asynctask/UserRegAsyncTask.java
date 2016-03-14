package com.mobilescope.asynctask;



import org.codehaus.jettison.json.JSONObject;

import com.beatme.process.data.ProcessData;
import com.beatme.restclient.http.HttpCreater;
import com.beatme.restclient.http.RequestMethod;
import com.beatme.restclient.http.RestClient;
import com.mobilescope.SSMobilePlayerStatus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;


public class UserRegAsyncTask extends AsyncTask<Object,Void,String>{

	private ProgressDialog progressDialog1;
	ProcessData pd = new ProcessData();
	public UserRegAsyncTask(Context _context) {
		super();
		this.set_context(_context);
		
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		System.out.println(authResponse);
		progressDialog1.cancel();
		
		try{
			JSONObject rootNode = pd.processRootNode("Response", authResponse);
			JSONObject errorNode = pd.processErrorNode("Response", authResponse);
			if(errorNode != null){
				String errorMessage =errorNode.getString("$");
				simpleBox("Error",errorMessage);
			}else{
				storeUserid(signid);
				Intent i = new Intent();
				i.putExtra("SIGNID", signid);
				i.setClassName("com.mobilescope",
						"com.mobilescope.MainPageActivity");
				_context.startActivity(i);
				
//				if(DBEXIST){
//	        		   _DBUserInfo.updateDataInfo("metadatahash","user_name",ui.userid,ui.metadatahash);
//	        	   }else{
//	        		   _DBUserInfo.insertPlayer(ui.userid, encryPassword, ui.getmetadatahash());
//	        	   }
			}
			
		}catch(Exception e){
			System.out.println("Error in creating user[No Network]");
		}
	}
	
	 public void simpleBox(String Title, String message){
   	  Builder alertdialog;
   	  alertdialog =  new AlertDialog.Builder(_context);  
   	  alertdialog.setTitle(Title);
   	  alertdialog.setMessage(message);
   	  alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
   		
   		public void onClick(DialogInterface dialog, int which) {
   			// TODO Auto-generated method stub
   			
   		}
   	});
   	  
   	
   	  alertdialog.show();
   	  
   	  
     }	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog1 = new ProgressDialog(_context);
		progressDialog1
				.setMessage("Creating User id with Sharkscope");
		progressDialog1.setCancelable(false);
		progressDialog1.show();
	}
	private String signid;
	private String pid;
	private String country;
	private String subscribe;
	private Context _context;
	private String authResponse;
	HttpCreater httpcreator = new HttpCreater();
	@Override
	protected String doInBackground(Object... params) {
		signid= (String)params[0];
		pid = (String)params[1];
		country=(String)params[2];
		subscribe=(String)params[3];
		set_context((Context)params[4]);
//		 _displayMetrics=(DisplayMetrics)params[5];
		// TODO Auto-generated method stub
		String formUrl = httpcreator.formUrl("user/create");
		authResponse = httpcreator.httpProcessNewUser(formUrl, signid, pid, country,subscribe,"newuser.txt", null);
		return null;
	}
	public Context get_context() {
		return _context;
	}
	public void set_context(Context _context) {
		this._context = _context;
	}
	
	private void storeUserid(String userid2) {
		String keyname="com.mobilescope.sign_id";
		SharedPreferences sharedPrefs = _context.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
		  SharedPreferences.Editor editor = sharedPrefs.edit();
		  editor.putString("sign_id", userid2).commit();

	}


}
