package com.mobilescope.preference;

import com.mobilescope.util.AppConstant;
import com.mobilescope.util.AppConstant.CurrType;
import com.mobilescope.util.UserCurrEnum;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class UserSettings extends Activity{
	UserCurrEnum userCurr;
	
	public UserSettings(Context _context) {
		super();
		this._context = _context;
	}

	public UserSettings() {
		// TODO Auto-generated constructor stub
	}

	private static final int RESULT_SETTINGS = 1;
	int currSetting;
	Context _context;
	
	public int getCurrSetting() {
		return currSetting;
	}

	public void setCurrSetting(int currSetting) {
		this.currSetting = currSetting;
	}

	public void settingChanges() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 
        StringBuilder builder = new StringBuilder();
 
        builder.append("\n Clear Data: "
                + sharedPrefs.getString("PRE_REFRESH", "NULL"));
 
        builder.append("\n Currency: "
                + sharedPrefs.getString("PRE_CURR", "NULL"));
        builder.append("\n Feedback: "
                + sharedPrefs.getString("PRE_FEEDBACK", "NULL"));
 
        Toast.makeText(_context, builder.toString(), Toast.LENGTH_LONG).show();
 
        
    }

	public void prefSetting(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
		currSetting=Integer.valueOf(sharedPrefs.getString("PRE_CURR", "0"));
	}

	public String userCurrValue(String value){
		String moneyValue="-";
		CurrType currType=CurrType.EUR;
		try{
		if (value.equals(null) || value.equals("")){
			return moneyValue;
		}
		}catch(Exception e){
			return moneyValue;
		}
		prefSetting();
		userCurr = UserCurrEnum.getUserCurrEnum(currSetting);
		moneyValue=userCurr.getCurrency()+value;
		return moneyValue;
	}
	
	public String isStringNull(String value){
		String strValue="0";
		try{
		if (value.equals(null) || value.equals("")){
			return strValue;
		}
		}catch(Exception e){
			return strValue;
		}
		return value;
	}
	
	public static int getResultSettings() {
		return RESULT_SETTINGS;
	}
}
