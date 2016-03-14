
package com.mobilescope.leader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilescope.R;
import com.mobilescope.processdata.ProcessPlayerStatus;
import com.mobilescope.util.TouchHighLight;
import com.mobilescope.util.UIObjects;
import com.mobilescope.util.UserUtil;
import com.mobilescope.database.auth.DBUserInfo;
import com.mobilescope.database.auth.RecentTournament;

public class LeaderboardRankActivity extends Activity {
	String Year;
    String CategoryDisplayOrder;
    String Category;
    TableLayout tableLayout;
    DBUserInfo db;
    ArrayList <LeaderboardTabActivity> Leadersubcategory;
    ArrayList<PlayerRank> ListPlayerRank;
    String Title;
    PlayerRank rowPlayerRank;
    Leaderboard lb;
    DisplayMetrics displayMetrics;
    TextView addDisplay;
    UserUtil userUtil;
    TouchHighLight tchHighLight = new TouchHighLight();
    
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public String getCategoryDisplayOrder() {
		return CategoryDisplayOrder;
	}
	public void setCategoryDisplayOrder(String categoryDisplayOrder) {
		CategoryDisplayOrder = categoryDisplayOrder;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public ArrayList<LeaderboardTabActivity> getLeadersubcategory() {
		return Leadersubcategory;
	}
	public void setLeadersubcategory(
			ArrayList<LeaderboardTabActivity> leadersubcategory) {
		Leadersubcategory = leadersubcategory;
	} 
	
	
	
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.rankleader);
	        db = new DBUserInfo(this);
	        userUtil = new UserUtil();
	    	db.openDB();
	    	 Intent i = getIntent();
	         Bundle b = i.getExtras();
	         
	         Title = b.getString("TITLE");
	    	
	        tableLayout = (TableLayout)findViewById(R.id.rankleader);
	        UIObjects uiObjects = new UIObjects();
	        lb = new Leaderboard();
	        ListPlayerRank = dbleaderrank(Title,db,lb);
	        float sizeInDip = 3f;
		 	displayMetrics = getResources().getDisplayMetrics();
	        int pixel = uiObjects.convertToPixel(sizeInDip, displayMetrics);
	        db.close();
	        for(int i1=0; i1<ListPlayerRank.size();i1++){
	        	rowPlayerRank = ListPlayerRank.get(i1);
	        	TableRow tr = addTableRow(rowPlayerRank,uiObjects, i1, pixel); 
	        	 tr.setOnClickListener(new OnClickListener() {
	                 public void onClick(View v) {
	                     v.setBackgroundColor(Color.BLUE);
	                     
	                     System.out.println("Row clicked: " + v.getId());
//	                     String value = ListPlayerRank.get(v.getId()-1).name;
//	                     String network = ListPlayerRank.get(v.getId()-1).network;
	                     String value = ListPlayerRank.get(v.getId()).name;
	                     String network = ListPlayerRank.get(v.getId()).network;
	                     
	                     Toast.makeText(getApplicationContext(), value+":"+network, Toast.LENGTH_SHORT).show();
	                     Intent i = new Intent();
					    	i.putExtra("NETWORK", network);
					    	i.putExtra("USERNAME", value);
					    	 i.setClassName("com.mobilescope", "com.mobilescope.SSMobilePlayerStatus");
					    	 
					    	startActivity(i);
					    	

	                 }
	             });
	        	 tr.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							
							tchHighLight.touchHighlight(v, event,getResources().getDrawable(R.drawable.textviewborder),getResources().getDrawable(R.drawable.textviewhghlig));
							return false; 
						}

					});
	        	 tableLayout.addView(tr, new TableLayout.LayoutParams(
	                   TableLayout.LayoutParams.FILL_PARENT,
	                   TableLayout.LayoutParams.WRAP_CONTENT));
	        }
	        
	       
	        
	    }
	
	  public ArrayList<PlayerRank> dbleaderrank(String Title,DBUserInfo db,Leaderboard lb){
		  Cursor cursor=null;
		  ArrayList<PlayerRank> playerRank = new ArrayList<PlayerRank>();
		  ArrayList<String> category = lb.dbleaderdefaultCategory(db);
		  String query = "select position,name,network,value from PokerLeader where rankingStatisticTitle='";
		  query+=Title+"' AND year='"+category.get(0)+"' AND category='"+category.get(1)+"' AND subcategory ='"+category.get(2)+"'";
		  Log.i("Debug","[LeaderRankActivity]"+query);
		   cursor = db.processSingleRowQuery(query,cursor);
		  if(cursor.moveToFirst())
		   {
		       do
		       {
		    	   PlayerRank prank = new PlayerRank();
		    	   prank.position=cursor.getString(0);
		    	   prank.name=cursor.getString(1);
		    	   prank.network=cursor.getString(2);
		    	   prank.value=cursor.getString(3);
		    	   playerRank.add(prank);
		          
		       }while(cursor.moveToNext());
		       if(cursor != null && !cursor.isClosed())
		          cursor.close();
		   }
		  return playerRank;
	  }
	  
	  
	  public TableRow addTableRow( PlayerRank recentTournament, UIObjects uiObjects,int i, int pixel){
		    TableRow tr = new TableRow(this);
		     String Id = recentTournament.getPosition();
	         String stake =recentTournament.getName();
	         String rake = recentTournament.getNetwork();
	         String prize = recentTournament.getValue();
//	         
//	         final float scale = getContext().getResources().getDisplayMetrics().density;
//	         int pixels = (int) (dps * scale + 0.5f);
//	         
	         tr.setId(i);
	         tr.setPadding(0, pixel, 0, 0);
	         
	         tr.setLayoutParams(new TableRow.LayoutParams(
	                 TableRow.LayoutParams.FILL_PARENT,
	                 TableRow.LayoutParams.MATCH_PARENT));
//	          if (prize.length()>0 ){
//	        	 prize = curValue+"0";
//	         }else{
//	        	 prize =curValue+prize;
//	         }
//	         
	        View layoutInflater = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
	        View layoutInflater1 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
	        View layoutInflater2 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
	        View layoutInflater3 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
//	        View layoutInflater4 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
	        
	        TextView tourId = addTextView(Id,"0",layoutInflater,false,false,uiObjects,.125);
	        TextView tourStake = addTextView(stake,"0",layoutInflater1,false,false,uiObjects,.4);
	        SpannableString content = new SpannableString(stake);
	        content.setSpan(new UnderlineSpan(), 0, content.length()-1, 0);
	        tourStake.setText(content);
	        
	        TextView tourPrize = addTextView(rake,"0",layoutInflater2,false,false,uiObjects,.3);
//	        TextView tourState = addTextView(state,"50",layoutInflater3,false,false,uiObjects);
	        TextView tourPosition = addTextView(userUtil.currValue(prize),"0",layoutInflater3,false,false,uiObjects,.175);
	        
	        tr.addView(tourId);
	        tr.addView(tourStake);
	        tr.addView(tourPrize);
	        tr.addView(tourPosition);
	        return tr;
	    
	   }
	   
	   public TextView addTextView(String value, String width,View layoutInflater,boolean type,boolean rowvalue,UIObjects uiObjects,Double weight){
		   
	       return uiObjects.createTextViewSize(value, width, layoutInflater, type,rowvalue,Integer.valueOf(10),weight);
	       
	   }
}
