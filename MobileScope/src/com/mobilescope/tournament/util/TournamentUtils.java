package com.mobilescope.tournament.util;

import java.util.ArrayList;

public class TournamentUtils {
	public ArrayList<String> _entrantlist = new ArrayList<String>();
	public ArrayList<String> _stacklist = new ArrayList<String>();
	
	
	public final ArrayList<String> stackList(){
		_stacklist.add("0");
		_stacklist.add("0.01");
		_stacklist.add("0.1");
		_stacklist.add("1");
		_stacklist.add("2");
		_stacklist.add("2.5");
		_stacklist.add("3");
		_stacklist.add("4");
		_stacklist.add("5");
		_stacklist.add("6");
		_stacklist.add("7");
		_stacklist.add("8");
		_stacklist.add("9");
		_stacklist.add("10");
		_stacklist.add("11");
		_stacklist.add("12");
		_stacklist.add("13");
		_stacklist.add("14");
		_stacklist.add("15");
		_stacklist.add("16");
		_stacklist.add("17");
		_stacklist.add("18");
		_stacklist.add("19");
		_stacklist.add("20");
		_stacklist.add("21");
		_stacklist.add("22");
		_stacklist.add("23");
		_stacklist.add("24");
		_stacklist.add("25");
		_stacklist.add("25");
		_stacklist.add("26");
		_stacklist.add("27");
		_stacklist.add("28");
		_stacklist.add("30");
		_stacklist.add("32");
		_stacklist.add("33");
		_stacklist.add("35");
		_stacklist.add("36");
		_stacklist.add("40");
		_stacklist.add("45");
		_stacklist.add("50");
		_stacklist.add("54");
		_stacklist.add("60");
		_stacklist.add("63");
		_stacklist.add("70");
		_stacklist.add("80");
		_stacklist.add("90");
		_stacklist.add("100");
		_stacklist.add("109");
		_stacklist.add("110");
		_stacklist.add("200");
		_stacklist.add("220");
		_stacklist.add("300");
		_stacklist.add("330");
		_stacklist.add("500");
		_stacklist.add("550");
		_stacklist.add("1000");
		_stacklist.add("1100");
		_stacklist.add("2000");
		_stacklist.add("2200");
		_stacklist.add("5000");
		_stacklist.add("10000");
		_stacklist.add("100000");
		_stacklist.add("*");
		return _stacklist;
	}
    public final ArrayList<String> etrantList(){
    	_entrantlist.add("2");
    	_entrantlist.add("3");
    	_entrantlist.add("4");
    	_entrantlist.add("5");
    	_entrantlist.add("6");
    	_entrantlist.add("7");
    	_entrantlist.add("8");
    	_entrantlist.add("9");
    	_entrantlist.add("10");
    	_entrantlist.add("12");
    	_entrantlist.add("15");
    	_entrantlist.add("16");
    	_entrantlist.add("18");
    	_entrantlist.add("20");
    	_entrantlist.add("27");
    	_entrantlist.add("30");
    	_entrantlist.add("31");
    	_entrantlist.add("32");
    	_entrantlist.add("35");
    	_entrantlist.add("36");
    	_entrantlist.add("40");
    	_entrantlist.add("45");
    	_entrantlist.add("50");
    	_entrantlist.add("54");
    	_entrantlist.add("60");
    	_entrantlist.add("63");
    	_entrantlist.add("90");
    	_entrantlist.add("180");
    	_entrantlist.add("216");
    	_entrantlist.add("240");
    	_entrantlist.add("333");
    	_entrantlist.add("360");
    	_entrantlist.add("999");
    	_entrantlist.add("*");
    	return _entrantlist;
    }

	public TournamentUtils() {
		super();
		this._entrantlist =  etrantList();
		this._stacklist = stackList();
	}
    
    
}
