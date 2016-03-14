package com.mobilescope.leader;

import java.util.ArrayList;

import com.mobilescope.R;
import com.mobilescope.database.auth.DBUserInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.Toast;

public class LeaderCategoryActivity extends Activity{
	 String Year;
	 ArrayList <LeaderboardRankActivity> LeaderBoardCategory;
	 Button year;
	 Button category;
	 Button subcategory;
	 DBUserInfo db;
	 String workObject;
	 String[] valueCategory ={"Any Game" ,
	    		"Any Game By Network",
	    		"Holdem Heads Up",
	    		"Omaha Hi Heads Up",
	    		"Omaha HiLo Heads Up",
	    		"Stud Heads Up",
	    		"Draw Heads Up",
	    		"Mixed Games Heads Up",
	    		"Holdem 5 to 6 Players",
	    		"Holdem 5 to 6 Players Turbo",
	    		"Any Game 5 to 6 Players Super Turbo",
	    		"Stud 5 to 8 Players",
	    		"Draw 5 to 8 Players",
	    		"Mixed Games 6 to 8 Players",
	    		"Holdem 9 to 10 Players",
	    		"Holdem 9 to 10 Players Turbo",
	    		"Holdem 9 to 10 Players Super Turbo",
	    		"Omaha Hi 5 to 10 Players",
	    		"Omaha HiLo 5 to 10 Players",
	    		"Omaha 2 to 3 Table",
	    		"Any Game 2 to 3 Table",
	    		"Any Games 4 to 6 Table",
	    		"Any Game 7 or More Tables",
	    		"Double or Nothing and Fifty50",
	    		"Streaks",
	    		"Scheduled",
	    		"Heads Up Turbo",
	    		"Heads Up Super Turbo",
	    		"Scheduled by Network"};
	    
	 
	public String getworkObject(){
		return this.workObject;
	}
	public void setworkObject(String workObject){
		this.workObject = workObject;
	}
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public ArrayList<LeaderboardRankActivity> getLeaderBoardCategory() {
		return LeaderBoardCategory;
	}
	public void setLeaderBoardCategory(
			ArrayList<LeaderboardRankActivity> leaderBoardCategory) {
		LeaderBoardCategory = leaderBoardCategory;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topleader);
        this.setTitle("MobileScope");
        db = new DBUserInfo(this);
        db.openDB();
        db.getAssetManager();
        year = (Button)findViewById(R.id.year);
        category = (Button)findViewById(R.id.category);
        subcategory = (Button)findViewById(R.id.subcategory);
        year.setText(db.getSingleTableValue("year", "0","0" , "PokerLeaderCategoryDefault"));
        category.setText(db.getSingleTableValue("category","0" ,"0" , "PokerLeaderCategoryDefault"));
        subcategory.setText(db.getSingleTableValue("subcategory", "0", "0", "PokerLeaderCategoryDefault"));
        year.setOnClickListener(new View.OnClickListener(){
    		public void onClick(View arg0) {
    			workObject="year";
    			AlertCreateDialog(workObject,year);
               }
        });

        category.setOnClickListener(new View.OnClickListener(){
    		public void onClick(View arg0) {
    			workObject="category";
    			AlertCreateDialog(workObject,category);
               }
        });
      
        subcategory.setOnClickListener(new View.OnClickListener(){
    		public void onClick(View arg0) {
    			workObject="subcategory";
    			AlertCreateDialog(workObject,subcategory);
               }
        });
	
	
	
   
}

	
public void AlertCreateDialog(final String field,final Button bName){
   final String[] value=FieldValuesName(field);
   AlertDialog.Builder builder = new AlertDialog.Builder(this);
   builder.setTitle("Select "+field);
   builder.setSingleChoiceItems(value, -1, new DialogInterface.OnClickListener() {
       public void onClick(DialogInterface dialog, int item) {
           Toast.makeText(getApplicationContext(), value[item], Toast.LENGTH_SHORT).show();
           updateLeaderDefault(field,value[item],bName.getText());
           bName.setText(value[item]);
           Intent i = new Intent();
	     	i.setClassName("com.mobilescope", "com.mobilescope.leader.LeaderCategoryActivity");
	    	startActivity(i);
       }
    

   });
   AlertDialog alert = builder.create();
   alert.show();
}

private void updateLeaderDefault(String field, String value,CharSequence text) {
	 String query="update PokerLeaderCategoryDefault set "+field+"='"+value+"' where "+field+"='"+text.toString()+"'";
		db.processSingleRowQuery(query, field);
	
}

public String[] FieldValuesName(String field){
	String [] value=null;
	if (field.equals("year"))
	{	
		String[] value1={"2009","2010","2011","2012"};
		value = value1;
	}else{
		value = FieldValue(field);
	}
	if (field.equals("category")){
		value = valueCategory;
	}
	return value;
}

public String[] FieldValue(String field){
	String value[] = null;
	 String query="Select DISTINCT "+field+" from PokerLeaderCategory";
	return (db.processSingleRowQuery(query, field));
}

@Override
public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
super.onCreateContextMenu(menu, v, menuInfo);
    menu.setHeaderTitle("Select "+this.workObject);
    String[] networks=FieldValuesName(this.workObject);
    for (int i=0;i<networks.length;i++){
    menu.add(Menu.NONE, v.getId(), i, networks[i]).setCheckable(true);
    }


}

	
	
}
