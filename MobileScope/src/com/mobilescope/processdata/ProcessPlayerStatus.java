package com.mobilescope.processdata;

import android.R;
import android.app.Application;
import android.content.res.AssetManager;
import com.beatme.process.data.ProcessData;
import com.beatme.restclient.http.HttpCreater;
import com.mobilescope.util.JsonUtils;
import com.mobilescope.util.UserUtil;
import com.mobilescope.util.messageBox;
import com.mobilescope.database.auth.PlayerDetail;
import com.mobilescope.database.auth.RecentTournament;

import org.apache.http.protocol.HTTP;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: 11/28/11
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 * 
 * 
 */

import org.acra.*;
import org.acra.annotation.*;

import android.app.Application;


@ReportsCrashes(formKey = "dEZLV2Jta3BIdW5Kaks5MzlBa0lCRUE6MQ",
mode = ReportingInteractionMode.TOAST,
forceCloseDialogAfterToast = false // optional, default false
)

public class ProcessPlayerStatus extends Application{
    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	private String _userName;
    private String _networkName;
    private String _userid;
    private String _passid;
    private String _count;
    private AssetManager _assetManager;
    private ProcessData pd;
    private PlayerDetail playerd;
    
    
    public ProcessPlayerStatus(){

    }

     public ProcessPlayerStatus(String _userName, String _networkName,String _userid, String _passid,AssetManager _assetManager){
         this._userName=_userName;
         this._networkName=_networkName;
         this._userid = _userid;
         this._passid = _passid;
         this._assetManager = _assetManager;
         
     }

    public String processPlayer(){
    	
        HttpCreater hc = new HttpCreater();
//         ProcessData pd;
        
       
         String formUrl=null;
		formUrl = hc.formUrl("networks/"+_networkName+"/players/"+_userName+"?&Currency=USD");
		
         String authResponse = hc.HttpProcessUserAccess(formUrl,_userid,_passid,"quicksearchdataprocess.txt",_assetManager);
//                String authResponse = HttpProcess(formUrl,signid,pid);
         return authResponse;
 
    }
    
    public void processPlayerData(String authResponse){
    	playerd = new PlayerDetail();
    	 JSONArray jsonArray=null;
         JSONObject jsonObject=null;
         JSONObject userObject=null;

        System.out.println("AuthResponse:"+authResponse);
        try{
         pd = new ProcessData(authResponse);
         jsonObject = pd.processRootNode("Response",authResponse);
         userObject = pd.processRootNode("Response",authResponse);
         jsonObject = pd.processNode(jsonObject,"PlayerResponse");
         userObject = pd.processNode(userObject, "UserInfo");
         playerd.set_RemainingSearches(getJSONValue(userObject,"RemainingSearches"));
         
         set_count(getJSONValue(userObject,"RemainingSearches"));
         // processNodeArray is not used
//         jsonArray = pd.processNodeArray(jsonObject,"PlayerView");
//         for (int i=0;i<jsonArray.length();i++){
//             JSONObject playerArray = jsonArray.getJSONObject(i);
//             playerHistory(playerArray, playerd);
//             playerGraph(playerArray,playerd);
//             playerTournament(playerArray,playerd);
//         }
         
         jsonObject = pd.processNode(jsonObject, "PlayerView");
         if(! isPlayerOptIn(jsonObject,playerd, _networkName) ){
         	playerHistory(jsonObject, playerd);
         	playerGraph(jsonObject,playerd);
         	playerTournament(jsonObject,playerd);
         }
         
         
     }  catch (Exception e){
         System.out.println("Error executing the Request[processPlayer]"+e.toString());
     }
   finally {

   }
    }

