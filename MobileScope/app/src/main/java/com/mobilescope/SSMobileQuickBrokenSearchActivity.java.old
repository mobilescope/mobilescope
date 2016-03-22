package com.mobilescope;

import android.content.res.AssetManager;
import android.util.Log;
import android.view.*;
import android.view.View.OnKeyListener;

import com.beatme.process.data.ProcessData;
import com.beatme.restclient.http.HttpCreater;
import com.beatme.restclient.http.RequestMethod;
import com.beatme.restclient.http.RestClient;
import com.mobilescope.processdata.ProcessPlayerStatus;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.SSMobileSQLite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.WebView;
import android.widget.*;
import android.view.ContextMenu.ContextMenuInfo;


import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import javax.security.auth.login.LoginException;

public class SSMobileQuickBrokenSearchActivity extends Activity implements OnItemClickListener, OnItemSelectedListener, Runnable
{
    Button back,exit;
    TextView result;
    AutoCompleteTextView quickSearchView;
    String quickTextPrefix = "al";
    String signID;
    String pid;
    DBUserInfo db;
    private String _playerName;
    private String _playerNetwork;
    private SSMobileSQLite _SSMobileSQLite;
    private Button mainButton;
    private Button _leaderboard;
    private int _limit=10;
    ProgressDialog progressDialog;
    Context mContext = this;
    Thread thread;
    String[] playersList;
//    WebView mWebView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchpanel);
        db= new DBUserInfo(this);
        db.openDB();
        db.assetManager=getAssets();
        _SSMobileSQLite = new SSMobileSQLite(this);
        _SSMobileSQLite.openDB();

        Intent i = getIntent();
        Bundle b = i.getExtras();
        
//        signID = b.getString("SIGNID");
//        pid=db.getValue("encrytPassword", "user_name", signID);;
//        
        signID =db.getColumnValue("user_name");
        pid = db.getColumnValue("encrytPassword");
        
        mainButton = (Button)findViewById(R.id.searchgo);
        mainButton.setEnabled(false);   
        _leaderboard = (Button)findViewById(R.id.leaderboards);
        final String[] playersList = new String[_limit];
//        String[] playersList = initializeAutoComplete(10,signID,pid,db.getAssetManager());
//        mWebView = (WebView) findViewById(R.id.webview);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.loadUrl("http://beta.sharkscope.com/#Leaderboards//2012/Any-Game/Any%20Stakes");
//        mWebView.bringToFront();
//        
        quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, playersList);
        quickSearchView.setAdapter(adapter);
        quickSearchView.setHint("Search Player name");
        quickSearchView.setThreshold(3);
        registerForContextMenu(quickSearchView);
        mainButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//			    registerForContextMenu(arg0);
//                openContextMenu(arg0);
//                unregisterForContextMenu(arg0);
//                function1(_playerNetwork);
				AlertCreateDialog();
               }
        });
    
        _leaderboard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
		    	
		    	i.setClassName("com.mobilescope", "com.mobilescope.leader.LeaderboardTabActivity");
		    	startActivity(i);	
			}
		}) ;
        
    // attach listeners
        quickSearchView.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int before, int after) { }

            public void onTextChanged(CharSequence s, int start,
                    int before, int after) {

//                 updateAutoComplete();
//            	createProgressDialogThread();
            }

            public void afterTextChanged(Editable s) {
//                        updateAutoComplete();
	    		 EditText quickTextObj = (EditText)findViewById(R.id.quickSearch);
		  	        final String quickText = quickTextObj.getText().toString();
		  	    


		                Thread thread = new Thread(new Runnable() {
		 		           

						@Override
		 		           public void run() {
		 		        	   Looper.prepare();
		 		               runOnUiThread(new Runnable(){
		 		                   @Override
		 		                   public void run() {
		 		                	   
		 		                	  progressDialog = ProgressDialog.show(mContext, "", "Getting list of users", true);
		 		                   }                   
		 		               });
		 		              getPlayerList(quickText);
		 		               runOnUiThread(new Runnable(){
		 		                   @Override
		 		                   public void run() {
		 		                	   
		 	
		 		                	   progressDialog.dismiss();
		 		                	  createAdapter();
		                            Log.i("Debug","Killing the progessdialog");
		 		                  	

		 		                   }                   
		 		               });     
		 		           }
		 		       });
		 		       thread.start();
		            }


        
        }
        );
   //     quickSearchView.setOnKeyListener(quickSearchKeyListener);
    
//        quickSearchView.setOnItemClickListener(this);
//        quickSearchView.setOnItemSelectedListener(this);
        
        
        
