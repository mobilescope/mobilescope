package com.mobilescope.tour.detail;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.beatme.process.data.ProcessData;
import com.beatme.restclient.http.HttpCreater;
import com.mobilescope.R;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.history.TournamentSearchHistory;
import com.mobilescope.leader.Leaderboard;
import com.mobilescope.leader.LeaderboardTabActivity;
import com.mobilescope.menu.MobileScopeMenu;
import com.mobilescope.preference.UserSettings;
import com.mobilescope.util.JsonUtils;
import com.mobilescope.util.TouchHighLight;
import com.mobilescope.util.UIObjects;
import com.mobilescope.util.UserUtil;
//import com.mobliescope.database.auth.RecentTournament;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TourDetails extends Activity {
	private String _networkName;
	private String _transationID;
	private String _signID;
	private String _pid;
	private ProcessData pd;
	TableLayout tableLayout;
	UserUtil userUtil;
	TournamentSearchHistory _tourSearchHistory;
	TourDetailsAsyncTask _tourDetails = new TourDetailsAsyncTask();
	String authResponse;
	Context _context;

	DBUserInfo db;
	private UserSettings userSettings;
	TouchHighLight tchHighLight = new TouchHighLight();

	public void onCreate(Bundle savedInstanceState) {
		userUtil = new UserUtil();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourdetail);
		this.setTitle("MobileScope");
		Intent i = getIntent();
		Bundle b = i.getExtras();
		_context = this;

		_networkName = b.getString("NETWORK");
		if (_networkName == "All Networks") {
			_networkName = "*";
		}
		_transationID = b.getString("TOURNAMEID");
		SharedPreferences sharedPrefs = this.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
		_signID = sharedPrefs.getString("sign_id", "NULL");
