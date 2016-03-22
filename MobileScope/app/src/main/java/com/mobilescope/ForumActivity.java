package com.mobilescope;

import com.mobilescope.menu.MobileScopeMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

public class ForumActivity extends Activity {

	private String _signId;


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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forumpage);
		Intent i = getIntent();
		Bundle b = i.getExtras();
		SharedPreferences sharedPrefs = this.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
		_signId = sharedPrefs.getString("sign_id", "NULL");
//		_signId = sharedPrefs.getString("SIGNID", "NULL");
		WebView myWebView = (WebView) findViewById(R.id.webview);
		myWebView.loadUrl("http://www.sharkscopers.com/forum");
		
//		/javascript/flowplayer/flowplayer.commercial-3.2.12.swf
	}
	
	

}