//        quickSearchView.setOnKeyListener(new OnKeyListener(){
//
//			@Override
//			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
//				// TODO Auto-generated method stub
//				 createProgressDialogThread();
//					return false;
//			}
//			
//        });
       
        quickSearchView.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
			  	
				return false;
			}
        	
        });
        
  
        
    }

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.activity_select_network, menu);
        return true;
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())
        {
        case R.id.menu_home:
        	Intent i = new Intent();
            i.setClassName("com.mobilescope", "com.mobilescope.MobileScopeMain");
		   	startActivity(i);
        default:
        }
		return super.onOptionsItemSelected(item);
	}

    public void createProgressDialogThread(){
		  EditText quickTextObj = (EditText)findViewById(R.id.quickSearch);
	        final String quickText = quickTextObj.getText().toString();

	        if(quickText != null && quickText.trim().length() >= 3) {
	        	progressDialog = ProgressDialog.show(mContext, "", "Getting list of users", true);
	        	 thread = new Thread(this);
	        	 thread.start();
			              
			              
	        }

    }
   
    
   public void AlertCreateDialog(){
	   final String[] networks=getNetworkNames();
	   AlertDialog.Builder builder = new AlertDialog.Builder(this);
	   builder.setTitle("Select Network");
	   builder.setSingleChoiceItems(networks, -1, new DialogInterface.OnClickListener() {
	       public void onClick(DialogInterface dialog, int item) {
	           Toast.makeText(getApplicationContext(), networks[item], Toast.LENGTH_SHORT).show();
	           processPlayerNetworkInfo(networks[item]);
	           
	       }
	   });
	   AlertDialog alert = builder.create();
	   alert.show();
   }

   public void getPlayerList(String quickText){
	  playersList=null;
	  
	if(!_SSMobileSQLite.checkPlayerPrefix(quickText)) {
            quickTextPrefix = quickText;
            _SSMobileSQLite.removeAllPlayers();
            playersList = initializeAutoComplete(25,signID,pid,db.getAssetManager());
         } else{
        	 playersList = _SSMobileSQLite.getUniquePlayers();
         }
	  
   }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Network");
        String[] networks=getNetworkNames();
        for (int i=0;i<networks.length;i++){
        menu.add(Menu.NONE, v.getId(), i, networks[i]).setCheckable(true);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        function1(item.getTitle().toString());
        processPlayerNetworkInfo(item.getTitle().toString());
       
         return true;
    }

    public void processPlayerNetworkInfo(String network){
    	 _playerNetwork=network;

//    	 progressDialog = ProgressDialog.show(this, "",
//    			 "Getting user information wait for few seconds...", true);
//       ProcessPlayerStatus processPlayerStatus =((ProcessPlayerStatus)getApplicationContext());
//       processPlayerStatus.set_assetManager(db.getAssetManager());
//       processPlayerStatus.set_networkName(_playerNetwork);
//       processPlayerStatus.set_userName(_playerName);
//       processPlayerStatus.set_userid(signID);
//       processPlayerStatus.set_passid(pid);
//       processPlayerStatus.processPlayer();
       Intent i = new Intent();

       i.putExtra("USERNAME", _playerName);
       i.putExtra("NETWORK",_playerNetwork);
       i.putExtra("SIGNID",signID);
       i.putExtra("PID", pid);
       i.setClassName("com.mobilescope", "com.mobilescope.SSMobilePlayerStatus");
//     i.setClassName("com.mobilescope", "com.mobilescope.search.RecentTournamentHistory");
//     i.setClassName("com.mobilescope", "com.mobilescope.search.PlayerHistory");
//        i.setClassName("com.mobilescope", "com.mobilescope.search.PlayerGraph");
//       progressDialog.dismiss();     
                      startActivity(i);

    }
    
    public void createAdapter(){
        quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
        //ArrayAdapter<String> adapter = (ArrayAdapter<String>) quickSearchView.getAdapter();
        //adapter.clear();
        
        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(this, R.layout.list_item, playersList);
        quickSearchView.setAdapter(newAdapter);
        quickSearchView.showDropDown();
    }
    
    public void function1(String network){
        Toast.makeText(this, network, Toast.LENGTH_SHORT).show();
    }


    /**
      * Implements OnItemClickListener
      */
     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // perform the quick search ..
        EditText quickTextObj = (EditText)findViewById(R.id.quickSearch);
        String quickText = quickTextObj.getText().toString();
         Log.i("DEBUG","[onItemClick]quick Search value is "+quickText);
         _playerName=quickText;
        // should get the network from the SQLLite and perform the search ..
     }

     /**
      * Implements OnItemSelectedListener
      */
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         // perform the quick search ..
          EditText quickTextObj = (EditText)findViewById(R.id.quickSearch);
          String quickText = quickTextObj.getText().toString();
//          registerForContextMenu(quickTextObj);

          Log.i("DEBUG","[onItemSelected]quick Search value is "+quickText);
      }
    /**
      * Implements OnItemSelectedListener
      */
     public void onNothingSelected(AdapterView<?> parent) {

     }


