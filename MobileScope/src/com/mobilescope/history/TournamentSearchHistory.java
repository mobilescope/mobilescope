package com.mobilescope.history;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.leader.Leaderboard;

public class TournamentSearchHistory {
	
	DBUserInfo _dbUserInfo; 
	String _tourId;
	String _tableName="TourSearchHistory";
	
	public TournamentSearchHistory(String _tourId, String _playerNetwork) {
		
		this._tourId = _tourId;
		this._playerNetwork = _playerNetwork;
	}
	public String get_tourId() {
		return _tourId;
	}
	public void set_tourId(String _tourId) {
		this._tourId = _tourId;
	}
	public String get_playerNetwork() {
		return _playerNetwork;
	}
	public void set_playerNetwork(String _playerNetwork) {
		this._playerNetwork = _playerNetwork;
	}
	String _playerNetwork;
	
	public void insertTourHistory(DBUserInfo db){
		try{
		 db.insertHistory(_tableName, _tourId, _playerNetwork);
		 
		}catch(Exception e){
		
		}
	}
	
	public TournamentSearchHistory() {
		super();
		try{
			 _dbUserInfo = new DBUserInfo();
			
			}catch(Exception e){
			
			}
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<HistoryDAO> toursearchHistory(DBUserInfo db){
		  Cursor cursor=null;
		  ArrayList<HistoryDAO> playerhistory = new ArrayList<HistoryDAO>();
//		  ArrayList<String> category = lb.dbleaderdefaultCategory(db);
		  String query = "select tourid,tournetwork from "+_tableName+" order by lookdate desc";
//		  		" where rankingStatisticTitle='";
//		  query+=Title+"' AND year='"+category.get(0)+"' AND category='"+category.get(1)+"' AND subcategory ='"+category.get(2)+"'";
		  Log.i("Debug","[toursearchHistory]"+query);
		
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
