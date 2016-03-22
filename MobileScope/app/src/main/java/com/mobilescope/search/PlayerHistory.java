package com.mobilescope.search;

import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.mobilescope.R;
import com.mobilescope.preference.UserSettings;
import com.mobilescope.processdata.ProcessPlayerStatus;
import com.mobilescope.database.auth.PlayerDetail;
import com.mobilescope.util.UserUtil;
import com.mobilescope.util.messageBox;
import com.mobilescope.util.UIObjects;


/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: 11/28/11
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerHistory extends Activity{
    PlayerDetail playerDetail;
     private String _Count;
      private String _AvProfit;
       private String _AvStake;
       private String _AvROI;
       private String _TotalProfit;
       private String _Ability;
       TableLayout tableLayout;
       UserUtil userUtil;
       UserSettings userSettings;
       messageBox mb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             setContentView(R.layout.searchresult);
             mb= new messageBox();
             userUtil = new UserUtil();
             userSettings = new UserSettings(this);
             
             ProcessPlayerStatus processPlayerStatus = ((ProcessPlayerStatus) getApplicationContext());
             playerDetail = processPlayerStatus.getPlayerd();
             _Count = playerDetail.get_count();
            _AvProfit = playerDetail.get_avProfit();
            _AvStake = playerDetail.get_avStake();
            _AvROI = playerDetail.get_avROI();
            _TotalProfit = playerDetail.get_Profit();
            _Ability = playerDetail.get_ability();
            tableLayout = (TableLayout)findViewById(R.id.searchresulttable);
           
//            tableLayout = new TableLayout(this);
            TextView textview = new TextView(this);       
            textview.setText("This is the Player tab"); 
            
        Context context = getApplicationContext();
        setTableRowValue(context.getString(R.string.SEARCHRESULTROW1),userSettings.isStringNull(_Count),false);
        setTableRowValue(context.getString(R.string.SEARCHRESULTROW2),userSettings.userCurrValue(_AvProfit),true);
        setTableRowValue(context.getString(R.string.SEARCHRESULTROW3),userSettings.userCurrValue( _AvStake),true);
        setTableRowValue(context.getString(R.string.SEARCHRESULTROW4),userSettings.isStringNull(_AvROI),true);
        setTableRowValue(context.getString(R.string.SEARCHRESULTROW5), userSettings.userCurrValue(_TotalProfit),true);
        setTableRowValue(context.getString(R.string.SEARCHRESULTROW6),userSettings.isStringNull(_Ability),false);
//        setContentView(tableLayout);
        
        
        LayoutInflater inflater = getLayoutInflater();
        View remaingSearchCount = inflater.inflate(R.layout.toplayer, null);
        TextView remaingCount = (TextView) remaingSearchCount.findViewById(R.id.playerSEARCHCOUNT);
        remaingCount.setText(processPlayerStatus.get_count());
        
//        remaingCount.refreshDrawableState();
        
    }

    public void setTableRowValue(String rowName, String rowValue,boolean type){
        TableRow tr = new TableRow(this);
//          TableRow tr = (TableRow) LayoutInflater.from(this).inflate(R.layout.tablerow, tableLayout,false);
        
          tr.setLayoutParams(new TableRow.LayoutParams(
                           TableRow.LayoutParams.FILL_PARENT,
                           TableRow.LayoutParams.MATCH_PARENT
                            ));
                  
                  
                  
              View layoutInflater = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);
              View layoutInflater1 = LayoutInflater.from(this).inflate(R.layout.textviewlayout, tr,false);


           UIObjects uiObjects = new UIObjects();
           TextView tourState = uiObjects.createTextView(rowName, "150", layoutInflater, type,false);
           tr.addView(tourState);
           TextView tourPosition = uiObjects.createTextView(rowValue,"200", layoutInflater1,type,true);    
           tr.addView(tourPosition);


            // Add the TableRow to the TableLayout
            tableLayout.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
    }

}
