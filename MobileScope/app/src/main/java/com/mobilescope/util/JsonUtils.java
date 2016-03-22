package com.mobilescope.util;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mobilescope.Exception.MobileScopeException;

public class JsonUtils implements JsonUtilsInterface {

	public JSONArray getJSONArray(org.codehaus.jettison.json.JSONObject jsonObject, String fieldname) 
	{
		Object object = null;
		JSONArray jsonArray = null;
		try {
			object = jsonObject.get(fieldname);
		} catch (org.codehaus.jettison.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(object instanceof JSONArray){
			jsonArray = (JSONArray)object;
			return jsonArray;
		}
		if (object instanceof JSONObject){
			jsonArray = new JSONArray();
			jsonArray.put((JSONObject)object);
			return jsonArray;
		}
		return null;
	}

	
}
