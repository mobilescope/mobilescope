package com.mobilescope;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import com.beatme.restclient.http.HttpCreater;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.SSMobileSQLite;
import com.mobilescope.preference.UserSettings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class MainPageActivity extends Activity {
	
	 private Button _leaderboard;
	    private Button _toursearch;
//	    private Button _forum;
	    private Button _playersearch;
	    private Button _touridsearch;
	    ViewFlipper _flippy;
	    String signID;
	    String pid;
	    DBUserInfo db;
	    private SSMobileSQLite _SSMobileSQLite;
	    Context mContext=this;
	   
	    final static int RESULT_SETTINGS = UserSettings.getResultSettings();
	    private final static int RESULT_SETTINGS1 = 1;
	    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		 db= new DBUserInfo(this);
	        db.openDB();
	       
	        db.assetManager=getAssets();
	        _SSMobileSQLite = new SSMobileSQLite(this);
	        _SSMobileSQLite.openDB();

	        Intent i = getIntent();
	        Bundle b = i.getExtras();
//			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences sharedPrefs = this.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
			signID = sharedPrefs.getString("sign_id", "NULL");
//	        signID = b.getString("SIGNID");
			if(signID == null){
				Log.i("DEBUG", "Preference value set to null");
			}
	        pid=db.getValue("encrytPassword", "user_name", signID);;
	     _leaderboard = (Button)findViewById(R.id.leaderboards);
	     _toursearch = (Button)findViewById(R.id.findtournament);
//	     _forum = (Button)findViewById(R.id.forum);
	     _playersearch = (Button)findViewById(R.id.playerstatics);
	     _touridsearch = (Button)findViewById(R.id.tournament);
//	     _flippy = (ViewFlipper) findViewById(R.id.viewFlipper1);
//	     signID = b.getString("SIGNID");
	     pid=db.getValue("encrytPassword", "user_name", signID);
	     
	        _leaderboard.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent();
					i.putExtra("SIGNID", signID);
			    	i.setClassName("com.mobilescope", "com.mobilescope.leader.LeaderboardTabActivity");
			    	startActivity(i);	
				}
			}) ;
	   
//	        _forum.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent i = new Intent();
//					i.putExtra("SIGNID", signID);
//					i.setClassName("com.mobilescope","com.mobilescope.ForumActivity");
//					startActivity(i);
//				}
//			});
	        
	        _toursearch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					    
				  Intent i = new Intent();
				  i.putExtra("SIGNID", signID);
				  i.putExtra("PID", pid);
					i.setClassName("com.mobilescope","com.mobilescope.TournamentSearchActivity");
					startActivity(i);
				}
			});
	        
	        _playersearch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					    
					Intent i = new Intent();
			    	i.putExtra("SIGNID", signID);
			    	i.setClassName("com.mobilescope", "com.mobilescope.PlayerSearchActivity");
			    	startActivity(i);
				}
			});
	        
	        _touridsearch.setOnClickListener(
	        		new View.OnClickListener() {
	    				
	    				@Override
	    				public void onClick(View v) {
//	    					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
//	    					alertBuilder.setMessage("Underconstruction ....")
//	    						.setCancelable(false)
//	    						.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
//	    						       public void onClick(DialogInterface dialog, int id) {
//	    						    		    						       }
//	    						   });
//	    					 AlertDialog displayDialog = alertBuilder.create();
//	    					  displayDialog.show();
	    					Intent i = new Intent();
	    					i.putExtra("SIGNID", signID);
	    					  i.putExtra("PID", pid);
	    					i.setClassName("com.mobilescope", "com.mobilescope.tournament.TourStaticSearchActivity");
	    					startActivity(i);
	    				}
	        		});
//	        _flippy.setFlipInterval(500);
	        //_flippy.startFlipping();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.mainmenu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case R.id.menu_home:
			Intent i = new Intent();
			i.putExtra("SIGNID", signID);
			i.setClassName("com.mobilescope",
					"com.mobilescope.MainPageActivity");
			startActivity(i);
			break;
		case R.id.menu_settings1:
			Intent i1 = new Intent();
			i1.setClassName("com.mobilescope",
					"com.mobilescope.preference.UserPreferenceActivity");
			startActivityForResult(i1, RESULT_SETTINGS);
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		UserSettings _userSettings1 = new UserSettings(this);
		switch (requestCode) {
		case RESULT_SETTINGS1:
//			_userSettings1.settingChanges();
			break;
		}
		;
	}


}
