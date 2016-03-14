package com.mobilescope;

import java.util.ArrayList;

import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.RecentTournament;
import com.mobilescope.history.HistoryDAO;
import com.mobilescope.history.TournamentSearchHistory;
import com.mobilescope.menu.MobileScopeMenu;
import com.mobilescope.util.TouchHighLight;
import com.mobilescope.util.UIObjects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TournamentSearchActivity extends Activity{
	Button _tourNetwork;
	Button _tourSearch;
	String _network;
	String _tourid;
	String _signId;
	String _pId;
	int buttonText;
	Intent i;
	ArrayList<HistoryDAO> historyDetails;
	TournamentSearchHistory tourSearchHistory;
	TableLayout tableLayout;
	HistoryDAO tableHistory;
	DBUserInfo db;
	DisplayMetrics displayMetrics;
	TouchHighLight tchHighLight = new TouchHighLight();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tourSearchHistory = new TournamentSearchHistory();
		setContentView(R.layout.tournamentidsearch);
		float sizeInDip = 3f;
	 	displayMetrics = getResources().getDisplayMetrics();
		_tourNetwork = (Button)findViewById(R.id.buttontouridsearch);
		i = getIntent();
		Bundle b = i.getExtras();
		SharedPreferences sharedPrefs = this.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
		_signId = sharedPrefs.getString("sign_id", "NULL");
//		_signId = sharedPrefs.getString("sign_id", "null");
		
//		_signId = b.getString("SIGNID");
		_pId = b.getString("PID");
	 	i.setClassName("com.mobilescope", "com.mobilescope.SelectNetworkActivity");
	 	_tourNetwork.setOnClickListener(new OnClickListener(){
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(i,buttonText);
			}
       	
        });
	 	
	
	 	_tourSearch = (Button)findViewById(R.id.touridsearch);
	 	_tourSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				/networks/PokerStars/tournaments/372582213
				Intent i = new Intent();
				_network = _tourNetwork.getText().toString();
//				_network = _network == "All Networks"?"*":_network;
				TextView text = (TextView)findViewById(R.id.texttouridsearch);
				_tourid = text.getText().toString();
				
				 i.putExtra("NETWORK", _network);
				 i.putExtra("TOURNAMEID", _tourid);
				 i.putExtra("SIGNID", _signId);
				 i.putExtra("PID", _pId);
				 i.setClassName("com.mobilescope", "com.mobilescope.tour.detail.TourDetails");
				 startActivity(i);
			}
	 		
	 	});
	 	
	 	db=new DBUserInfo(this);
	 	db.openDB();
	 	
	 	historyDetails = tourSearchHistory.toursearchHistory(db);
	 	if(!historyDetails.isEmpty()){
	 		tableLayout = (TableLayout)findViewById(R.id.toursearchhistory);
	        UIObjects uiObjects = new UIObjects();
	        int pixel = uiObjects.convertToPixel(sizeInDip, displayMetrics);
	        for(int i=0; i<historyDetails.size();i++){
	        	tableHistory = historyDetails.get(i);
	        	TableRow tr = addTableRow(tableHistory,uiObjects, i, pixel); 
	        	 tr.setOnClickListener(new OnClickListener() {
	                 public void onClick(View v) {
	                     v.setBackgroundColor(Color.GRAY);
	                     System.out.println("Row clicked: " + v.getId());
	                     Intent i = new Intent();
					    	i.putExtra("NETWORK", historyDetails.get(v.getId()).get_network());
					    	i.putExtra("TOURNAMEID", historyDetails.get(v.getId()).get_name());
					    	i.putExtra("SIGNID",_signId);
					    	i.putExtra("PID", _pId);
					    	i.setClassName("com.mobilescope", "com.mobilescope.tour.detail.TourDetails");
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
	 	db.closeDB();
		
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
		i.putExtra("SIGNID", _signId);
		 i =_mobileScopeMenu.processMenu(i,item);
		startActivity(i);
		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		  super.onActivityResult(requestCode, resultCode, data);
		_tourNetwork = (Button)findViewById(R.id.buttontouridsearch);
		  if (requestCode == buttonText){
		   TextView text = null;
		if (resultCode == RESULT_OK) {
			_tourNetwork.setText(data.getStringExtra("BUTTONTEXT").toString());
		   }
		   else{
			   _tourNetwork.setText("Canceled");
		   }
		  }
		}
	
	   public TableRow addTableRow( HistoryDAO historyDao, UIObjects uiObjects,int i, int pixel){
		    TableRow tr = new TableRow(this);
		     String Id = historyDao.get_name();
	         String stake =historyDao.get_network();
	        
	         String curValue="#";
	        
	         tr.setId(i);
	         tr.setPadding(0, pixel, 0, 0);
//	         tr.setId(Integer.valueOf(Id));
	         tr.setLayoutParams(new TableRow.LayoutParams(
	                 TableRow.LayoutParams.FILL_PARENT,
	                 TableRow.LayoutParams.FILL_PARENT));
	         
	        View layoutInflater = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
	        View layoutInflater1 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
	        
	        TextView tourId = addTextView(Id,"150",layoutInflater,false,false,uiObjects,.50);
	        SpannableString content = new SpannableString(Id);
	        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
	        tourId.setText(content);
	        TextView tourStake = addTextView(stake,"150",layoutInflater1,false,false,uiObjects,.50);
	        
	        tr.addView(tourId);
	        tr.addView(tourStake);
	        return tr;
	    
	   }
	   
	   public TextView addTextView(String value, String width,View layoutInflater,boolean type,boolean rowvalue,UIObjects uiObjects,Double weight){
		   
	       return uiObjects.createTextViewSize(value, width, layoutInflater, type,rowvalue,Integer.valueOf(10),weight);
	       
	   }

}
