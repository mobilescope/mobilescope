package com.mobilescope.tournament.backend;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.protocol.HTTP;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import android.content.res.AssetManager;

import com.beatme.process.data.ProcessData;
import com.beatme.restclient.http.HttpCreater;
import com.mobilescope.database.auth.DBUserInfo;

public class TournamentMain {
	TournamentSearch tourSearch;
	private String limit="25"; 
	
	
	
	public TournamentMain(TournamentSearch tourSearch) {
		super();
		this.tourSearch = tourSearch;
	}



	public TournamentMain() {
		// TODO Auto-generated constructor stub
	}



	public void tournamentSearchInputs(){
//		Entrants:4~*;StakePlusRake:USD3~*;Networks:*
		tourSearch = new TournamentSearch();
		tourSearch._games="All Games";
		tourSearch._networks="*";
		tourSearch._incnetworks="-";
		tourSearch._excnetworks="-";
		tourSearch._entrants="4~*";
		tourSearch._stakerake="USD3~*";
				
	}
	
	private String formTourSearchString(){
		String toursearchstr = "Entrants:"+tourSearch._entrants+";StakePlusRake:"+tourSearch._stakerake+";Networks:"+tourSearch._networks;
		return toursearchstr;
		
	}
	
	
	
	public ArrayList<TournamentResult> processTourSearch(String authResponse){
		ProcessData pd;
		JSONArray AutoComplete = null;
		JSONObject regjsonobj ;
		ArrayList<TournamentResult> listtourresult = new ArrayList();
		
		
		System.out.println("AuthResponse:" + authResponse);
		pd = new ProcessData(authResponse);
		try {
			//Capture Error message
			
			AutoComplete = pd.processActiveTour(authResponse, "Response");
		
			System.out.println("AuthReponse array size"+AutoComplete.length());
			for (int i = 1; i < AutoComplete.length(); i++) {
//				@lastUpdateTime	:	1362731684
//				
//				@currentEntrants	:	4
//						
//				@totalEntrants	:	6
//						
//				@structure	:	NL
//						
//				@state	:	Registering
//						
//				@stake	:	10.0
//						
//				@rake	:	0.5
//						
//				@network	:	Merge
//						
//				@name	:	Swift Room - Super Turbo
//						
//				@id	:	67450066
//						
//				@game	:	H
//						
//				@flags	:	6MX,ST
//						
//				@currency	:	USD

				JSONObject autoComplete = AutoComplete.getJSONObject(i);
				String tourid = autoComplete.getString("@id");
				String tournetwork = autoComplete.getString("@network");		
				String tourdate = pd.processNodeValue(autoComplete, "@scheduledStartDate");
				String tourstake = autoComplete.getString("@stake");
				String tourrake = autoComplete.getString("@rake");
				Double value = Double.valueOf(tourstake)+Double.valueOf(tourrake);
			    TournamentResult tourresult = new TournamentResult(tourid,tourdate,tournetwork,String.valueOf(value));
			    listtourresult.add(tourresult);
			}

		}
		catch (Exception e) {
			System.out.println("Error executing the Request[TournamentMain]"+e.getMessage());

		} finally {
		}
		
		
		return listtourresult;
		
	}
	
	
}
