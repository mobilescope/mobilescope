package com.beatme;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: Nov 6, 2011
 * Time: 12:44:49 AM
 * To change this template use File | Settings | File Templates.
 */

import android.util.Log;

import com.beatme.com.beatme.datatype.UserInfo;
import com.beatme.restclient.http.*;
import com.beatme.frontend.api.mobilescope;
import com.beatme.process.data.ProcessData;
import com.mobilescope.database.auth.DBUserInfo;

public class TestMobileScope {
    String userid;
    String password;
    DBUserInfo _DBUserInfo;
   
   public TestMobileScope(){
	   
   }
   public TestMobileScope(String userid, String password,DBUserInfo _DBUserInfo){
	   this.userid=userid;
	   this.password=password;
	   this._DBUserInfo=_DBUserInfo;
   }
   
   public String LoginStatus(boolean DBEXIST){
       String ErrorStatus= userAuth(this.userid,this.password,this._DBUserInfo,DBEXIST);
       return ErrorStatus;
   }
	
    public String userAuth(String userid, String password,DBUserInfo _DBUserInfo, boolean DBEXIST){
//        String userid="judefdo@gmail.com";
    			String authResponse = null;
                String accept="application/json";
                mobilescope ms = new mobilescope();
                HttpCreater hc = new HttpCreater();
                ProcessData pc = new ProcessData();
                String encryPassword=null;
                if(DBEXIST){
                  encryPassword=password;
                }else{
                    encryPassword=ms.encryPassword(password);
                }
                System.out.println("Password string is:"+encryPassword);

                String formUrl= hc.formUrl("user");
                System.out.println("Form url to hit the rest server is "+formUrl);
//                RestClient rc = new RestClient(formUrl);
//                rc.AddHeader("Accept",accept);
//                rc.AddParam("Username",userid);
//                rc.AddParam("Password",encryPassword);
//                rc.AddHeader("Username",userid);
//                rc.AddHeader("Password",encryPassword);
//                try{
//                    rc.Execute(RequestMethod.GET);
//                }  catch (Exception e){
//                    System.out.println("Error executing the Request");
//                }
//               String authResponse = rc.getResponse();
            authResponse = hc.HttpProcessUserAccess(formUrl,userid,encryPassword,"login.txt",_DBUserInfo.getAssetManager());
           System.out.println("AuthResponse:"+authResponse);
           try{
           if(authResponse.equals(null)){
        	   return "No network connection";
           }
           }catch (NullPointerException e){
        	   return "No network connection";
           }
           UserInfo ui = pc.processGetMetaHash(authResponse,"@metadataHash");
//           System.out.println("MetadataHash" +ui.getmetadatahash());
//           System.out.println("Userid" +ui.getuserid());

           Log.i("DEBUG","Error String  is" +ui.getError());
           if (ui.getError().equals("No Error")){
        	   if(DBEXIST){
        		   _DBUserInfo.updateDataInfo("metadatahash","user_name",ui.userid,ui.metadatahash);
        	   }else{
        		   _DBUserInfo.insertPlayer(ui.userid, encryPassword, ui.getmetadatahash());
        	   }
           }


           return ui.getError();

    }

//    public static void main(String[] args){
////           userAuth();
//      }

}
