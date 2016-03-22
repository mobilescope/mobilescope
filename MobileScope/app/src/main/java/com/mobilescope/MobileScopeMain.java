package com.mobilescope;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.beatme.*;

import com.mobilescope.leader.Leaderboard;
import com.mobilescope.preference.UserSettings;
import com.mobilescope.util.NetworkStatus;
import com.mobilescope.util.messageBox;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.asynctask.LeaderboardAsyncTask;
import com.mobilescope.asynctask.LoginNetworkAsyncTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA. User: Jude.Fernando Date: Nov 9, 2011 Time: 1:03:16
 * AM To change this template use File | Settings | File Templates.
 */
public class MobileScopeMain extends Activity {

	/**
	 * Called when the activity is first created.
	 */

	public class MobileScopeHandler extends Handler {
		private MobileScopeMain parent;

		private MobileScopeHandler(MobileScopeMain parent) {
			this.parent = parent;
		}

		public void handleMessage(Message msg) {
			parent.handleMessage(msg);
		}
	}

	Button mainButton;
	Button newSignButton;
	messageBox mb;
	TextView atx;
	TextView apx;
	String userid = null;
	String pwid = null;
	boolean DBEXIST = false;
	Leaderboard lb;
	ProgressDialog pd;
	ProgressDialog progressDialog;
	private DBUserInfo _DBUserInfo;
	ProgressBar pbar;
	String urlString;
	ArrayList<String> leaderdefault;
	Context mContext = this;
	MobileScopeMainPD mpd;
	String signid;
	String _errorMessage = null;

	private MobileScopeHandler handler;
	LeaderboardAsyncTask leaderboardAsyncTask ;
	LoginNetworkAsyncTask loginAsyncTaks = new LoginNetworkAsyncTask();
	NetworkStatus networkstatus = new NetworkStatus();
	UserSettings _userSettings;
	final static int RESULT_SETTINGS = UserSettings.getResultSettings();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scopemain);
		this.setTitle("MobileScope");
		mContext=this;
		leaderboardAsyncTask = new LeaderboardAsyncTask(mContext);
		atx = (TextView) findViewById(R.id.username);
		apx = (TextView) findViewById(R.id.password);
		newSignButton = (Button) findViewById(R.id.newusersign);

		setHandler(new MobileScopeHandler(this));
		pbar = (ProgressBar) findViewById(R.id.progressBar1);

		mpd = new MobileScopeMainPD();
		_DBUserInfo = new DBUserInfo(this);

		lb = new Leaderboard();
		_DBUserInfo.openDB();
		_DBUserInfo.assetManager = getAssets();
