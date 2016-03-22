package com.mobilescope;

import java.lang.annotation.Target;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilescope.menu.MobileScopeMenu;
import com.mobilescope.preference.UserSettings;
import com.mobilescope.processdata.ProcessPlayerStatus;
import com.mobilescope.search.graph.DetailGraph;
import com.mobilescope.search.PlayerGraph;
import com.mobilescope.search.PlayerHistory;
import com.mobilescope.search.RecentTournamentHistory;
import com.mobilescope.tour.detail.TourDetails;
import com.mobilescope.asynctask.PlayerStatusAsyncTask;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.PlayerDetail;
import com.mobilescope.database.auth.SSMobileSQLite;
import com.mobilescope.history.PlayerSearchHistory;
import com.mobilescope.history.TournamentSearchHistory;
import com.mobilescope.util.*;

/**
 * Created by IntelliJ IDEA. User: Jude.Fernando Date: 11/26/11 Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("deprecation")
public class SSMobilePlayerStatus extends TabActivity {
	private String _userName;
	private String _networkName;
	private String _signID;
	private String _pid;
	// private SSMobileSQLite _SSMobileSQLite;
	PlayerDetail playerDetail;
	ProgressDialog progressDialog;
	DBUserInfo db;
	UserUtil userUtil = new UserUtil();
	ProcessPlayerStatus processPlayerStatus;
	private Handler handler = new Handler();
	ProgressDialog progressDialog1;
	Context mContext = this;
	PlayerSearchHistory _playerSearchHistory;
	PlayerStatusAsyncTask playerAsyncTask = new PlayerStatusAsyncTask();
	Intent i;
	Intent graphIntent;
	UserSettings _userSettings ;
	final static int RESULT_SETTINGS = UserSettings.getResultSettings();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playerdetail);
		this.setTitle("MobileScope");
		i = getIntent();
		Bundle b = i.getExtras();

		_userName = b.getString("USERNAME");
		_networkName = b.getString("NETWORK");

		mContext = this;

		playerAsyncTask.execute(mContext, processPlayerStatus, _signID);
		// try {
		// processPlayerStatus = (ProcessPlayerStatus) playerAsyncTask.get();
		//
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

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

	public void playerOpt() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
		alertBuilder.setMessage("Player opted in").setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = new Intent();
						i.putExtra("SIGNID", _signID);
						i.setClassName("com.mobilescope",
								"com.mobilescope.PlayerSearchActivity");
						startActivity(i);
					}
				});
		AlertDialog displayDialog = alertBuilder.create();
		displayDialog.show();
	}

	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	public void addTabHost(Intent i) {
		// progressDialog.dismiss();
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		UIObjects uiObject = new UIObjects();
		PlayerSearchHistory playerHistory = new PlayerSearchHistory(_userName,
				_networkName);
		playerHistory.insertPlayerHistory(db);

		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, PlayerHistory.class);
		i.putExtra("USERNAME", _userName);
		i.putExtra("NETWORK", _networkName);

		setTableLayoutHeight();
		// Initialize a TabSpec for each tab and add it to the TabHost
		// spec =
		// tabHost.newTabSpec("playerhistory").setIndicator("History",res.getDrawable(R.drawable.icon)).setContent(intent);
		View tabview = uiObject.createTabView(tabHost.getContext(), "History");
		spec = tabHost.newTabSpec("playerhistory").setIndicator(tabview)
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		// intent = new Intent().setClass(this, PlayerGraph.class);
		// intent = getGraphIntent();
		tabview = uiObject.createTabView(tabHost.getContext(), "Graph");
		spec = tabHost.newTabSpec("playergraph").setIndicator(tabview)
				.setContent(graphIntent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, RecentTournamentHistory.class);
		tabview = uiObject.createTabView(tabHost.getContext(), "Recent");
		spec = tabHost.newTabSpec("recenttournament").setIndicator(tabview)
				.setContent(intent);
		tabHost.addTab(spec);
		tabHost.setCurrentTab(0);

	}

	// commented codes
	// TextView UserName = (TextView) findViewById(R.id.playerstat_userName);
	// UserName.setText(_userName);
	// Spinner NetworkName = (Spinner)
	// findViewById(R.id.playerstat_networkName);
	// ArrayAdapter networkAdapter = new
	// ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,getNetworkNames(_userName));
	// NetworkName.setAdapter(networkAdapter);

	private void setValue() {
		TextView UserName = (TextView) findViewById(R.id.playerUSERNAME);
		UserName.setText(_userName);
		TextView Network = (TextView) findViewById(R.id.playerNETWORK);
		Network.setText(_networkName);

	}

	// private String[] getNetworkNames(String username){
	// return _SSMobileSQLite.getPlayerNetwork(username);
	// }
	//
	private Intent getGraphIntent() {
		Intent intent;
		ProcessPlayerStatus processPlayerStatus = ((ProcessPlayerStatus) getApplicationContext());
		playerDetail = processPlayerStatus.getPlayerd();
		// intent = new
		// DetailGraph().execute(this,playerDetail.get_lCumulativeProfitGrossSeries());
		intent = new DetailGraph().execute(this,
				playerDetail.get_lCumulativeProfitSeries());
		return intent;
	}

	private boolean checkOptValue() {
		ProcessPlayerStatus processPlayerStatus = ((ProcessPlayerStatus) getApplicationContext());
		if (processPlayerStatus.get_networkName().contains("Poker")) {
			return processPlayerStatus.getPlayerd().get_isOptValue();
		} else {
			return false;
		}
	}

	private void setTableLayoutHeight() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int screen_width = display.getWidth();
		int screen_height = display.getHeight();
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearmiddle);
		// Gets the layout params that will allow you to resize the layout
		ViewGroup.LayoutParams params = layout.getLayoutParams();
		// Changes the height and width to the specified *pixels*
		int height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 110, getResources()
						.getDisplayMetrics());

		params.height = screen_height - height;
		// params.width = 100;
	}

	private class PlayerStatusAsyncTask extends AsyncTask<Object, Void, Object> {

		ProgressDialog progressDialog1;
		String authResponse;

		protected void onPostExecute(Object result) {
			processPlayerStatus.processPlayerData(authResponse);
			if (!checkOptValue()) {
				graphIntent = getGraphIntent();
			}
			setValue();
			TextView remaingCount = (TextView) findViewById(R.id.playerSEARCHCOUNT);
			remaingCount.setText(processPlayerStatus.get_count());
			if (!checkOptValue()) {
				addTabHost(i);
				progressDialog1.cancel();
			} else {
				playerOpt();
				progressDialog1
						.setMessage("This player opted out, please select different player");
				progressDialog1.setCancelable(true);
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog1 = new ProgressDialog(SSMobilePlayerStatus.this);
			progressDialog1
					.setMessage("Getting player information, please wait for mobilescope to get all the data");
			progressDialog1.setCancelable(false);
			progressDialog1.show();
		}

		@Override
		protected Object doInBackground(Object... params) {

			// progressDialog = ProgressDialog.show(mContext, "",
			// "Getting user information wait for few seconds...", true);
			db = new DBUserInfo(mContext);
			db.openDB();
			db.assetManager = getAssets();
			_signID = db.getColumnValue("user_name");
			_pid = db.getColumnValue("encrytPassword");
			processPlayerStatus = ((ProcessPlayerStatus) getApplicationContext());

			processPlayerStatus.set_assetManager(db.getAssetManager());
			processPlayerStatus.set_networkName(_networkName);
			processPlayerStatus.set_userName(_userName);
			processPlayerStatus.set_userid(_signID);
			processPlayerStatus.set_passid(_pid);
			authResponse = processPlayerStatus.processPlayer();

			return processPlayerStatus;
		}

	}
}
