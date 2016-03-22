package com.mobilescope.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.mobilescope.R;
import com.mobilescope.preference.UserSettings;
import com.mobilescope.processdata.ProcessPlayerStatus;
import com.mobilescope.util.UIObjects;
import com.mobilescope.util.UserUtil;
import com.mobilescope.database.auth.PlayerDetail;
import com.mobilescope.database.auth.RecentTournament;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: 11/30/11
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecentTournamentHistory extends Activity {
    PlayerDetail playerDetail;
    ArrayList<RecentTournament> tournaments;
    TableLayout tableLayout;
    String _networkName;
    String _signID;
    String _pid;
    RecentTournament recentTournament;
    UserUtil userUtil;
	private UserSettings userSettings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resttour);
        userUtil = new UserUtil();
        ProcessPlayerStatus processPlayerStatus = ((ProcessPlayerStatus) getApplicationContext());
        playerDetail = processPlayerStatus.getPlayerd();
        _networkName = processPlayerStatus.get_networkName();
        _signID = processPlayerStatus.get_userid();
        _pid = processPlayerStatus.get_passid();
        tournaments = playerDetail.get_recentTournaments();
        printArrayList(tournaments);
        tableLayout = (TableLayout)findViewById(R.id.resttourtable);
        UIObjects uiObjects = new UIObjects();
        for(int i=0; i<tournaments.size();i++){
        	recentTournament = tournaments.get(i);
        	TableRow tr = addTableRow(recentTournament,uiObjects, i); 
        	 tr.setOnClickListener(new OnClickListener() {
                 public void onClick(View v) {
                     v.setBackgroundColor(Color.GREEN);
                     System.out.println("Row clicked: " + v.getId());
                     Intent i = new Intent();
				    	i.putExtra("NETWORK", _networkName);
				    	i.putExtra("TOURNAMEID",  tournaments.get(v.getId()).get_id());
				    	i.putExtra("SIGNID",_signID);
				    	i.putExtra("PID", _pid);
				    	i.setClassName("com.mobilescope", "com.mobilescope.tour.detail.TourDetails");
				    	startActivity(i);
				    	

                 }
             });
        	 tableLayout.addView(tr, new TableLayout.LayoutParams(
                   TableLayout.LayoutParams.FILL_PARENT,
                   TableLayout.LayoutParams.WRAP_CONTENT));
        }

    }
//    
//    public boolean onTouch(View v, MotionEvent event){
//    	int viewId= v.getId();
//    	if (event.getAction() == MotionEvent.ACTION_DOWN){
//    		v.setBackgroundResource(R.drawable.pressed_application_background_static); 
//    		}else
//    			if(event.getAction() == MotionEvent.ACTION_UP){
//    				v.setBackgroundResource(R.color.white); handleEvent(viewId);
//    				}else
//    				{ v.setBackgroundResource(R.color.white); 
//    				}
//    	return false; 
//    	return true;
//    }
    
   public TableRow addTableRow( RecentTournament recentTournament, UIObjects uiObjects,int i){
	    TableRow tr = new TableRow(this);
	    userSettings = new UserSettings(this);
	     String Id = recentTournament.get_id();
         String stake =recentTournament.get_stake();
         String rake = recentTournament.get_rake();
         String prize = recentTournament.get_prize();
         String state = recentTournament.get_state();
         String position = recentTournament.get_position();
         String totalEntrants = recentTournament.get_totalEntrants();
         String cur = recentTournament.get_currency();
         String curValue="#";
        
         tr.setId(i);
//         tr.setId(Integer.valueOf(Id));
         tr.setLayoutParams(new TableRow.LayoutParams(
                 TableRow.LayoutParams.FILL_PARENT,
                 TableRow.LayoutParams.FILL_PARENT));
         if (cur.equals("USD")){
        	 curValue="$";
         }
//         if (prize.length()>0 ){
//        	 prize = curValue+"0";
//         }else{
//        	 prize =curValue+prize;
//         }
//         
        View layoutInflater = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
        View layoutInflater1 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
        View layoutInflater2 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
        View layoutInflater3 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
        View layoutInflater4 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
        
        TextView tourId = addTextView(Id,"0",layoutInflater,false,false,uiObjects,.30);
        SpannableString content = new SpannableString(Id);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tourId.setText(content);
        TextView tourStake = addTextView(userSettings.userCurrValue(stake)+"+"+userUtil.currValue(rake),"0",layoutInflater1,false,false,uiObjects,.25);
        TextView tourPrize = addTextView(userSettings.userCurrValue(prize),"0",layoutInflater2,false,false,uiObjects,.25);
//        TextView tourState = addTextView(state,"50",layoutInflater3,false,false,uiObjects);
        TextView tourPosition = addTextView(position+"/"+totalEntrants,"0",layoutInflater3,false,false,uiObjects,.20);
        
        tr.addView(tourId);
        tr.addView(tourStake);
        tr.addView(tourPrize);
        tr.addView(tourPosition);
        return tr;
      }
   
   public TextView addTextView(String value, String width,View layoutInflater,boolean type,boolean rowvalue,UIObjects uiObjects,Double weight){
	   
       return uiObjects.createTextViewSize(value, width, layoutInflater, type,rowvalue,Integer.valueOf(10),weight);
       
   }

    public void printArrayList(ArrayList<RecentTournament> tournaments){
        RecentTournament recentTournament;

        for(int i=0; i<tournaments.size();i++){
            recentTournament = tournaments.get(i);
            System.out.println(tournaments.get(i).get_id());
//            System.out.println(recentTournament.get_stake());
//            System.out.println(recentTournament.get_prize());
//            System.out.println(recentTournament.get_state());
//            System.out.println(recentTournament.get_position());
        }
    }



}
