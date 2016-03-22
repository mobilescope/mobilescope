package com.mobilescope;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.res.AssetManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;

import com.beatme.process.data.ProcessData;
import com.beatme.restclient.http.HttpCreater;
import com.beatme.restclient.http.RequestMethod;
import com.beatme.restclient.http.RestClient;

import com.mobilescope.util.UIObjects;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.SSMobileSQLite;
import com.mobilescope.history.HistoryDAO;
import com.mobilescope.history.PlayerSearchHistory;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.widget.*;
import android.view.ContextMenu.ContextMenuInfo;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import org.apache.http.protocol.HTTP;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@SuppressLint("NewApi")
public class SSMobileQuickSearchActivity extends Activity implements
		OnItemClickListener, OnItemSelectedListener, Runnable {
	class SSMobileQuickSearchHandler extends Handler {
		private SSMobileQuickSearchActivity parent;

		private SSMobileQuickSearchHandler(SSMobileQuickSearchActivity parent) {
			this.parent = parent;
		}

		@Override
		public void handleMessage(Message msg) {
			parent.handleMessage(msg);
		}
	}

	private SSMobileQuickSearchHandler handler;

	Button back, exit;
	TextView result;
	AutoCompleteTextView quickSearchView;
	String quickTextPrefix = "al";
	String signID;
	String pid;
	PlayerSearchHistory playerSearchHistory;
	TableLayout tableLayout;
	HistoryDAO tableHistory;
	DBUserInfo db;
	DisplayMetrics displayMetrics;

	ArrayList<HistoryDAO> historyDetails;
	private String _playerName;
	private String _playerNetwork;
	private SSMobileSQLite _SSMobileSQLite;
	private Button mainButton;
	// private Button _leaderboard;
	// private Button _toursearch;
	// private Button _forum;
	private int _limit = 10;
	ProgressDialog progressDialog;
	Context mContext = this;
	Thread thread;
	String[] playersList;
	boolean isAdapterCreated=false;
	
	
	HttpCreater hc;
	ViewFlipper flippy;
	// WebView mWebView;
	int _playerListCount = 0;
	boolean _showNetworkDialog=false;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.searchpanel);
		db = new DBUserInfo(this);
		db.openDB();
		
		hc = new HttpCreater();
		db.assetManager = getAssets();
		_SSMobileSQLite = new SSMobileSQLite(this);
		_SSMobileSQLite.openDB();

		Intent i = getIntent();
		Bundle b = i.getExtras();

		signID = b.getString("SIGNID");
		pid = db.getValue("encrytPassword", "user_name", signID);
		;
		playerSearchHistory = new PlayerSearchHistory();
		mainButton = (Button) findViewById(R.id.searchgo);
		mainButton.setEnabled(false);
		// _leaderboard = (Button)findViewById(R.id.leaderboards);
		// _toursearch = (Button)findViewById(R.id.findtournament);
		// _forum = (Button)findViewById(R.id.forum);
		final String[] playersList = new String[_limit];
		// String[] playersList =
		// initializeAutoComplete(10,signID,pid,db.getAssetManager());
		// mWebView = (WebView) findViewById(R.id.webview);
		// mWebView.getSettings().setJavaScriptEnabled(true);
		// mWebView.loadUrl("http://beta.sharkscope.com/#Leaderboards//2012/Any-Game/Any%20Stakes");
		// mWebView.bringToFront();
		//

		float sizeInDip = 3f;
		displayMetrics = getResources().getDisplayMetrics();

		quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, playersList);
		quickSearchView.setAdapter(adapter);
		quickSearchView.setHint("Search Player name");
		quickSearchView.setThreshold(4);
		registerForContextMenu(quickSearchView);
		mainButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				EditText quickTextObj = (EditText) findViewById(R.id.quickSearch);
				_playerName = quickTextObj.getText().toString();
				AlertCreateDialog();
			}
		});