    public void playerTournament(JSONObject jsonObject,PlayerDetail playerDetail){
    	JsonUtils jsonUtils = new JsonUtils();
        JSONObject playerObject=null;
          JSONArray playerArray=null;


             try{
                playerObject = jsonObject.getJSONObject("Player");
                playerObject = playerObject.getJSONObject("RecentTournaments");
                playerArray = jsonUtils.getJSONArray(playerObject,"Tournament");
                playerDetail.set_recentTournaments(playerArray.length());
                for (int i=0;i<playerArray.length();i++){
                    JSONObject playerT = playerArray.getJSONObject(i);
                    RecentTournament tr = new RecentTournament();
                    tr.set_totalEntrants(playerT.getString("@totalEntrants"));
//                    tr.set_prizePool(playerT.getString("@prizePool"));
                    tr.set_prizePool(pd.processNodeValue(playerT,"@prizePool"));
                    tr.set_structure(playerT.getString("@structure"));
                    tr.set_state(playerT.getString("@state"));
                    tr.set_stake(playerT.getString("@stake"));
                    tr.set_rake(playerT.getString("@rake"));
                    tr.set_id(playerT.getString("@id"));
                    System.out.println("Tournament ID:"+playerT.getString("@id"));
                    tr.set_game(playerT.getString("@game"));
                    tr.set_flags(pd.processNodeValue(playerT, "@flags"));
                    tr.set_currency(playerT.getString("@currency"));
                    playerT = playerT.getJSONObject("TournamentEntry");
                    tr.set_prize(pd.processNodeValue(playerT, "@prize"));
                    tr.set_position(pd.processNodeValue(playerT,"@position") == null?"":pd.processNodeValue(playerT,"@position"));
                    tr.set_network(pd.processNodeValue(playerT,"@network"));
                   playerDetail.add_recentTournament(tr);
                }
             }catch (Exception e){
                System.out.println("Error executing the Request[playerTournament]"+e.toString());
             }

    }

    public void playerGraph(JSONObject jsonObject, PlayerDetail playerDetail){
    	UserUtil userUtil = new UserUtil();
    	JsonUtils jsonUtils = new JsonUtils();
               JSONObject playerObject=null;
               JSONArray playerArray=null;
               JSONArray playerArray1=null;
               Number[] _lCumulativeProfitSeries;
               Number[] _lCumulativeProfitGrossSeries;
               Number[] _lxValue;

            try{
               playerObject = jsonObject.getJSONObject("Player");
                playerObject = playerObject.getJSONObject("Statistics");
               playerArray =  jsonUtils.getJSONArray(playerObject,"StatisticalDataSet");
//               playerObject = playerArray.getJSONObject(0);  // Getting Value only ByGame
               playerObject = userUtil.getJSONOBject(playerArray, "@id", "ByGame");
               playerArray1 = jsonUtils.getJSONArray(playerObject,"Data");
               playerDetail._lxValue = new Number[playerArray1.length()];
               playerDetail._lCumulativeProfitGrossSeries= new Number[playerArray1.length()];
               playerDetail._lCumulativeProfitSeries = new Number[playerArray1.length()];
                double lcumulativeProfit=0;
                double lcumulativeRake=0;

               for (int i=0;i<playerArray1.length();i++){
                   // x value
            	   String profit="0";
            	   String rake="0";
            	   double dataValue=0;
            	   double dataValue1=0;
                   JSONObject xyObject = playerArray1.getJSONObject(i);
                   playerDetail._lxValue[i]=Integer.parseInt(xyObject.getString("@x"));
                   JSONArray yObject = jsonUtils.getJSONArray(xyObject,"Y");
//                   profit = yObject.getJSONObject(1).getString("$");
//                   rake = yObject.getJSONObject(2).getString("$");
                   
//                   profit = getJSONValue(yObject.getJSONObject(1),"$");
                   profit = getJSONValue(userUtil.getJSONOBject(yObject, "@id", "Profit"),"$");
                   if (yObject.length() > 2){
                	   System.out.println("Error executing the Request[processGraph]"+yObject.length());
//                	   rake = getJSONValue(yObject.getJSONObject(2),"$");
                	   rake = getJSONValue(userUtil.getJSONOBject(yObject, "@id", "Rake"),"$");
                   }	
                   
                   dataValue = Double.parseDouble(profit.trim());
                   lcumulativeProfit += dataValue;
                   dataValue1 = Double.parseDouble(rake.trim());
                   lcumulativeRake += dataValue1;
                   
                   playerDetail._lCumulativeProfitSeries[i] = messageBox.RoundDouble(lcumulativeProfit, 2);
                   System.out.println("Profit :" + messageBox.RoundDouble(lcumulativeProfit, 2));
                   playerDetail._lCumulativeProfitGrossSeries[i] = messageBox.RoundDouble((lcumulativeProfit + lcumulativeRake), 2);;
                   System.out.println("Profit/Rake: " + playerDetail._lCumulativeProfitGrossSeries[i] +"--"+ profit +"--"+ dataValue+"--"+dataValue1);

               }
               }catch (Exception e){
                System.out.println("Error executing the Request[processGraph]"+e.toString());
                System.out.println("Error executing the Request[processGraph]"+playerArray1.length());
                
            }


    }
    
