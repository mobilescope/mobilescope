package com.beatme.process.data;

import com.beatme.com.beatme.datatype.UserInfo;
import com.mobilescope.util.JsonUtils;

import org.apache.commons.logging.Log;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: Nov 3, 2011
 * Time: 12:21:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessData {
       List beatData;
       String httpResponse;
       UserInfo ui;
       JsonUtils jsonUtils;

       private void sethttpResponse(String httpResponse){
           this.httpResponse=httpResponse;

       }
       private String gethttpResponse(){
           return this.httpResponse;
       }
       public ProcessData(){
           beatData = new ArrayList();
           ui=new UserInfo();
           jsonUtils = new JsonUtils();
       }

      public ProcessData(String httpResponse){
            this.httpResponse=httpResponse;
      }
//       private void parseDocument(){
//           SAXParserFactory  beatParserFactory = SAXParserFactory.newInstance() ;
//           try{
//               SAXParser beatParser = beatParserFactory.newSAXParser();
//               beatParser.parse(new InputSource(new StringReader(this.httpResponse)));
//               beatParser.parse(new InputSource(new StringReader(this.httpResponse)),this);
//           }catch(SAXException se){
//                se.printStackTrace();
//           }catch(ParserConfigurationException pce){
//               pce.printStackTrace();
//           }catch(Exception e){
//               e.printStackTrace();
//           }

    public static void ProcessJSON(String httpResponse){
        String metaDataHash=null;
        JSONArray subscription=null;
        try{
          JSONObject jsonArray = new JSONObject(httpResponse);
          metaDataHash=jsonArray.getJSONObject("AuthResponse").getJSONObject("Response").getString("@metadataHash");
          subscription =jsonArray.getJSONObject("AuthResponse").getJSONObject("Response").getJSONObject("UserInfo").getJSONObject("Subscriptions").getJSONArray("Subscription");
          System.out.println("Length of the array"+subscription.getJSONObject(1).getString("@txnID"));
        } catch (JSONException jsone){
            System.out.println(" JSONException:"+jsone.toString());
        }
        System.out.println("Metadatahash vaule:"+ metaDataHash);

        
    }

    public JSONObject processRootNode(String rootNode, String httpResponse){
        try{
                  JSONObject jsonArray = new JSONObject(httpResponse);
                  return jsonArray.getJSONObject(rootNode);
                } catch (JSONException jsone){
                    System.out.println(" [processRootNode] JSONException:"+jsone.toString());
                }catch (NullPointerException ex){
                    System.out.println("No Node");
                }
              return null;
      }

    public JSONObject processErrorNode(String rootNode, String httpResponse){
            try{
                      JSONObject jsonArray = new JSONObject(httpResponse);
                      return jsonArray.getJSONObject(rootNode).getJSONObject("ErrorResponse").getJSONObject("Error");
                    } catch (JSONException jsone){
                        System.out.println(" [processRootNode] JSONException:"+jsone.toString());
                    }catch (NullPointerException ex){
                        System.out.println("No Node");
                    }


                  return null;
          }


    public UserInfo processGetMetaHash(String httpResponse, String node){
        AtomicReference<JSONObject> dataObject = new AtomicReference<JSONObject>();
        try{
            dataObject.set(processRootNode("Response", httpResponse));
            if (dataObject.get().has("ErrorResponse")){
                ui.setmetadatahash(dataObject.get().getString(node));
               dataObject.set(processErrorNode("Response", httpResponse));
                 ui.setError(dataObject.get().getString("$"));

            } else{
                System.out.println(dataObject.get().getString("@metadataHash"));
              ui.metadatahash= dataObject.get().getString("@metadataHash");
              ui.userid= dataObject.get().getJSONObject("UserInfo").getString("Username");
              ui.Error="No Error";
            }

        }catch (JSONException jsone){
           System.out.println(" [processGetMetaHash]JSONException:"+jsone.toString());
        }catch (NullPointerException ex){
            System.out.println("[processGetMetaHash]:"+ex.toString());
        }
        return ui;
    }

    public JSONArray processAutoComplete(String httpResponse, String node){
        JSONObject dataObject = null;
        JSONArray AutoComplete=null;
        Object object;
                try{
                    dataObject = processRootNode("Response",httpResponse);
                    object = dataObject.getJSONObject("SearchSuggestionsResponse").getJSONObject("PlayerSuggestions").get("Player");
                    if(object instanceof JSONArray){
                    	AutoComplete = (JSONArray) object;
                    }
                    if(object instanceof JSONObject){
                    	AutoComplete = new JSONArray();
                    	AutoComplete.put((JSONObject)object);
                    }
                }catch (JSONException jsone){
                      System.out.println(" [processAutoComplete]JSONException:"+jsone.toString());
//                    Log.i("DEBUG"," [processAutoComplete]JSONException:"+jsone.toString());
                }catch (NullPointerException ex){
                    System.out.println("[processAutoComplete]:"+ex.toString());
                }
        return AutoComplete;

    }

    public JSONObject processAutoCompleteObject(String httpResponse, String node){
        JSONObject dataObject = null;
        JSONObject AutoComplete=null;
                try{
                    dataObject = processRootNode("Response",httpResponse);
                    AutoComplete = dataObject.getJSONObject("SearchSuggestionsResponse").getJSONObject("PlayerSuggestions").getJSONObject("Player");
                }catch (JSONException jsone){
                      System.out.println(" [processAutoComplete]JSONException:"+jsone.toString());
//                    Log.i("DEBUG"," [processAutoComplete]JSONException:"+jsone.toString());
                }catch (NullPointerException ex){
                    System.out.println("[processAutoComplete]:"+ex.toString());
                }
        return AutoComplete;

    }

    public JSONObject processNode(JSONObject jobject, String node){
        AtomicReference<JSONObject> dataObject = new AtomicReference<JSONObject>();
        try{
            dataObject.set(jobject);
            if(dataObject.get().has(node)){
                     return jobject.getJSONObject(node);
            }else{
                return null;
            }

        }catch(JSONException json){
            System.out.println("[processNode] JSONExcepiton"+json.toString() +"******"+node);
        }catch (NullPointerException ex){
            System.out.println("[processNode]:"+ex.toString());
        }

        return null;
    }

    public String processNodeValue(JSONObject jsonObject,String node){
        AtomicReference<JSONObject> dataObject = new AtomicReference<JSONObject>();

     try{
           dataObject.set(jsonObject);
        if(dataObject.get().has(node)){
            return jsonObject.getString(node);
        }else{
            return null;
        }

        }catch(JSONException json){
            System.out.println("[processNodeValue] JSONExcepiton"+json.toString() +"******"+node);
        }catch (NullPointerException ex){
            System.out.println("[processNodeValue]:"+ex.toString());
        }

        return null;
    }


    public JSONArray processNodeArray(JSONObject jsonObject, String node){
//    	obsulet
//        JSONArray jsonArray=null;
//        try{
//        jsonArray= jsonObject.getJSONArray(node);
//        }catch(JSONException json){
//            System.out.println("[processNodeArray] JSONExcepiton"+json.toString() +"******"+node);
//        }catch (NullPointerException ex){
//            System.out.println("[processNodeArray]:"+ex.toString());
//        }
    	

        return jsonUtils.getJSONArray(jsonObject, node);
    }

    public static String convertStreamtoString(InputStream fileStream) throws IOException {
        if (fileStream != null) {
                Writer writer = new StringWriter();
                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(
                    new InputStreamReader(fileStream, "UTF-8"));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                            writer.write(buffer, 0, n);
                        }
                    } finally {
                            fileStream.close();
                    }
//                   System.out.println(writer.toString());
                   return writer.toString();
       }else {
             System.out.println("Error: InputStream is null");
             return "";
       }
        
    }
	public JSONArray processActiveTour(String authResponse, String string) {
		// TODO Auto-generated method stub
		 JSONObject dataObject = null;
	        JSONArray AutoComplete=null;
	        Object object;
	                try{
	                    dataObject = processRootNode("Response",httpResponse);
	                    object = dataObject.getJSONObject("RegisteringTournamentsResponse").getJSONObject("RegisteringTournaments").get("RegisteringTournament");
	                    if(object instanceof JSONArray){
	                    	AutoComplete = (JSONArray) object;
	                    }
	                    if(object instanceof JSONObject){
	                    	AutoComplete = new JSONArray();
	                    	AutoComplete.put((JSONObject)object);
	                    }
	                }catch (JSONException jsone){
	                      System.out.println(" [processActiveTour]JSONException:"+jsone.toString());
//	                    Log.i("DEBUG"," [processAutoComplete]JSONException:"+jsone.toString());
	                }catch (NullPointerException ex){
	                    System.out.println("[processActiveTour]:"+ex.toString());
	                }
	        return AutoComplete;
	}


//    public static void main(String [] args){
//        ProcessData pd = new ProcessData();
//
//        try{
//            InputStream  fileStream = new FileInputStream("C:\\restdata.txt");
//            String httpResponse = convertStreamtoString(fileStream);
//            ProcessJSON(httpResponse);
//        }catch(Exception e){
//
//        }
//
//    }

}