//		db = new DBUserInfo(this);
//		db.openDB();

		historyDetails = playerSearchHistory.playersearchHistory(db);
		if (!historyDetails.isEmpty()) {
			db.closeDB();
			tableLayout = (TableLayout) findViewById(R.id.playersearchhistory);
			UIObjects uiObjects = new UIObjects();
			int pixel = uiObjects.convertToPixel(sizeInDip, displayMetrics);
			for (int i1 = 0; i1 < historyDetails.size(); i1++) {
				tableHistory = historyDetails.get(i1);
				TableRow tr = addTableRow(tableHistory, uiObjects, i1, pixel);
				tr.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						v.setBackgroundColor(Color.GRAY);
						System.out.println("Row clicked: " + v.getId());
						Intent i = new Intent();
						i.putExtra("USERNAME", historyDetails.get(v.getId())
								.get_name());
						i.putExtra("NETWORK", historyDetails.get(v.getId())
								.get_network());
						i.putExtra("SIGNID", signID);
						i.putExtra("PID", pid);
						i.setClassName("com.mobilescope",
								"com.mobilescope.SSMobilePlayerStatus");

						startActivity(i);

					}
				});
				tableLayout.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
		db.closeDB();

		// attach listeners
		quickSearchView.addTextChangedListener(new TextWatcher() {

			boolean backbutton;
			int charcount;

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int before, int after) {
				charcount = s.length();
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// updateAutoComplete();
				
				
				final AutoCompleteTextView quickTextObj = (AutoCompleteTextView) findViewById(R.id.quickSearch);
				final String quickText = quickTextObj.getText().toString();

				if (quickText != null && quickText.trim().length() >= 3
						&& charcount < quickText.trim().length()) {
					SSMobilePD ssMobilePD = new SSMobilePD();
					try{
						
						ssMobilePD.execute(quickText);
					}catch(Exception e){
						
					}
					if(ssMobilePD.isCancelled()){
						Log.i("DEBUG","[AsycProcess running]"+quickText.length());
					}
//					createAdapter();

				}
				
			}

		});

		quickSearchView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub

				return false;
			}

		});

		 quickSearchView.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				 _playerName=(String)arg0.getItemAtPosition(arg2);
				 quickSearchView.dismissDropDown();
				 quickSearchView.setText(_playerName);
				 Log.i("DEBUG", "[onItemSelected]quick Search item select value is " + _playerName);
