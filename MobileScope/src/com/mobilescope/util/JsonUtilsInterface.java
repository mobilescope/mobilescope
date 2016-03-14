package com.mobilescope.util;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.mobilescope.Exception.MobileScopeException;

public interface JsonUtilsInterface {
	public JSONArray getJSONArray(JSONObject jsonObject, String fieldname) throws MobileScopeException;
}