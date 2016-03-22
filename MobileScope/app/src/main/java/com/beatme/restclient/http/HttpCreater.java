package com.beatme.restclient.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.app.Activity;
import android.content.res.AssetManager;


/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: Nov 5, 2011
 * Time: 2:30:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpCreater{
    private String urlName="http://api.sharkscope.com/api";
    private String urlName1="http://www.sharkscope.com/poker-statistics";
    private String appName="mobilescope";
    private String licKey="876sqa7245cv95";
    public  String resourcePath;
    private boolean NOWEB=false; // change this setting to true to access internet or use the side

    public HttpCreater(){
          this.NOWEB=true;
    }

//    private void seturlName(){
//        this.urlName="http://www.sharkscope.com/api";
//    }
//    private String geturlName(){
//        return this.urlName;
//    }
//
//    private void setappName(){
//        this.appName="mobilescope";
//    }
//
//    private String getappName(){
//        return this.appName;
//    }
//
//
//    public void setlicKey(){
//        this.licKey=" 876sqa7245cv95";
//    }
    public String getlicKey(){
        return this.licKey;
    }

    public void setresourcePath(String resourcePath){
          this.resourcePath=resourcePath;
      }

      public String getresourcePath(){
          return this.resourcePath;
      }


    public String formUrl(String resourcePath){
        String formUrl=this.urlName+"/"+this.appName+"/"+resourcePath;
        System.out.println("URL Name:"+formUrl);
        return urlFormat(formUrl);
    }
    
    public String urlFormat(String urlStr){
    	URL url = null;
		try {
			url = new URL(urlStr);
		
    	URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
    	url = uri.toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return url.toString();
    }
    
    public String formUrl1(String resourcePath){
    	  String formUrl=this.urlName1+"/"+resourcePath;
          System.out.println("URL Name:"+formUrl);
          return formUrl;
    }
    
    
//    public String ConvertXMLtoJSON(String xmlFile){
//    	
//    	 XmlSerializer xmlSerializer = new XmlSerializer(); 
//         JSON json = ((Reader) xmlSerializer).read( xmlFile );
//         return json.toString(2);
//    }

    public String HttpProcessUserAccess(String formUrl, String signid, String pid,String filename,AssetManager assetManager){
         if (NOWEB){
           String authResponse=null;
                  String accept="application/json";
//                  String accept="application/xml";
//                  try {
//					formUrl = URLEncoder.encode(formUrl, "UTF-8");
//				} catch (UnsupportedEncodingException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
                  RestClient rc = new RestClient(formUrl);
                  rc.AddHeader("Accept",accept);
                  rc.AddHeader("Username", signid);
                  rc.AddHeader("Password", pid);
                  try{
                      rc.Execute(RequestMethod.GET);
                      authResponse = rc.getResponse();
                  }catch (Exception e){
                      System.out.println("Error executing the Request:"+formUrl);
                      e.printStackTrace();
                  }
           return authResponse;
         } else{
        	 return noWeb(assetManager,filename);
             }

         }

	 public String httpProcessNewUser(String formUrl, String signid, String pid,String country,String emailOption,String filename,AssetManager assetManager){
         if (NOWEB){
           String authResponse=null;
                  String accept="application/json";
               RestClient rc = new RestClient(formUrl);
                  rc.AddHeader("Accept",accept);
                  rc.AddHeader("Username", signid);
                  rc.AddHeader("Password", pid);
                  rc.AddHeader("Country", country);
                  rc.AddHeader("emailOption", emailOption);
                  
                  
                  try{
                      rc.Execute(RequestMethod.GET);
                      authResponse = rc.getResponse();
                  }catch (Exception e){
                      System.out.println("Error executing the Request:"+formUrl);
                      e.printStackTrace();
                  }
           return authResponse;
         }else{
        	 return noWeb(assetManager,filename);
         }
         }

	 private String noWeb(AssetManager assetManager, String filename){
		 
             String response=null;
              System.out.println("**********************Not going through web************************");
             try{
//                 System.out.println("Jude"+getresourcePath().toString());
                 if (assetManager.equals(null)){

                 }
                 InputStream openFile=assetManager.open("test/"+filename);
//                 InputStream openFile = this.getResources().openRawResource(R.raw.login);
                 System.out.println("Jude");
                 response = readFileFromAssert(openFile);
                 openFile.close();
             } catch(Exception e) {
                 System.out.println("Noweb ...."+filename);
                 e.printStackTrace();
             }  finally {

//                 assetManager.close();
             }
           return response;
         
	 }

    private  String readFileFromAssert(InputStream is)
    {
        System.out.println("Noweb ....1");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}