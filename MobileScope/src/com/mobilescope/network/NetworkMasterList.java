package com.mobilescope.network;

import java.util.ArrayList;
import java.util.List;

public class NetworkMasterList {
	String[] toplevel={"Live Events","Player Group","Other regions","Everleaf","Merge","Revolution"};
	String[] otherregions={"Spanish Sites","Swedish Sites","French Sites","Non US","Finnish Sites","Italian Site","Closed network"};
	String[] spanishsites={"888Poker.es","PartyPoker.es","PokerStars.es"};
	String[] swedishsites={"SvenskaSpel"};
	String[] frenchsites={"Barriere","Everest.fr","iPoker.fr","Ongame.fr","PartyPoker.fr","PokerStars.fr","Winamax.fr"
						};
	String[] non_us={
			"Everest",
		    "IGT",
		    "IPN",
		    "iPoker",
		    "MicroGaming",
		    "Ongame",
		    "Pacific",
		    "PartyPoker",
		    "PKR",
		    "PokerStars",
		    "SkyPoker"};
	String[] finnishsites={"Ray.fi"};
	String[] italiansites={" Gioco Online Italia",
		    "iPoker.it",
		    "MicroGame",
		    "Ongame.it",
		    "PartyPoker.it",
		    "PokerClub",
		    "PokerStars.it"};
	String[] closednetwork={"Betfair",
		    "Bodog",
		    "Cereus",
		    "CryptoLogic",
		    "FullTilt",
		    "FullTilt.fr",
		    "GiocoDigitale",
		    "LSP (New)"};
	List<NetworkNames> _masterList = new ArrayList();
	public NetworkMasterList() {
		super();
		generateList();
		// TODO Auto-generated constructor stub
		
	}
	
	public void generateList(){
		NetworkNames _topLevel = new NetworkNames("All Networks",toplevel);
		
		NetworkNames _otherregions = new NetworkNames("Other regions",otherregions);
		NetworkNames _spanishsites = new NetworkNames("Spanish Sites",spanishsites);
		NetworkNames _swedishsites = new NetworkNames("Swedish Sites",swedishsites);
		NetworkNames _frenchsites = new NetworkNames("French Sites",frenchsites);
		NetworkNames _non_us = new NetworkNames("Non US",non_us);
		NetworkNames _finnishsites = new NetworkNames("Finnish Sites",finnishsites);
		NetworkNames _italiansites = new NetworkNames("Italian Site",italiansites);
		NetworkNames _closednetwork = new NetworkNames("Closed network",closednetwork);
		_masterList.add(_topLevel);
		_masterList.add(_otherregions);
		_masterList.add(_spanishsites);
		_masterList.add(_swedishsites);
		_masterList.add(_frenchsites);
		_masterList.add(_non_us);
		_masterList.add(_finnishsites);
		_masterList.add(_italiansites);
		_masterList.add(_closednetwork);
		
	}

	public String[] getChildList(String name){
		String[] childList=null;
		for(NetworkNames networkList : _masterList){
			if(networkList._parent == name){
				return networkList.get_child();
			}
		}
		return childList;
	}
	
	
}
