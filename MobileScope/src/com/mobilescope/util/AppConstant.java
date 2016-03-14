package com.mobilescope.util;

public class AppConstant {
	public float textdpsize=3f;
	public enum CurrType{
		USD(0),
		EUR(1),
		PON(2);
		
		private int value;
		private CurrType(int value){
			this.value=value;
		}
	};
	
	public String[] currValue={ "$","€","£"};
	
	public String[] valueCategory ={"All Games" ,
    		"Sit & Go",
    		"Scheduled"};
	
}
