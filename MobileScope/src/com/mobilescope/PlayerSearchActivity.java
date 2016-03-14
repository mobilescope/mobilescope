package com.mobilescope;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.protocol.HTTP;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.beatme.process.data.ProcessData;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.SSMobileSQLite;
import com.mobilescope.history.HistoryDAO;
import com.mobilescope.history.PlayerSearchHistory;
import com.mobilescope.menu.MobileScopeMenu;
import com.mobilescope.player.SearchPlayer;
import com.mobilescope.preference.UserSettings;
import com.mobilescope.util.TouchHighLight;
import com.mobilescope.util.UIObjects;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PlayerSearchActivity extends Activity implements TextWatcher {

	private DBUserInfo db;
	private PlayerSearchHistory playerSearchHistory;
	private Button mainButton;
	private DisplayMetrics displayMetrics;
	private AutoCompleteTextView quickSearchView;
	private int _limit = 10;
	final String[] playersList = new String[1];
	PlayerSearchAsyncTask _psearchTask;
	ArrayAdapter<String> adapter;
	ArrayList<HistoryDAO> historyDetails;
	private TableLayout tableLayout;
	private HistoryDAO tableHistory;
	float sizeInDip = 3f;
	String signID;
	String pid;
	private String _playerNetwork;
	Context mContext = this;

	private Map<String, ArrayList<String>> _playerSearchData = new HashMap<String, ArrayList<String>>();
	UserSettings _userSettings;
	final static int RESULT_SETTINGS = UserSettings.getResultSettings();
	private final static int RESULT_SETTINGS1 = 1;
    TouchHighLight tchHighLight = new TouchHighLight();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchpanel);
		db = new DBUserInfo(this);
		db.openDB();
		Intent i = getIntent();
		Bundle b = i.getExtras();
		SharedPreferences sharedPrefs = this.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
		signID = sharedPrefs.getString("sign_id", "NULL");
