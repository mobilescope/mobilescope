package com.mobilescope.history;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.leader.Leaderboard;
import com.mobilescope.leader.PlayerRank;

public class PlayerSearchHistory {

	public PlayerSearchHistory(String _playerName, String _playerNetwork) {
		
		this._playerName = _playerName;
		this._playerNetwork = _playerNetwork;
	}
	public PlayerSearchHistory() {
		// TODO Auto-generated constructor stub
		super();
		try{
			 _dbUserInfo = new DBUserInfo();
			
			}catch(Exception e){
			
			}
	}
	DBUserInfo _dbUserInfo; 
	String _playerName;
	String _tableName="PlayerSearchHistory";
	public String get_playerName() {
		return _playerName;
	}
	public void set_playerName(String _playerName) {
		this._playerName = _playerName;
	}
	public String get_playerNetwork() {
		return _playerNetwork;
	}
	public void set_playerNetwork(String _playerNetwork) {
		this._playerNetwork = _playerNetwork;
	}
	String _playerNetwork;
	
	public void insertPlayerHistory(DBUserInfo db){
		try{
		 db.insertHistory(_tableName, _playerName, _playerNetwork);
		 
		}catch(Exception e){
		
		}
	}
	
	public ArrayList<HistoryDAO> playersearchHistory(DBUserInfo db){
		  Cursor cursor=null;
		  ArrayList<HistoryDAO> playerhistory = new ArrayList<HistoryDAO>();
//		  ArrayList<String> category = lb.dbleaderdefaultCategory(db);
		  String query = "select playername,playernetwork from "+_tableName+" order by lookdate desc";
//		  		" where rankingStatisticTitle='";
//		  query+=Title+"' AND year='"+category.get(0)+"' AND category='"+category.get(1)+"' AND subcategory ='"+category.get(2)+"'";
		  Log.i("Debug","[playerrsearchHistory]"+query);
		
		   cursor = db.processSingleRowQuery(query,cursor);
		  if(cursor.moveToFirst())
		   {
		       do
		       {
		    	   HistoryDAO prank = new HistoryDAO();
		    	   prank._name=cursor.getString(0);
		    	   prank._network=cursor.getString(1);
		    	   playerhistory.add(prank);
		          
		       }while(cursor.moveToNext());
		       if(cursor != null && !cursor.isClosed())
		          cursor.close();
		   }
		  _dbUserInfo.closeDB();
		  return playerhistory;
	  }
	  
	
}
