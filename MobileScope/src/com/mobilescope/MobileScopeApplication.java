package com.mobilescope;
import org.acra.*;
import org.acra.annotation.*;

import android.app.Application;
import android.content.Intent;


@ReportsCrashes(formKey = "dERCdGwtVkVLN2Q3ZVB0aG95ZUQ5bHc6MQ",
mode = ReportingInteractionMode.TOAST,
forceCloseDialogAfterToast = false, // optional, default false
resToastText = R.string.crash_toast_text)

public class MobileScopeApplication extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
//		ACRA.init(this);
		super.onCreate();
//		Intent i = new Intent();
//		i.setClassName("com.mobilescope",
//				"com.mobilescope.SplashScreen");
//
//		startActivity(i);
	}

}
