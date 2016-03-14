package com.beatme.com.beatme.datatype;

import java.util.ArrayList;

import com.mobilescope.database.auth.DBUserInfo;

import android.content.res.AssetManager;
import android.database.Cursor;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: Nov 8, 2011
 * Time: 9:48:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserInfo {
    public String metadatahash=null;
    public String userid;
    public String Error;
   
    private String _username;
	private String _password;
	private String _currency; // not yet implemented
	
	private String _queryString="select user_name,encrytPassword from PokerUserInfo";
	
	public UserInfo(String _username) {
		super();
		this._username = _username;
	}
	public UserInfo(DBUserInfo db){
		super();
		getUserInfo(db);
	}
	public UserInfo() {
		// TODO Auto-generated constructor stub
	}
	public String get_username() {
		return _username;
	}
	public void set_username(String _username) {
		this._username = _username;
	}
	public String get_password() {
		return _password;
	}
	public void set_password(String _password) {
		this._password = _password;
	}
	public String get_currency() {
		return _currency;
	}
	public void set_currency(String _currency) {
		this._currency = _currency;
	}
	


    public void setmetadatahash(String metadatahash){
        this.metadatahash = metadatahash;
    }

    public String getmetadatahash(){
        return this.metadatahash;
    }

    public void setuserid(String userid){
        this.userid=userid;
    }
    
    public String getuserid(){
           return this.userid;
    }

    public void setError(String Error){
        this.Error=Error;
    }

    public String getError(){
           return this.Error;
    }
    
    public void getUserInfo(DBUserInfo db){
    	Cursor cursor=null;
   	    
   	  cursor = db.processSingleRowQuery(_queryString,cursor);
   	  if(cursor.moveToFirst())
   	   {
   	       do
   	       {
   	    	   _username=cursor.getString(0);
   	    	   _password=cursor.getString(1);
   	       }while(cursor.moveToNext());
	       if(cursor != null && !cursor.isClosed())
		          cursor.close();   
   	   }
    }
    
}
