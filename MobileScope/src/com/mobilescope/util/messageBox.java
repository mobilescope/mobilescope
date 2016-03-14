package com.mobilescope.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import java.math.BigDecimal;

public class messageBox extends Activity {
    boolean Debug;
    private int _deviceWidth;
    private int _deviceHeight;
    
	public messageBox(){
	   this.Debug=true;
//	   setDeviceWidthHeight();
	}
   
	public int getDeviceWidth(){
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	_deviceWidth=display.getWidth();
    	return _deviceWidth;
	}
	
	public void setDeviceWidthHeight(){
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	_deviceWidth=display.getWidth();
    	_deviceHeight = display.getHeight();
	}
	
	public int getDeviceHeight(){
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	return _deviceHeight=display.getHeight();
    	
	}


    public static double RoundDouble(double r, int decimalPlace) {
           BigDecimal bd = new BigDecimal(r);
           bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
           r = bd.doubleValue();
           if (decimalPlace == 0) {
               return ((int) r);
           }
           return r;
       }

	
	
  public void SimpleBox(String Title, String message){
	  Builder alertdialog;
	  alertdialog =  new AlertDialog.Builder(messageBox.this);  
	  alertdialog.setTitle(Title);
	  alertdialog.setMessage(message);
	  alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		}
	});
	  
	
	  alertdialog.show();
	  
	  
  }

public void set_deviceWidth(int _deviceWidth) {
	this._deviceWidth = _deviceWidth;
}

public int get_deviceWidth() {
	return _deviceWidth;
}

public void set_deviceHeight(int _deviceHeight) {
	this._deviceHeight = _deviceHeight;
}

public int get_deviceHeight() {
	return _deviceHeight;
}	
}