//		_signID = b.getString("SIGNID");
		_pid = b.getString("PID");
		db = new DBUserInfo(this);
		db.openDB();
		_tourSearchHistory = new TournamentSearchHistory(_transationID,
				_networkName);
		_tourSearchHistory.insertTourHistory(db);

		// processTournament(_networkName,_transationID,_signID,_pid);
		_tourDetails.execute(_networkName, _transationID, _signID, _pid);

	}

	public void tourDetail(String _authResponse) {
		JsonUtils jsonUtils = new JsonUtils();
		JSONObject jsonObject;
		JSONObject typeTournament;
		JSONArray tourjsonarray;
		String game;
		String entrants;
		String prize;
		String errorMessage=null;
		pd = new ProcessData(_authResponse);
		try {
			JSONObject errorNode = pd.processErrorNode("Response", _authResponse);
			if(errorNode != null){
				
				try {
					errorMessage = errorNode.getString("$");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				simpleBox("Info",errorMessage);
			}
			else
			{
			typeTournament=pd.processRootNode("Response", _authResponse)
					.getJSONObject("TournamentResponse");
			
			if(typeTournament.has("CompletedTournament")){
				jsonObject = pd.processRootNode("Response", _authResponse)
					.getJSONObject("TournamentResponse")
					.getJSONObject("CompletedTournament");
				prize = jsonObject.getString("@prizePool");
			}else{
				jsonObject = pd.processRootNode("Response", _authResponse)
						.getJSONObject("TournamentResponse")
						.getJSONObject("ActiveTournament");
				Double value = Double.valueOf(jsonObject.getString("@stake"))+Double.valueOf(jsonObject.getString("@rake"));
				prize = String.valueOf(value);
			}

			game = jsonObject.getString("@structure");
			entrants = jsonObject.getString("@totalEntrants");
//			prize = jsonObject.getString("@prizePool");
			TextView txtgame = (TextView) findViewById(R.id.tourdetailtxtgame);
			TextView txtentrants = (TextView) findViewById(R.id.tourdetailtxttotal);
			TextView txtprize = (TextView) findViewById(R.id.tourdetailtxtprize);
			txtgame.setText(game);
			txtentrants.setText(entrants);
			txtprize.setText(prize);
			tourjsonarray = jsonUtils.getJSONArray(jsonObject,
					"TournamentEntry");
			tableLayout = (TableLayout) findViewById(R.id.tabletourdetail);
			UIObjects uiObjects = new UIObjects();

			for (int i1 = 0; i1 < tourjsonarray.length(); i1++) {
				JSONObject tournamentDetial = tourjsonarray.getJSONObject(i1);
				TableRow tr = addTableRow(tournamentDetial, uiObjects);
				tr.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						final TextView playername;
						String _playername = null;
						v.setBackgroundColor(Color.GRAY);
						System.out.println("Row clicked: " + v.getId());
						TableRow row = (TableRow) v;
						playername = (TextView) row.getChildAt(1);
						Intent i = new Intent();
						_playername = playername.getText().toString();
						i.putExtra("USERNAME", _playername);
						i.putExtra("NETWORK", _networkName);
						i.putExtra("SIGNID", _signID);
						i.putExtra("PID", _pid);
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
				tableLayout.addView(tr, new TableLayout.LayoutParams(
						TableLayout.LayoutParams.FILL_PARENT,
						TableLayout.LayoutParams.WRAP_CONTENT));
			}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String processTournament(final String network, String transationID,
			final String signID, final String pid) {

		HttpCreater hc = new HttpCreater();

		String _network;
		// final String pid = userUtil.getEncryCredentail(signID);
		// /api/mobilescope/networks/microgame/tournaments/4858544
		if (network.equals("All Networks")) {
			_network = "*";
		} else {
			_network = network;
		}

		String formUrl = hc.formUrl("networks/" + _network + "/tournaments/"
				+ transationID);
		String[] playersList = null;
		authResponse = hc.HttpProcessUserAccess(formUrl, signID, pid,
				"tournament48858544.txt", null);
		System.out.println("AuthResponse[TourDetail]:" + authResponse);
		try {

			if (authResponse.equals(null)) {

				return "Empty";
			}

		} catch (Exception e) {
			return "Empty";
		}
		return authResponse;
	}

	public TableRow addTableRow(JSONObject recentTournament, UIObjects uiObjects) {
		TableRow tr = new TableRow(this);
		String prize = "0";
		String position = null;
		String playerName = null;
		userSettings = new UserSettings(this);
		try {
			// prize=recentTournament.getString("@prize");
			prize = userUtil.getJSONDefaultValue(recentTournament, "@prize",
					"-");
			position =userUtil.getJSONDefaultValue(recentTournament, "@position",
					"-");// recentTournament.getString("@position");
			playerName = recentTournament.getString("@playerName");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		String curValue = "#";
		System.out.println("Player Name:" + playerName);
		// tr.setId(Integer.valueOf(Id));
		tr.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.FILL_PARENT));

		View layoutInflater = LayoutInflater.from(this).inflate(
				R.layout.textviewlayout, tr, false);
		View layoutInflater1 = LayoutInflater.from(this).inflate(
				R.layout.textviewlayout, tr, false);
		View layoutInflater2 = LayoutInflater.from(this).inflate(
				R.layout.textviewlayout, tr, false);
		// View layoutInflater3 =
		// LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
		// View layoutInflater4 =
		// LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
		//
		TextView tourPostion = addTextView(position, "75", layoutInflater,
				false, false, uiObjects, .10);
		TextView tourplayerName = addTextView(playerName, "100",
				layoutInflater1, false, false, uiObjects, .50);
		SpannableString content = new SpannableString(playerName);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		tourplayerName.setText(content);

		TextView tourprize = addTextView(userSettings.userCurrValue(prize), "75",
				layoutInflater2, false, false, uiObjects, .40);

		tr.addView(tourPostion);
		tr.addView(tourplayerName);
		tr.addView(tourprize);

		return tr;

	}

	public TextView addTextView(String value, String width,
			View layoutInflater, boolean type, boolean rowvalue,
			UIObjects uiObjects, Double weight) {

		return uiObjects.createTextViewSize(value, width, layoutInflater, type,
				rowvalue, Integer.valueOf(10), weight);

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
		MobileScopeMenu _mobileScopeMenu = new MobileScopeMenu();
		Intent i = new Intent();
		i.putExtra("SIGNID", _signID);
		 i =_mobileScopeMenu.processMenu(i,item);
		startActivity(i);
		return super.onOptionsItemSelected(item);
	}


	private class TourDetailsAsyncTask extends AsyncTask<Object, Void, String> {
		ProgressDialog progressDialog;
		String status1 = "false";
		String _authResponse;

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			String errorMessage=null;
			status1 = "true";
			getStatus();
			progressDialog.cancel();
			if (_authResponse.equals("Empty")) {
				Toast.makeText(_context,
						"Didn't get any result,  please search again",
						Toast.LENGTH_LONG);
			} else {
				
				tourDetail(_authResponse);
				
			}
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(TourDetails.this);
			progressDialog.setMessage("Getting tournament information");
			progressDialog.setCancelable(false);
			progressDialog.show();

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
			try {
				_authResponse = processTournament(_networkName, _transationID,
						_signID, _pid);
				status1 = "true";
				System.out.println("TourDetailsAsycnTask: execuitng"
						+ urlString);
			} catch (Exception error) {

			} finally {
			}

			return null;
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

}
