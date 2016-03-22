package com.mobilescope.util;

import com.mobilescope.R;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TableRow;
import android.widget.TextView;

public class UIObjects {

	public UIObjects(){
		
	}
   
	public TextView createTextView(String Value,String width, View layoutInflater,boolean type,boolean rowvalue){
		TextView _textview = (TextView) layoutInflater;
		 if(type && rowvalue){
             if (Value.contains("-")){
          	   _textview.setTextColor(Color.RED);
             }else{
          	   _textview.setTextColor(Color.GREEN);
             }
        }else{
          	   _textview.setTextColor(Color.DKGRAY);
         }
	      _textview.setText(" "+Value);
          _textview.setWidth(Integer.parseInt(width));
          
          _textview.setLayoutParams(new TableRow.LayoutParams(
                          TableRow.LayoutParams.FILL_PARENT,
                          TableRow.LayoutParams.WRAP_CONTENT));
         return _textview;         
	}
	
	@SuppressWarnings("unused")
	public  View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
	    TextView tv = (TextView) view.findViewById(R.id.tabsText);
	    tv.setText(text);
	    
	    return view;

		}
	
	
	public TextView createTextView(String Value,String width, View layoutInflater,boolean type,boolean rowvalue, Double weight){
		TextView _textview = (TextView) layoutInflater;
		
		
		 if(type && rowvalue){
             if (Value.contains("-")){
          	   _textview.setTextColor(Color.RED);
             }else{
          	   _textview.setTextColor(Color.GREEN);
             }
        }else{
          	   _textview.setTextColor(Color.DKGRAY);
         }
	      _textview.setText(Value);
	      _textview.setWidth(Integer.parseInt(width));
          
          _textview.setLayoutParams(new TableRow.LayoutParams(
                          Integer.valueOf(width),
                          TableRow.LayoutParams.WRAP_CONTENT,weight.floatValue()));
         return _textview;         
	}
	
	public TextView createTextViewSize(String Value,String width, View layoutInflater,boolean type,boolean rowvalue, int size){
		TextView _textview = createTextView(Value,width,layoutInflater,type,rowvalue);
		_textview.setTextSize(size);
		_textview.setLines(1);
	
		return _textview;
	}

	public TextView createTextViewSize(String Value,String width, View layoutInflater,boolean type,boolean rowvalue, int size,Double weight){
		TextView _textview = createTextView(Value,width,layoutInflater,type,rowvalue,weight);
		_textview.setTextSize(size);
		_textview.setPadding(size, 2, size, 2);
		_textview.setOnTouchListener( new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(MotionEvent.ACTION_DOWN == event.getAction()){
					v.setBackgroundColor(Color.GREEN);	
				}else{
					v.setBackgroundColor(Color.WHITE);
					
				}
					
				
				return false;
			}
			
		});
		
		_textview.setLines(1);
	
		return _textview;
	}
	
	public int convertToPixel(float sizeInDip, DisplayMetrics displayMetrics){
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDip,displayMetrics);
		return padding;
	}
	
}