//				 AlertCreateDialog();
				 _showNetworkDialog=true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			 
		 });
		 quickSearchView.setOnItemClickListener(new OnItemClickListener(){
		
		 @Override
		 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		 long arg3) {
		 // TODO Auto-generated method stub
			 
			 _playerName=(String)arg0.getItemAtPosition(arg2);
			 quickSearchView.dismissDropDown();
			 quickSearchView.setText(_playerName);
			 try{
				 SSMobilePD1 ssMobilePD1 = new SSMobilePD1();
				 ssMobilePD1.execute(_playerName);
			 }catch(Exception e){
				 Log.i("DEBUG","[onItemClick]exception");
			 }
			 Log.i("DEBUG", "[onItemClick]quick Search item click value is " + _playerName);
//			 AlertCreateDialog();
			 _showNetworkDialog=true;
		 }
		
		 });

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
			i.setClassName("com.mobilescope", "com.mobilescope.MobileScopeMain");
			startActivity(i);
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	public void createProgressDialogThread() {
		EditText quickTextObj = (EditText) findViewById(R.id.quickSearch);
		final String quickText = quickTextObj.getText().toString();

		if (quickText != null && quickText.trim().length() >= 3) {
			progressDialog = ProgressDialog.show(mContext, "",
					"Getting list of users", true);
			thread = new Thread(this);
			thread.start();

		}

	}

	public void AlertCreateDialog() {
		final String[] networks = getNetworkNames();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 
		builder.setTitle("Select Network");
		System.out.println("Length is:"+networks.length);
		if(networks.length == 0){
			builder.setMessage("No network data available");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		  		
		  		public void onClick(DialogInterface dialog, int which) {
		  			// TODO Auto-generated method stub
		  			
		  		}
		  	});
		}else{
		builder.setSingleChoiceItems(networks, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						Toast.makeText(getApplicationContext(), networks[item],
								Toast.LENGTH_SHORT).show();
						processPlayerNetworkInfo(networks[item]);

					}
				});
		}
		AlertDialog alert = builder.create();
	
		alert.show();
	}

	public void getPlayerList(String quickText) {
		playersList = null;
		try {
			if (!_SSMobileSQLite.checkPlayerPrefix(quickText)) {
				quickTextPrefix = quickText;
				_SSMobileSQLite.removeAllPlayers();
				playersList = initializeAutoComplete(25, signID, pid,
						db.getAssetManager());
			} else {
				playersList = _SSMobileSQLite.getUniquePlayers();
			}
		} catch (Exception e) {
			playersList = _SSMobileSQLite.getUniquePlayers();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Select Network ..");
		String[] networks = getNetworkNames();
		for (int i = 0; i < networks.length; i++) {
			menu.add(Menu.NONE, v.getId(), i, networks[i]).setCheckable(true);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		function1(item.getTitle().toString());
		processPlayerNetworkInfo(item.getTitle().toString());

		return true;
	}

	public void processPlayerNetworkInfo(String network) {
		_playerNetwork = network;

		// progressDialog = ProgressDialog.show(this, "",
		// "Getting user information wait for few seconds...", true);
		// ProcessPlayerStatus processPlayerStatus
		// =((ProcessPlayerStatus)getApplicationContext());
		// processPlayerStatus.set_assetManager(db.getAssetManager());
		// processPlayerStatus.set_networkName(_playerNetwork);
		// processPlayerStatus.set_userName(_playerName);
		// processPlayerStatus.set_userid(signID);
		// processPlayerStatus.set_passid(pid);
		// processPlayerStatus.processPlayer();
		Intent i = new Intent();

		i.putExtra("USERNAME", _playerName);
		i.putExtra("NETWORK", _playerNetwork);
		i.putExtra("SIGNID", signID);
		i.putExtra("PID", pid);
		i.setClassName("com.mobilescope",
				"com.mobilescope.SSMobilePlayerStatus");
		// i.setClassName("com.mobilescope",
		// "com.mobilescope.search.RecentTournamentHistory");
		// i.setClassName("com.mobilescope",
		// "com.mobilescope.search.PlayerHistory");
		// i.setClassName("com.mobilescope",
		// "com.mobilescope.search.PlayerGraph");
		// progressDialog.dismiss();
		startActivity(i);

	}

	public void createAdapter() {
		quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
		// ArrayAdapter<String> adapter = (ArrayAdapter<String>)
		// quickSearchView.getAdapter();
		// adapter.clear();
		
		if (playersList != null && playersList.length > 0) {
			ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(this,
					R.layout.list_item, playersList);
			if (isAdapterCreated){
				newAdapter.notifyDataSetChanged();
			}else{
			quickSearchView.setAdapter(newAdapter);
			newAdapter.setNotifyOnChange(true);
			isAdapterCreated=true;
			}
			quickSearchView.showDropDown();
			mainButton.setEnabled(true);
		}
	}

	public void function1(String network) {
		Toast.makeText(this, network, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Implements OnItemClickListener
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// perform the quick search ..
		EditText quickTextObj = (EditText) findViewById(R.id.quickSearch);
		String quickText = quickTextObj.getText().toString();
		Log.i("DEBUG", "[onItemClick1]quick Search value is " + quickText);
		_playerName = quickText;
		// should get the network from the SQLLite and perform the search ..
	}

	/**
	 * Implements OnItemSelectedListener
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// perform the quick search ..
		EditText quickTextObj = (EditText) findViewById(R.id.quickSearch);
		String quickText = quickTextObj.getText().toString();
		// registerForContextMenu(quickTextObj);

		Log.i("DEBUG", "[onItemSelected]quick Search value is " + quickText);
	}

	/**
	 * Implements OnItemSelectedListener
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	/**
	 * Callback to watch the textedit field for empty/non-empty
	 */
//	private TextWatcher quickSearchTextWatcher = new TextWatcher() {
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int before,
//				int after) {
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before,
//				int after) {
//
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			// updateAutoComplete();
//			createProgressDialogThread();
//		}
//	};

	private String[] initializeAutoComplete(int limit, String signid,
			String pid, AssetManager assetManager) {

		ProcessData pd;
		JSONArray AutoComplete = null;
		String formUrl = null;
		try {
			formUrl = hc.formUrl("networks/*/players/"
					+ URLEncoder.encode(quickTextPrefix, HTTP.UTF_8)
					+ "/suggestions?limit=" + limit);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] playersList = null;

		String authResponse = hc.HttpProcessUserAccess(formUrl, signid, pid,
				"qsrusername.txt", assetManager);
		// String authResponse = HttpProcess(formUrl,signid,pid);

		System.out.println("AuthResponse:" + authResponse);
		pd = new ProcessData(authResponse);
		try {
			
			AutoComplete = pd.processAutoComplete(authResponse, "Response");
			System.out.println("AuthReponse array size"+AutoComplete.length());
			for (int i = 0; i < AutoComplete.length(); i++) {
				JSONObject autoComplete = AutoComplete.getJSONObject(i);
				String playerName = autoComplete.getString("@name");
				String playerNetwork = autoComplete.getString("@network");
				if (playerName != null) {
					_SSMobileSQLite.insertPlayer(playerName, playerNetwork,
							quickTextPrefix);
				}
			}

		}
		catch (Exception e) {
			System.out.println("Error executing the Request");
			Toast.makeText(
					this,
					"Unable to find player details in any network. Check player name",
					Toast.LENGTH_LONG);
			mainButton.setEnabled(false);
		} finally {
			playersList = _SSMobileSQLite.getUniquePlayers();
		}
		return playersList;
	}

	private String HttpProcess(String formUrl, String signid, String pid) {
		String authResponse = null;
		String accept = "application/json";
		RestClient rc = new RestClient(formUrl);
		rc.AddHeader("Accept", accept);
		rc.AddHeader("Username", signid);
		rc.AddHeader("Password", pid);
		try {
			rc.Execute(RequestMethod.GET);
			authResponse = rc.getResponse();
		} catch (Exception e) {
			System.out.println("Error executing the Request");
		}
		return authResponse;

	}

	private String[] getNetworkNames() {
		String username = getQuickSearchString();
		return _SSMobileSQLite.getPlayerNetwork(username);
	}

	private String getQuickSearchString() {
		EditText quickTextObj = (EditText) findViewById(R.id.quickSearch);
		return quickTextObj.getText().toString();
	}

	private void quickSearchfill(String quickText) {
		if (!_SSMobileSQLite.checkPlayerPrefix(quickText)) {
			quickTextPrefix = quickText;
			_SSMobileSQLite.removeAllPlayers();
			String[] playersList = initializeAutoComplete(25, signID, pid,
					db.getAssetManager());
			if (playersList != null && playersList.length > 0) {
				quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
				// ArrayAdapter<String> adapter = (ArrayAdapter<String>)
				// quickSearchView.getAdapter();
				// adapter.clear();
				ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(
						this, R.layout.list_item, playersList);
				quickSearchView.setAdapter(newAdapter);
				quickSearchView.showDropDown();
				// mainButton.setEnabled(true);
			}
		} else {
			String[] playersList = _SSMobileSQLite.getUniquePlayers();
			if (playersList != null && playersList.length > 0) {
				quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
				// ArrayAdapter<String> adapter = (ArrayAdapter<String>)
				// quickSearchView.getAdapter();
				// adapter.clear();
				ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(
						this, R.layout.list_item, playersList);
				quickSearchView.setAdapter(newAdapter);
				quickSearchView.showDropDown();
				// mainButton.setEnabled(true);
			} else {
				// mainButton.setEnabled(false);
			}
		}
	}

	private void updateAutoComplete() {
		EditText quickTextObj = (EditText) findViewById(R.id.quickSearch);
		final String quickText = quickTextObj.getText().toString();

		if (quickText != null && quickText.trim().length() >= 3) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							progressDialog = ProgressDialog.show(mContext, "",
									"Getting list of users", true);
							quickSearchfill(quickText);
						}
					});

					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							progressDialog.dismiss();

						}
					});
				}
			});
			thread.start();
			while (thread.isAlive()) {
				Log.i("DEBUG", "In update Auto complete thread");
			}

		}
	}

	/**
	 * React to the user typing "enter" or other hardwired keys while typing in
	 * the search box. This handles these special keys while the edit box has
	 * focus.
	 */
	View.OnKeyListener quickSearchKeyListener = new View.OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// updateAutoComplete();
			return false;
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (_SSMobileSQLite != null) {
			_SSMobileSQLite.close();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(0);

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.i("DEBUG", "In update Auto complete thread");
			progressDialog.dismiss();
			registerForContextMenu(quickSearchView);
		}

	};

	public void handleMessage(Message msg) {
		progressDialog.dismiss();

		Log.i("Debug", msg.toString());
	}

	public TableRow addTableRow(HistoryDAO historyDao, UIObjects uiObjects,
			int i, int pixel) {
		TableRow tr = new TableRow(this);
		String Id = historyDao.get_name();
		String stake = historyDao.get_network();

		String curValue = "#";

		tr.setId(i);
		tr.setPadding(0, pixel, 0, 0);
		// tr.setId(Integer.valueOf(Id));
		tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		View layoutInflater = LayoutInflater.from(this).inflate(
				R.layout.textviewlayout, tr, false);
		View layoutInflater1 = LayoutInflater.from(this).inflate(
				R.layout.textviewlayout, tr, false);

		TextView tourId = addTextView(Id, "150", layoutInflater, false, false,
				uiObjects, .50);
		SpannableString content = new SpannableString(Id);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		tourId.setText(content);
		TextView tourStake = addTextView(stake, "150", layoutInflater1, false,
				false, uiObjects, .50);

		tr.addView(tourId);
		tr.addView(tourStake);
		return tr;

	}

	public TextView addTextView(String value, String width,
			View layoutInflater, boolean type, boolean rowvalue,
			UIObjects uiObjects, Double weight) {

		return uiObjects.createTextViewSize(value, width, layoutInflater, type,
				rowvalue, Integer.valueOf(10), weight);

	}

	private class SSMobilePD extends AsyncTask<String, String, String> {

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			 progressDialog.dismiss();

		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(String result) {
			 progressDialog.dismiss();
			createAdapter();

			super.onPostExecute(result);
			// TODO Auto-generated method stub

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			 progressDialog = ProgressDialog.show(mContext, "", "Getting list of users...", true, false);

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			getPlayerList(params[0]);
            Log.i("DEBUG",params[0]);
			return "Success";
		}

	}
	
	private class SSMobilePD1 extends AsyncTask<String, String, String> {

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			 progressDialog.dismiss();

		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			AlertCreateDialog();

			super.onPostExecute(result);
			// TODO Auto-generated method stub

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			 progressDialog = ProgressDialog.show(mContext, "","Getting users network...", true, false);

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			getPlayerList(params[0]);

			return "Success";
		}

	}
}
