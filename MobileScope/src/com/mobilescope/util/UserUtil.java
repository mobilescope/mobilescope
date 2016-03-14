package com.mobilescope.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.SSMobileSQLite;
import com.mobilescope.preference.UserSettings;
import com.mobilescope.util.AppConstant.CurrType;

public class UserUtil {
    DBUserInfo db;
    SSMobileSQLite _SSMobileSQLite;
    AppConstant appConstant = new AppConstant();
    public UserUtil(){
    	
    }
    
    public String convertEpochTime(String mtime)
    {
    	Date date = new Date(Long.parseLong(mtime) * 1000L);
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // the format of your date
    	sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
    	String formattedDate = sdf.format(date);
    	System.out.println(formattedDate);
    	return formattedDate;
    	
    }
    
	public String getEncryCredentail(String UserID){
		  db= new DBUserInfo();
	        db.openDB();
	        _SSMobileSQLite = new SSMobileSQLite(this);
	        _SSMobileSQLite.openDB();
//	        db.assetManager=getAssets();
	        String pid = db.getValue("db_encrytPassword", "db_user_name", UserID);
	        db.closeDB();
	       return pid;

	}
	// return String Value
	public String getJSONSingleValue(JSONArray jsonArray, String searchNode){
		String Value="";
		for (int i=0;i<jsonArray.length();i++){
		  	try {
				
				if (jsonArray.getString(i).equals(searchNode)){
					return jsonArray.getString(i);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  	
		}
		return Value;
	}
	
	//Set default value if getJSONObject not found
	public String getJSONDefaultValue(JSONObject object,String searchName, String defaultValue){
		String value="";
    	try{
    		value=object.getString(searchName);
    	}catch(Exception e){
    		return defaultValue;
    	}
    	return value;
	}

	public JSONObject getJSONOBject(JSONArray jsonArray, String searchNode, String searchValue){
		JSONObject Value = null;
		for (int i=0;i<jsonArray.length();i++){
		  	try {
				Value = jsonArray.getJSONObject(i);
				if (Value.getString(searchNode).equals(searchValue)){
					return Value;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  	
		}
		return Value;
	}
	
	public String currValue(String value){
		String moneyValue="-";
		UserSettings userSetting = new UserSettings();
		
		CurrType currType=CurrType.EUR;
		try{
		if (value.equals(null) || value.equals("")){
			return moneyValue;
		}
		}catch(Exception e){
			return moneyValue;
		}
		moneyValue=appConstant.currValue[0]+value;
		return moneyValue;
	}
	
	
	
}