/**
     * Callback to watch the textedit field for empty/non-empty
     */
    private TextWatcher quickSearchTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int before, int after) { }

        public void onTextChanged(CharSequence s, int start,
                int before, int after) {

//             updateAutoComplete();
//        	createProgressDialogThread();
        }

        public void afterTextChanged(Editable s) {
//                    updateAutoComplete();
        	createProgressDialogThread();
        }
    };


    private String[] initializeAutoComplete(int limit,String signid,String pid,AssetManager assetManager) {
    	 HttpCreater hc = new HttpCreater();
         ProcessData pd;
         JSONArray  AutoComplete=null;
    	 String formUrl= hc.formUrl("networks/*/players/"+quickTextPrefix+"/autocomplete?limit="+limit);
         String[] playersList = null;

                String authResponse = hc.HttpProcessUserAccess(formUrl,signid,pid,"qsrusername.txt",assetManager);
//                String authResponse = HttpProcess(formUrl,signid,pid);

                System.out.println("AuthResponse:"+authResponse);
               try{
                pd = new ProcessData(authResponse);
                AutoComplete = pd.processAutoComplete(authResponse,"Response");
                for (int i=0;i<AutoComplete.length();i++){
                    JSONObject autoComplete = AutoComplete.getJSONObject(i);
                    String playerName = autoComplete.getString("@name");
                    String playerNetwork=autoComplete.getString("@network");
                    if(playerName != null) {
                                _SSMobileSQLite.insertPlayer(playerName, playerNetwork, quickTextPrefix);
                    }
                }
                
                
            }  catch (Exception e){
                System.out.println("Error executing the Request");
                mainButton.setEnabled(false);
            }
          finally {
        	  playersList = _SSMobileSQLite.getUniquePlayers();
        	 }
        return playersList;
    }

    private String HttpProcess(String formUrl, String signid, String pid){
         String authResponse=null;
                String accept="application/json";
                RestClient rc = new RestClient(formUrl);
                rc.AddHeader("Accept",accept);
                rc.AddHeader("Username", signid);
                rc.AddHeader("Password", pid);
                try{
                    rc.Execute(RequestMethod.GET);
                    authResponse = rc.getResponse();
                }catch (Exception e){
                    System.out.println("Error executing the Request");
                }
         return authResponse;

    }

    private String[] getNetworkNames(){
       String username=getQuickSearchString();
       return _SSMobileSQLite.getPlayerNetwork(username);
    }

    private String getQuickSearchString(){
        EditText quickTextObj = (EditText)findViewById(R.id.quickSearch);
        return quickTextObj.getText().toString();
    }
    
    private void quickSearchfill(String quickText){
        if(!_SSMobileSQLite.checkPlayerPrefix(quickText)) {
            quickTextPrefix = quickText;
            _SSMobileSQLite.removeAllPlayers();
            String[] playersList = initializeAutoComplete(25,signID,pid,db.getAssetManager());
            if(playersList != null && playersList.length > 0) {
                quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
                //ArrayAdapter<String> adapter = (ArrayAdapter<String>) quickSearchView.getAdapter();
                //adapter.clear();
                ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(this, R.layout.list_item, playersList);
                quickSearchView.setAdapter(newAdapter);
                quickSearchView.showDropDown();
//                mainButton.setEnabled(true);
            }
        }
        else {
            String[] playersList = _SSMobileSQLite.getUniquePlayers();
            if(playersList != null && playersList.length > 0) {
                quickSearchView = (AutoCompleteTextView) findViewById(R.id.quickSearch);
                //ArrayAdapter<String> adapter = (ArrayAdapter<String>) quickSearchView.getAdapter();
                //adapter.clear();
                ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(this, R.layout.list_item, playersList);
                quickSearchView.setAdapter(newAdapter);
                quickSearchView.showDropDown();
//                mainButton.setEnabled(true);
            }else{
//            	mainButton.setEnabled(false);
            }
        }
    }

    private void updateAutoComplete() {
        EditText quickTextObj = (EditText)findViewById(R.id.quickSearch);
        final String quickText = quickTextObj.getText().toString();

        if(quickText != null && quickText.trim().length() >= 3) {
        	 Thread thread = new Thread(new Runnable() {
		           @Override
		           public void run() {
		        	   Looper.prepare();
		               runOnUiThread(new Runnable(){
		                   @Override
		                   public void run() {
		                	   
		                	   progressDialog = ProgressDialog.show(mContext, "", "Getting list of users", true);
		                   }                   
		               });
		               quickSearchfill(quickText);
		               runOnUiThread(new Runnable(){
		                   @Override
		                   public void run() {
		                	  	                       
		                	   progressDialog.dismiss();
		                	
		                   }                   
		               });     
		           }
		       });
		       thread.start();
		       while(thread.isAlive()){
		    	   Log.i("DEBUG","In update Auto complete thread");
		       }
             
        }
    }


    /**
        * React to the user typing "enter" or other hardwired keys while typing in the search box.
        * This handles these special keys while the edit box has focus.
        */
       View.OnKeyListener quickSearchKeyListener = new View.OnKeyListener() {
           public boolean onKey(View v, int keyCode, KeyEvent event) {
//               updateAutoComplete();
               return false;
           }
       };


    public void onDestroy()
    {
        super.onDestroy();
        if(_SSMobileSQLite != null) {
            _SSMobileSQLite.close();
        }
    }


	@Override
	public void run() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(0);
    
	}
	
	private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
        	Log.i("DEBUG","In update Auto complete thread");
        	progressDialog.dismiss();
            registerForContextMenu(quickSearchView);
        }
    	
    };	
	
} 