//		if (!networkstatus.mnetwork()){
//			SimpleBox("NetworkStatus", "Unable to connect to mobile network");
//		}

		if (_DBUserInfo.checkDB()) {
			leaderboardAsyncTask.execute(_DBUserInfo);
			// lb.processLeaderboard(_DBUserInfo,lb.urlString);
			// lb.processLeader(_DBUserInfo,lb.urlString);
			Log.i("DEBUG", "DB User Info check db false");
		} else {

			userid = _DBUserInfo.getColumnValue("user_name");
			pwid = _DBUserInfo.getColumnValue("encrytPassword");
			_DBUserInfo.removeLeader();

			// if (!userid.equals(null)){
			DBEXIST = true;
			// }
			leaderboardAsyncTask.execute(_DBUserInfo);
			// pd = ProgressDialog.show(this,
			// "Please wait getting leader data..", "", true,
			// false);

		}

		if (DBEXIST) {
			atx.setText(userid);
			apx.setText(pwid);
			newSignButton.setVisibility(View.GONE);
			storeUserid(userid);

		} else {
			atx.setHint("Enter sign id");
			atx.setHintTextColor(Color.GRAY);

			apx.setHint("Enter password");
			apx.setHintTextColor(Color.GRAY);

		}
		// Check database for User id already exits
		mainButton = (Button) findViewById(R.id.signIn);

		newSignButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClassName("com.mobilescope",
						"com.mobilescope.UserRegistration");
				startActivity(i);
			}

		});
		mainButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pbar = (ProgressBar) findViewById(R.id.progressBar1);
				pbar.setVisibility(View.VISIBLE);
				// pd.show();

				signid = atx.getText().toString().trim();

				pwid = apx.getText().toString().trim();
				Log.i("DEBUG", "On Click -- user id" + signid);
				Log.i("DEBUG", "On Click -- user id" + pwid);

				// mpd.execute("connection");
				AuthValidate(signid, pwid, _DBUserInfo, DBEXIST);

				if (_errorMessage.equals(null)
						|| _errorMessage.equals("No Error")) {
					// if(AuthValidate(signid,pwid,_DBUserInfo,DBEXIST)){
					storeUserid(signid);
					Intent i = new Intent();
					i.putExtra("SIGNID", signid);
					i.setClassName("com.mobilescope",
							"com.mobilescope.MainPageActivity");
					_DBUserInfo.close();
					startActivity(i);
				} else {
					SimpleBox("LoginStatus", _errorMessage);
					Log.i("DEBUG[Login Status", _errorMessage);
					atx.setText("");
					apx.setText("");
				}
			}

		});
	}

	private void storeUserid(String userid2) {
		String keyname="com.mobilescope.sign_id";
		SharedPreferences sharedPrefs = this.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
		  SharedPreferences.Editor editor = sharedPrefs.edit();
		  editor.putString("sign_id", userid2).commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.mainmenu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		switch (item.getItemId()) {
		case R.id.menu_home:
			
			i.setClassName("com.mobilescope",
					"com.mobilescope.MainPageActivity");
			startActivity(i);
			break;
		case R.id.menu_settings:
			i.setClassName("com.mobilescope",
					"com.mobilescope.preference.UserPreferenceActivity");
			startActivityForResult(i, RESULT_SETTINGS);
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		  _userSettings = new UserSettings(this);
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 1:
			_userSettings.settingChanges();
		}
		;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		loginAsyncTaks.cancel(true);
		super.onDestroy();
	}

	public boolean AuthValidate(String signid, String pwid,
			DBUserInfo _DBUserInfo, boolean DBEXIST) {

		if ((signid == null) || signid.length() < 1) {
			_errorMessage = "Enter a valid user id";
			// SimpleBox("Error In UserId", "Enter a valid user id");
			return false;
		}
		if ((pwid == null) || pwid.length() < 1) {
			_errorMessage = "Enter a valid password";
			// SimpleBox("Error In Password ", "Enter a valid password");
			return false;
		}

		String Error = null;
		// com.beatme.TestMobileScope mobliesignup = new
		// TestMobileScope(signid,pwid,_DBUserInfo);
		try {
			loginAsyncTaks.execute(signid, pwid, _DBUserInfo,
					Boolean.valueOf(DBEXIST));
			Error = loginAsyncTaks.get();
		
		if (Error.equals("No Error")) {
			_errorMessage = Error;
			return true;
		} else {
			// SimpleBox("LoginStatus", Error);
			_errorMessage = Error;
			return false;
		}
		} catch (Exception e) {
			Log.i("Login Activity", e.getMessage());
		}
         return true;
	}

	public void SimpleBox(String Title, String message) {
		Builder alertdialog;
		alertdialog = new AlertDialog.Builder(this);
		alertdialog.setTitle(Title);
		alertdialog.setMessage(message);
		alertdialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent i = new Intent();
						i.setClassName("com.mobilescope",
								"com.mobilescope.MobileScopeMain");

						startActivity(i);
					}
				});

		alertdialog.show();

	}

	public boolean isTableExists(String Tablename, DBUserInfo _DBUserInfo) {
		return true;
	}

	public void handleMessage(Message msg) {
		// pd.dismiss();
		Log.i("Debug", msg.toString());
	}

	public MobileScopeHandler getHandler() {
		return handler;
	}

	public void setHandler(MobileScopeHandler handler) {
		this.handler = handler;
	}

	@TargetApi(3)
	private class MobileScopeMainPD extends AsyncTask<String, String, String> {

		boolean authStatus = false;

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			// progressDialog.dismiss();
			pbar.setVisibility(View.INVISIBLE);

		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(String result) {
			// progressDialog.dismiss();
			pbar.setVisibility(View.INVISIBLE);

			super.onPostExecute(result);
			// TODO Auto-generated method stub

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// progressDialog = ProgressDialog.show(mContext, "",
			// "Connecting to server", true, false);

			pbar.setVisibility(View.VISIBLE);

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			authStatus = AuthValidate(signid, pwid, _DBUserInfo, DBEXIST);
			return "Success";

		}

	}
}
