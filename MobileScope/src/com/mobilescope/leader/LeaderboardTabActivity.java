package com.mobilescope.leader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import com.mobilescope.R;
import com.mobilescope.menu.MobileScopeMenu;
import com.mobilescope.search.PlayerHistory;
import com.mobilescope.util.UIObjects;
//import com.mobilescope.asynctask.LeaderboardAsyncTask;
import com.mobilescope.database.auth.DBUserInfo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

public class LeaderboardTabActivity extends TabActivity {
	String Year;
	String Subcategory;
	String SubCategoryDisplayOrder;
	String CategoryDisplayOrder;
	String Category;
	DBUserInfo db;
	String[] TabTitle = null;
	String tabTitle = null;
	Leaderboard lb;
	ArrayList<String> leaderdefault;
	String urlString = null;

	ArrayList<LeaderboardRankActivity> LeaderBoardCategory;
	Button year;
	Button category;
	Button subcategory;
	Context mContext;

	String workObject;
	LeaderboardsAsyncTask leaderboardAsyncTask ;
	ProgressDialog progressDialog;
	
	String[] valueCategory = { "Any Game", 
			"Holdem Heads Up", "Omaha Hi Heads Up", "Omaha HiLo Heads Up",
			"Stud Heads Up", "Draw Heads Up", "Mixed Games Heads Up",
			"Holdem 5 to 6 Players", "Holdem 5 to 6 Players Turbo",
			"Any Game 5 to 6 Players Super Turbo", "Stud 5 to 8 Players",
			"Draw 5 to 8 Players", "Mixed Games 6 to 8 Players",
			"Holdem 9 to 10 Players", "Holdem 9 to 10 Players Turbo",
			"Holdem 9 to 10 Players Super Turbo", "Omaha Hi 5 to 10 Players",
			"Omaha HiLo 5 to 10 Players", "Omaha 2 to 3 Table",
			"Any Game 2 to 3 Table", "Any Games 4 to 6 Table",
			"Any Game 7 or More Tables", "Double or Nothing and Fifty50",
			"Streaks", "Scheduled", "Heads Up Turbo", "Heads Up Super Turbo",
			"Scheduled by Network" };
	private String _signID;

	public String getYear() {
		return Year;
	}

	@TargetApi(4)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rankdetail);
		this.setTitle("MobileScope");
		db = new DBUserInfo(this);
		mContext=this;
		leaderboardAsyncTask = new LeaderboardsAsyncTask(mContext);
		UIObjects uiObject = new UIObjects();
		db.openDB();
		Intent intent1 = getIntent();
		Bundle b = intent1.getExtras();
		SharedPreferences sharedPrefs = this.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
		_signID = sharedPrefs.getString("sign_id", "NULL");
//		_signID = sharedPrefs.getString("sign_id", "null");
//		_signID = b.getString("SIGNID");
		
		db.removeLeader();
		// leaderdefault = lb.dbleaderdefaultCategory(db);
		// urlString = lb.processURLString(leaderdefault);
		// lb.processLeader(db,urlString);