//		signID = sharedPrefs.getString("sign_id", "null");
//		signID = b.getString("SIGNID");
		pid = db.getValue("encrytPassword", "user_name", signID);
		db.closeDB();
		playerSearchHistory = new PlayerSearchHistory();
		mainButton = (Button) findViewById(R.id.searchgo);
		// mainButton.setEnabled(false);

		mainButton.setOnClickListener(new View.OnClickListener() {

			private String _playerName;

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						"Getting player network", Toast.LENGTH_SHORT).show();
				mainButton.setBackgroundResource(R.drawable.ic_example_select);
				EditText quickTextObj = (EditText) findViewById(R.id.quickSearch);
				_playerName = quickTextObj.getText().toString();
				if (_playerName.length() > 3) {
					_psearchTask.cancel(true);
					_psearchTask = new PlayerSearchAsyncTask();
					_psearchTask.execute("network", _playerName);
				}
				// AlertCreateDialog();
			}
		});
		displayMetrics = getResources().getDisplayMetrics();
		_psearchTask = new PlayerSearchAsyncTask();
		
		quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
		quickSearchView.addTextChangedListener(this);
		playersList[0] = "";

		adapter = new ArrayAdapter<String>(this, R.layout.list_item,
				playersList);

		adapter.setNotifyOnChange(true);
		quickSearchView.setAdapter(adapter);
		quickSearchView.setHint("Search Player name");
		quickSearchView.setThreshold(4);
		// registerForContextMenu(quickSearchView);
		quickSearchView.setOnItemClickListener(new OnItemClickListener() {
			private String _playerName;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						"Getting player network", Toast.LENGTH_SHORT).show();
				EditText quickTextObj = (EditText) findViewById(R.id.quickSearch);
				_playerName = quickTextObj.getText().toString();
				if (_playerName.length() > 3) {
					_psearchTask.cancel(true);
					_psearchTask = new PlayerSearchAsyncTask();
					_psearchTask.execute("network", _playerName);
				}
			}

		});
		_psearchTask.execute("History");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		_psearchTask.cancel(true);
		super.onDestroy();
	}

	@Override
	public void afterTextChanged(Editable s) {
		adapter.notifyDataSetChanged();
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (s.length() > 3) {
			_psearchTask.cancel(true);
			_psearchTask = new PlayerSearchAsyncTask();
			_psearchTask.execute("search", s.toString());
		}
	}

	private class PlayerSearchAsyncTask extends AsyncTask<String, Void, Void> {
		String _type = null;
		private String limit = "25";
		Set<String> _playerList;
		String username = null;

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			_type = params[0];
			if (_type.equals("History")) {
				populateHistory();
			}
			if (_type.equals("search")) {
				SearchPlayer searchPlayer = new SearchPlayer();
				username = params[1];
				_playerList = searchPlayer.getPlayerList(params[1], signID,
						pid, _type);
			}
			if (_type.equals("network")) {
				username = params[1];
				SearchPlayer searchPlayer = new SearchPlayer();
				_playerList = searchPlayer.getPlayerList(params[1], signID,
						pid, _type);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (_type.equals("History")) {
				populateHistoryUI();
			}
			if (_type.equals("search")) {
				for (String playerlist : _playerList) {
					adapter.add(playerlist);
				}
				// adapter.addAll(_playerList); API 11 supported
				adapter.notifyDataSetChanged();
			}
			if (_type.equals("network")) {
				AlertCreateDialog(_playerList, username);
			}

			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	public void populateHistory() {
		DBUserInfo db = new DBUserInfo(this);
		db.openDB();
		historyDetails = playerSearchHistory.playersearchHistory(db);
		db.closeDB();
	}

	public void populateHistoryUI() {
		if (!historyDetails.isEmpty()) {
			tableLayout = (TableLayout) findViewById(R.id.playersearchhistory);
			UIObjects uiObjects = new UIObjects();
			int pixel = uiObjects.convertToPixel(sizeInDip, displayMetrics);
			for (int i1 = 0; i1 < historyDetails.size(); i1++) {
				tableHistory = historyDetails.get(i1);
				TableRow tr = addTableRow(tableHistory, uiObjects, i1, pixel);
				tr.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					//	v.setBackgroundColor(getResources().getColor(R.color.ms_select));
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
				tr.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						tchHighLight.touchHighlight(v, event,getResources().getDrawable(R.drawable.textviewborder),getResources().getDrawable(R.drawable.textviewhghlig));
						return false; 
					}

				});
				tr.setFocusable(true);
				tr.setFocusableInTouchMode(true);
				tableLayout.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
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

	public void AlertCreateDialog(Set<String> _playerList,
			final String playername) {

		final String[] networks;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (_playerList == null) {
			builder.setMessage("No network data available");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
		} else {
			networks = _playerList.toArray(new String[_playerList.size()]);

			builder.setTitle("Select Network");
			System.out.println("Length is:" + networks.length);

			builder.setSingleChoiceItems(networks, -1,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							Toast.makeText(getApplicationContext(),
									networks[item], Toast.LENGTH_SHORT).show();
							processPlayerNetworkInfo(networks[item], playername);

						}
					});
		}
		AlertDialog alert = builder.create();

		alert.show();
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
//			i1.setClassName("com.mobilescope",
//					"com.mobilescope.preference.UserPreferenceActivity");
			i1.setClassName("com.mobilescope", "com.mobilescope.SettingsActivity");
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
			_userSettings1.settingChanges();
			break;
		}
		;
	}

	public void processPlayerNetworkInfo(String network, String _playerName) {
		_playerNetwork = network;

		_psearchTask.cancel(true);
		Intent i = new Intent();

		i.putExtra("USERNAME", _playerName);
		i.putExtra("NETWORK", _playerNetwork);
		i.putExtra("SIGNID", signID);
		i.putExtra("PID", pid);
		i.setClassName("com.mobilescope",
				"com.mobilescope.SSMobilePlayerStatus");

		startActivity(i);

	}

}
