package com.mobilescope.leader;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.res.AssetManager;
import android.database.Cursor;

import com.beatme.process.data.ProcessData;
import com.beatme.restclient.http.HttpCreater;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.PlayerDetail;
import com.mobilescope.util.JsonUtils;

public class Leaderboard {
    private AssetManager _assetManager;
    private ProcessData pd;
    private JsonUtils jsonUtils;
    private String currentYear="2012";
    String _userid=null;
    String _passid=null;
   
    ArrayList <LeaderCategoryActivity> list_leaderCategory;
    public String urlString = "poker-leaderboards/"+currentYear+"/Any Game/$101-$300";
    public String urlString1 ="poker-leaderboards/"+currentYear;
    
   
    
public void processLeaderboard(DBUserInfo db,String urlString1){	 
	JSONArray jsonArray = processLeaderHttp(urlString1);
	LeaderboardCategory(jsonArray,db,"Category");
}

public void processLeader(DBUserInfo db,String urlString){
//	String urlString ="poker-leaderboards/"+currentYear+"/Any%20Game/$101-$300"; 
	JSONArray jsonArray = processLeaderHttp(urlString);
	LeaderboardCategory(jsonArray,db,"Leader");
}

public String processURLString(ArrayList<String> leaderdefault) {
	String urlString=null;
//	urlString ="poker-leaderboards/"+leaderdefault.get(0)+"/"+leaderdefault.get(1).replace(" ", "%20")+"/"+leaderdefault.get(2).replace(" ","%20");
	urlString ="poker-leaderboards/"+leaderdefault.get(0)+"/"+leaderdefault.get(1)+"/"+leaderdefault.get(2);

	// TODO Auto-generated method stub
	return urlString;
}


public void processLeaderRank(DBUserInfo db){
	
}

public Leaderboard(ProcessData pd) {
	super();
	this.pd = pd;
}

public Leaderboard(){
	pd = new ProcessData();
	jsonUtils = new JsonUtils();
}

public Leaderboard(String _userid, String _passwd){
 this._userid = _userid;
 this._passid = _passwd;
}

public void LeaderboardCategory(JSONArray jsonArray, DBUserInfo db,String type){
	try{
	for(int i=0;i <jsonArray.length();i++){
    	JSONObject jsonYear = jsonArray.getJSONObject(i);
//    	leaderCategory.setYear(pd.processNodeValue(jsonYear, "@year"));
    	if (jsonYear.has("Leaderboards")){
    		JSONArray jsonLeader = jsonUtils.getJSONArray(jsonYear,"Leaderboards");
    		ProcessCategory(jsonLeader,pd,db,type);
    	}
    	}
	}catch(Exception e){
		   System.out.println("Error executing the Request[Leaderboards]"+e.toString());
	}
}

public JSONArray processLeaderHttp(String urlString){
	HttpCreater hc = new HttpCreater();
	JSONArray jsonArray=null;
    JSONObject jsonObject=null;
    //	http://www.sharkscope.com/api/someapp/poker-leaderboards/2010
	
    String formUrl = hc.formUrl(urlString);	
    String authResponse = hc.HttpProcessUserAccess(formUrl,_userid,_passid,"poker-leaderboards.txt",_assetManager);


    System.out.println("AuthResponse:"+authResponse);
   try{
    pd = new ProcessData(authResponse);
    jsonObject = pd.processRootNode("Response",authResponse);
    jsonObject = pd.processNode(jsonObject,"LeaderboardResponse");
    jsonObject = pd.processNode(jsonObject, "Leaderboards");
    jsonArray = jsonUtils.getJSONArray(jsonObject,"Leaderboards");
    
    
   }catch (Exception e){
	   System.out.println("Error executing the Request[process Leader Http Leaderboards]"+e.toString());
   }
   finally {

   }
	return jsonArray;
}

public void ProcessCategory(JSONArray jsonYear,ProcessData pd,DBUserInfo db,String type){
	JSONArray jsonLeaderRank = null;
	for(int i=0;i <jsonYear.length();i++){
		try {
			JSONObject jsonLeaderYear = jsonYear.getJSONObject(i);
			if (jsonLeaderYear.has("Leaderboards")){
			JSONArray jsonLeader= jsonUtils.getJSONArray(jsonLeaderYear,"Leaderboards");
		    System.out.println("Length of jsonleader"+jsonLeader.length());
			for (int j=0;j <jsonLeader.length();j++){

				JSONObject jsonLeaderYearCategory = jsonLeader.getJSONObject(j);
				 if (type.equals("Leader")){
					 	if (jsonLeaderYearCategory.has("Leaderboard")){
					 		jsonLeaderRank = jsonUtils.getJSONArray(jsonLeaderYearCategory, "Leaderboard");
					 		//hack to fix the Leaderboard Json object issue
					 		if(jsonLeaderRank.equals(null)){
					 			InsertLeaderCategory(jsonLeaderYearCategory,db,type);
					 		}else{
					 		for(int k=0;k<jsonLeaderRank.length();k++){
					 			JSONObject jsonLeaderRankObject = jsonLeaderRank.getJSONObject(k);
					 			InsertLeaderCategory(jsonLeaderRankObject,db,type);
					 		}
					 		}
					 			
					 	}
				    }else{
				    		InsertLeaderCategory(jsonLeaderYearCategory,db,type);
				    }
			}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 System.out.println("Error executing the Request[Process Category Leaderboards]"+e.toString());
		}
		
	}
}

public void InsertLeaderCategory(JSONObject jsonLeaderYearCategory,DBUserInfo db,String type){
	String year, subcategory, subCategoryDisplayOrder, categoryDisplayOrder, category = null;
	
	year=pd.processNodeValue(jsonLeaderYearCategory,"@year");
	subcategory=pd.processNodeValue(jsonLeaderYearCategory,"@subcategory");
	category=pd.processNodeValue(jsonLeaderYearCategory,"@category");
	
	if ( type.equals("Category")){
		subCategoryDisplayOrder=pd.processNodeValue(jsonLeaderYearCategory,"@subCategoryDisplayOrder");
		categoryDisplayOrder=pd.processNodeValue(jsonLeaderYearCategory,"@categoryDisplayOrder");
				
	db.insertLeaderCategory(year, subcategory, subCategoryDisplayOrder, categoryDisplayOrder, category);
	}else{
		subCategoryDisplayOrder=pd.processNodeValue(jsonLeaderYearCategory,"@rankingStatisticTitle");
		categoryDisplayOrder=pd.processNodeValue(jsonLeaderYearCategory,"@currency");
		if (jsonLeaderYearCategory.has("Rank")){
	 	JSONArray rankArray = jsonUtils.getJSONArray(jsonLeaderYearCategory, "Rank");
	 	String value,position,count,network,name = null;
	 	
	 	for(int i=0;i < rankArray.length();i++){
	 		try {
				JSONObject player = rankArray.getJSONObject(i);
				value = pd.processNodeValue(player, "@value");
				position = pd.processNodeValue(player,"@position");
				count = pd.processNodeValue(player, "@count");
				JSONObject playerinfo = pd.processNode(player, "Player");
				network = pd.processNodeValue(playerinfo, "@network");
				name = pd.processNodeValue(playerinfo, "@name");
				db.insertLeader(year, subcategory, subCategoryDisplayOrder, categoryDisplayOrder, category,value,position,count,network,name);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("[Leaderboard]lenght of array is:"+rankArray.length());
				e.printStackTrace();
			}
	 	}
		}	
	 	
	}

}


public ArrayList<String> dbleaderdefaultCategory(DBUserInfo db){
	  Cursor cursor=null;
	  ArrayList<String> defaultCategory = new ArrayList<String>();
	  String query = "select year, category, subcategory from PokerLeaderCategoryDefault";
	  cursor = db.processSingleRowQuery(query,cursor);
	  if(cursor.moveToFirst())
	   {
	       do
	       {
	    	   defaultCategory.add(cursor.getString(0));
	    	   defaultCategory.add(cursor.getString(1));
	    	   defaultCategory.add(cursor.getString(2));
	    	   
	          
	       }while(cursor.moveToNext());
	       if(cursor != null && !cursor.isClosed())
	          cursor.close();
	   }
	  return defaultCategory;
}


}