    public String getJSONValue(JSONObject object,String name){
    	String value="";
    	try{
    		value=object.getString(name);
    	}catch(Exception e){
    		return "0";
    	}
    	return value;
    }

    public boolean isPlayerOptIn(JSONObject jsonObject,PlayerDetail playerDetail, String _networkName2){
    	 String optValue=null;
    	 
         try{
         optValue = jsonObject.getJSONObject("Player").getJSONObject("Statistics").getString("@optedIn");
         System.out.println("[IsPlayeroptout]"+optValue+_networkName2);
         playerDetail.set_isOptValue(true);
         if(!_networkName2.contains("Poker")){
        	 return false;
         }
         
         return true;
         }catch(Exception e){
        	 playerDetail.set_isOptValue(false);
        	 return false;
         }
    	
    }
    
    
    public void playerValue(JSONArray playerArray,PlayerDetail playerDetail){
    	 for (int i=0;i<playerArray.length();i++){
         	try{
             JSONObject playerHistory = playerArray.getJSONObject(i);
             String value = playerHistory.getString("@id");
             PlayerStatus playerStatus = PlayerStatus.valueOf(value.toUpperCase());
                 switch (playerStatus){
                     case COUNT : playerDetail.set_count(playerHistory.getString("$"));
                                  break;
                     case ABILITY: playerDetail.set_ability(playerHistory.getString("$"));
                                  break;
                     case AVPROFIT: playerDetail.set_avProfit(playerHistory.getString("$"));
                                  break;
                     case AVROI: playerDetail.set_avROI(playerHistory.getString("$"));
                                  break;
                     case AVSTAKE: playerDetail.set_avStake(playerHistory.getString("$"));
                                  break;
                     case CASHES: playerDetail.set_Cashes(playerHistory.getString("$"));
                                  break;
                     case PROFIT: playerDetail.set_Profit(playerHistory.getString("$"));
                                  break;
                     case STAKE: playerDetail.set_stake(playerHistory.getString("$"));
                                  break;
                     case RAKE: playerDetail.set_rake(playerHistory.getString("$"));
                                  break;
                     case TOTALROI: playerDetail.set_totalROI(playerHistory.getString("$"));
                                  break;

                     case ITM: playerDetail.set_itm(playerHistory.getString("$"));
                                  break;
                     case MAXWINNINGSTREAK : playerDetail.set_maxWinningStreak(playerHistory.getString("$"));
                                  break;
                     case MAXLOSINGSTREAK : playerDetail.set_maxLosingStreak(playerHistory.getString("$"));
                                  break;
                     case WINNINGDAYS: playerDetail.set_WinningDays(playerHistory.getString("$"));
                                  break;
                     case LOSINGDAYS: playerDetail.set_LosingDays(playerHistory.getString("$"));
                                  break;
                     default: break;


                 }


             

         } catch (Exception e){
                 System.out.println("Error executing the Request[playerValue]"+e.toString());
                 
             }
         }// end of for loop

    }
    
    public void playerHistory(JSONObject jsonObject, PlayerDetail playerDetail){
    	JsonUtils jsonUtils = new JsonUtils();
        JSONObject playerObject=null;
        JSONArray playerArray=null;
        try{
        playerObject = jsonObject.getJSONObject("Player");
        playerDetail.set_network(playerObject.getString("@network"));
        playerObject = playerObject.getJSONObject("Statistics");
        playerArray = jsonUtils.getJSONArray(playerObject,"Statistic");
        playerValue(playerArray,playerDetail);
        }catch(Exception e){
        	System.out.println("Error executing the Request[playerHistory]"+e.toString());
        }
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_networkName() {
        return _networkName;
    }

    public void set_networkName(String _networkName) {
        this._networkName = _networkName;
    }

    public String get_userid() {
        return _userid;
    }

    public void set_userid(String _userid) {
        this._userid = _userid;
    }

    public String get_passid() {
        return _passid;
    }

    public void set_passid(String _passid) {
        this._passid = _passid;
    }

    public AssetManager get_assetManager() {
        return _assetManager;
    }

    public void set_assetManager(AssetManager _assetManager) {
        this._assetManager = _assetManager;
    }

    public ProcessData getPd(){
        return this.pd;
    }

    public PlayerDetail getPlayerd(){
        return this.playerd;
    }

	public void set_count(String _count) {
		this._count = _count;
	}

	public String get_count() {
		return _count;
	}

}