//		progressDialog = new ProgressDialog(LeaderboardTabActivity.this);
//		progressDialog.setMessage("Getting leader information");
//		progressDialog.setCancelable(false);
//		progressDialog.show();
		try {
			leaderboardAsyncTask.execute(db);
			// while(leaderboardAsyncTask.get().equals("true")){
			// System.out.println(leaderboardAsyncTask.get().toString());
			// }
			leaderboardAsyncTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("DEBUG", "got leaderboard value");
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		TabTitle = UniqueTabTitle();
		// Create an Intent to launch an Activity for the tab (to be reused)
		for (int i = 0; i < TabTitle.length; i++) {
			tabTitle = TabTitle[i];
			Log.i("Debug", "Title:" + tabTitle);
			intent = new Intent().setClass(this, LeaderboardRankActivity.class);
			intent.putExtra("TITLE", tabTitle);
			View tabview = uiObject.createTabView(tabHost.getContext(),
					tabTitle);
			spec = tabHost.newTabSpec(tabTitle).setIndicator(tabview)
					.setContent(intent);
			tabHost.addTab(spec);
			intent = new Intent().setClass(this, LeaderCategoryActivity.class);
			// Intent lb = new Intent();
			// lb.setClassName("com.mobilescope",
			// "com.mobilescope.leader.LeaderCategoryActivity");
			// startActivity(lb);
		}

		year = (Button) findViewById(R.id.year);
		category = (Button) findViewById(R.id.category);
		subcategory = (Button) findViewById(R.id.subcategory);
		year.setText(db.getSingleTableValue("year", "0", "0",
				"PokerLeaderCategoryDefault"));
		category.setText(db.getSingleTableValue("category", "0", "0",
				"PokerLeaderCategoryDefault"));
		subcategory.setText(db.getSingleTableValue("subcategory", "0", "0",
				"PokerLeaderCategoryDefault"));
		year.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				workObject = "year";
				AlertCreateDialog(workObject, year);
			}
		});

		category.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				workObject = "category";
				AlertCreateDialog(workObject, category);
			}
		});

		subcategory.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				workObject = "subcategory";
				AlertCreateDialog(workObject, subcategory);
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
		MobileScopeMenu _mobileScopeMenu = new MobileScopeMenu();
		Intent i = new Intent();
		i.putExtra("SIGNID", _signID);
		 i =_mobileScopeMenu.processMenu(i,item);
		startActivity(i);
		return super.onOptionsItemSelected(item);
	}


	public String[] UniqueTabTitle() {
		String[] value = null;
		String query = "select DISTINCT rankingStatisticTitle from PokerLeader";
		return (db.processSingleRowQuery(query, "rankingStatisticTitle"));
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getSubcategory() {
		return Subcategory;
	}

	public void setSubcategory(String subcategory) {
		Subcategory = subcategory;
	}

	public String getSubCategoryDisplayOrder() {
		return SubCategoryDisplayOrder;
	}

	public void setSubCategoryDisplayOrder(String subCategoryDisplayOrder) {
		SubCategoryDisplayOrder = subCategoryDisplayOrder;
	}

	public String getCategoryDisplayOrder() {
		return CategoryDisplayOrder;
	}

	public void setCategoryDisplayOrder(String categoryDisplayOrder) {
		CategoryDisplayOrder = categoryDisplayOrder;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	// Category functions

	public void AlertCreateDialog(final String field, final Button bName) {
		final String[] value = FieldValuesName(field);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select " + field);
		builder.setSingleChoiceItems(value, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						Toast.makeText(getApplicationContext(), value[item],
								Toast.LENGTH_SHORT).show();
						updateLeaderDefault(field, value[item], bName.getText());
						bName.setText(value[item]);
						Intent i = new Intent();
						i.setClassName("com.mobilescope",
								"com.mobilescope.leader.LeaderboardTabActivity");
						startActivity(i);
					}

				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void updateLeaderDefault(String field, String value,
			CharSequence text) {
		String query = "update PokerLeaderCategoryDefault set " + field + "='"
				+ value + "' where " + field + "='" + text.toString() + "'";
		db.processSingleRowQuery(query, field);

	}

	public String[] FieldValuesName(String field) {
		String[] value = null;
		if (field.equals("year")) {
			// String[] value1={"2009","2010","2011","2012"};
			String[] value1 = getYears();
			value = value1;
		} else {
			value = FieldValue(field);
		}
		if (field.equals("category")) {
			value = valueCategory;
		}
		return value;
	}

	public String[] FieldValue(String field) {
		String value[] = null;
		String query = "Select DISTINCT " + field + " from PokerLeaderCategory";
		return (db.processSingleRowQuery(query, field));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Select " + this.workObject);
		String[] networks = FieldValuesName(this.workObject);
		for (int i = 0; i < networks.length; i++) {
			menu.add(Menu.NONE, v.getId(), i, networks[i]).setCheckable(true);
		}

	}

	public String[] getYears() {
		int noofyears = 5;
		String[] years = new String[noofyears];
		int year = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = 0; i < noofyears; i++) {

			years[i] = String.valueOf(year - i);
		}
		return years;
	}

	private class LeaderboardsAsyncTask extends AsyncTask<Object, Void, String> {
		
		private LeaderboardsAsyncTask(Context _context) {
			super();
			this._context = _context;
		}

		String status1 = "false";
		ProgressDialog progressDialog2;
		Context _context;

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			status1 = "true";
			getStatus();
			progressDialog2.cancel();
		}

		protected void onPreExecute() {
			progressDialog2 = new ProgressDialog(_context);
			progressDialog2
					.setMessage("Getting leader details, please wait for mobilescope to get all the data");
			progressDialog2.setCancelable(false);
			progressDialog2.show();

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
				leaderdefault = lb.dbleaderdefaultCategory(db);
				urlString = lb.processURLString(leaderdefault);

				lb.processLeader(db, urlString);
		
				status1 = "true";
				System.out.println("LoginAsycnTask: execuitng" + urlString);
			} catch (Exception error) {
		
			} finally {
			}
		
			return null;
		}

	}

}
