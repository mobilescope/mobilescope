package com.mobilescope.player;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.protocol.HTTP;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import android.content.res.AssetManager;
import android.widget.Toast;

import com.beatme.process.data.ProcessData;
import com.beatme.restclient.http.HttpCreater;
import com.mobilescope.database.auth.DBUserInfo;

public class SearchPlayer {
	Map<String, ArrayList<String>> _hashMap = new HashMap<String, ArrayList<String>>();
	private String limit="25";
	String playerName;
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Set<String> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(Set<String> playersList) {
		this.playersList = playersList;
	}

	public void set_hashMap(Map<String, ArrayList<String>> _hashMap) {
		this._hashMap = _hashMap;
	}

	Set<String> playersList = null;
	
	public Set<String> getPlayerList(String name,String signid,String pid,String type){
		ProcessData pd;
		JSONArray AutoComplete = null;
		
		
		String authResponse = getHTTPResponse(name,signid,pid);
		System.out.println("AuthResponse:" + authResponse);
		pd = new ProcessData(authResponse);
		try {
			
			AutoComplete = pd.processAutoComplete(authResponse, "Response");
			System.out.println("AuthReponse array size"+AutoComplete.length());
			for (int i = 0; i < AutoComplete.length(); i++) {
				JSONObject autoComplete = AutoComplete.getJSONObject(i);
				String playerName = autoComplete.getString("@name");
				String playerNetwork = autoComplete.getString("@network");
				if (playerName != null) {
//					_SSMobileSQLite.insertPlayer(playerName, playerNetwork,	quickTextPrefix);
					loadPlayerHash(playerName,playerNetwork);
				}
			}

		}
		catch (Exception e) {
			System.out.println("Error executing the Request[searchPlayer.getPlayerList]");
//			Toast.makeText(
//					this,
//					"Unable to find player details in any network. Check player name",
//					Toast.LENGTH_LONG);
//			mainButton.setEnabled(false);
		} finally {
			if(type.equals("search")){
			playersList = getPlayerList();
			}else{
			playersList=getNetworkList(name);	
			}
		}
		
		return playersList;
	}
	
	public Map<String, ArrayList<String>> get_hashMap() {
		return _hashMap;
	}

	public void set_hashMap(HashMap<String, ArrayList<String>> _hashMap) {
		this._hashMap = _hashMap;
	}

	public void loadPlayerHash(String playerName,String playerNetwork){
		Object checkPlayer = _hashMap.get(playerName);
		if(checkPlayer == null){
			addPlayerHash(playerName,playerNetwork);
		}else{
			updatePlayerHash(playerName,playerNetwork);
		}
		
	}
	
	private void updatePlayerHash(String playerName, String playerNetwork) {
		// TODO Auto-generated method stub
		ArrayList<String> _networkList = _hashMap.get(playerName); 
				
		_networkList.add(playerNetwork);
		_hashMap.put(playerName, _networkList);
	}

	private void addPlayerHash(String playerName, String playerNetwork) {
		// TODO Auto-generated method stub
		ArrayList<String> _networkList = new ArrayList<String>();
		_networkList.add(playerNetwork);
		_hashMap.put(playerName, _networkList);
		
	}
	
	private Set<String> getPlayerList(){
		try{
		return _hashMap.keySet();
		}catch(Exception e){
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private Set<String> getNetworkList(String name){
		try{
		Set<String> networkList = new HashSet<String>( _hashMap.get(name));
		return networkList; 
		}catch(Exception e){
			return null;
		}
	}
	
	private String getHTTPResponse(String name,String signid,String pid){
		DBUserInfo db = new DBUserInfo();
		HttpCreater hc = new HttpCreater();
		String formUrl = null;
		AssetManager assetmanager=null;
		db.openDB();
		System.out.println("Name"+name);
		try {
			formUrl = hc.formUrl("networks/*/players/"
					+ URLEncoder.encode(name, HTTP.UTF_8)
					+ "/suggestions?limit=" + limit);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        assetmanager=db.getAssetManager();
        db.closeDB();
		return hc.HttpProcessUserAccess(formUrl, signid, pid,
				"qsrusername.txt", assetmanager);
		// String authResponse = HttpProcess(formUrl,signid,pid);
	}
}
