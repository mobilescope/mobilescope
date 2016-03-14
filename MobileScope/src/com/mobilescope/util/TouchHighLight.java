package com.mobilescope.util;

import android.R;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TouchHighLight {

	@SuppressLint("NewApi")
	public void touchHighlight(View v, MotionEvent event,Drawable normalres, Drawable highres){
		 switch(event.getAction())
         {
             case MotionEvent.ACTION_DOWN:
             	System.out.println("OnTouch::"+ "Changing to GREEN");
                
                 for(int i=0; i<((ViewGroup)v).getChildCount(); ++i) {
                     View nextChild = ((ViewGroup)v).getChildAt(i);
                     if (nextChild instanceof TextView){
                     	TextView txtview = (TextView)nextChild;
                     	if (Build.VERSION.SDK_INT >= 16) {

                     		txtview.setBackground(highres);

                     	} else {

                     	    txtview.setBackgroundDrawable(highres);
                     		
                     	}
                     	
                     	txtview.setPadding(Integer.valueOf(10), 2, Integer.valueOf(10), 2);
                     }
                    
                 }
                 System.out.print("Touch:"+v.getLayoutParams());
                 break;
             case MotionEvent.ACTION_UP:
              	System.out.println("OnTouch::"+ "Changing to GREEN");
                 
                  for(int i=0; i<((ViewGroup)v).getChildCount(); ++i) {
                      View nextChild = ((ViewGroup)v).getChildAt(i);
                      if (nextChild instanceof TextView){
                      	TextView txtview = (TextView)nextChild;
                      	if (Build.VERSION.SDK_INT >= 16) {

                     		txtview.setBackground(normalres);

                     	} else {

                     	    txtview.setBackgroundDrawable(normalres);
                     		
                     	}
                      	
                      	txtview.setPadding(Integer.valueOf(10), 2, Integer.valueOf(10), 2);
                      }
                     
                  }
                  System.out.print("Touch:"+v.getLayoutParams());
                  break;
             default:
             	System.out.println("OnTouch::"+"Changing to BLACK");
                 v.setBackgroundColor(Color.BLACK);
                 break;
         }
         
	}
	
}
