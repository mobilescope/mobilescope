package com.mobilescope.util;

import java.util.HashMap;
import java.util.Map;

public enum UserCurrEnum {
	USD(0,"USD","$"),
	EUR(1,"EUR","€"),
	PON(2,"PON","£");

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	private UserCurrEnum(int type, String currtype, String currency) {
		this.type = type;
		this.currtype = currtype;
		this.currency = currency;
	}
	int type;
	String currtype;
	String currency;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCurrtype() {
		return currtype;
	}
	public void setCurrtype(String currtype) {
		this.currtype = currtype;
	}
	
	private static Map<Integer, UserCurrEnum> typeToUserCurrMapping;
	public static UserCurrEnum getUserCurrEnum(int i) {
        if (typeToUserCurrMapping == null) {
            initMapping();
        }
        UserCurrEnum result = null;
        for (UserCurrEnum s : values()) {
            result = typeToUserCurrMapping.get(i);
        }
        return result;
    }
 
    private static void initMapping() {
    	typeToUserCurrMapping = new HashMap<Integer, UserCurrEnum>();
        for (UserCurrEnum s : values()) {
        	typeToUserCurrMapping.put(s.type, s);
        }
    }
	
}
