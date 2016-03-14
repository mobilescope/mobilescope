package com.mobilescope.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.mobilescope.R;
import com.mobilescope.TournamentSearchActivity;


public class MobileScopeMenu{
	MenuItem item;
public Intent processMenu(Intent i, MenuItem item){
	switch (item.getItemId())
    {
    case R.id.menu_home:
    	i.setClassName("com.mobilescope", "com.mobilescope.MainPageActivity");
	   	return i; 
    	default:
    	case R.id.menu_settings1:
    	i.setClassName("com.mobilescope", "com.mobilescope.preference.UserPreferenceActivity");	
    }
	return null;
}

}
