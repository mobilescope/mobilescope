package com.mobilescope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.beatme.frontend.api.mobilescope;
import com.mobilescope.asynctask.UserRegAsyncTask;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
//import android.support.v4.app.NavUtils;
import android.widget.TextView;

public class UserRegistration extends Activity {
	
	EditText _newUser;
	EditText _newPass;
	EditText _confPass;
	Button _buttonUser;
	TextView _countrycode;
	String jmessage="Passwords did not match";
	Context mContext;
	private CheckBox _subscribe;
	String subscribevalue="N";
	UserRegAsyncTask _userasynctask;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        
        setContentView(R.layout.activity_user_registration);
        _newUser = (EditText)findViewById(R.id.txtnewuserid);
        _newPass = (EditText)findViewById(R.id.txtnewpass);
        _confPass = (EditText)findViewById(R.id.txtnewconfpass);
        _buttonUser = (Button)findViewById(R.id.butnewuserreg);
        _countrycode = (TextView)findViewById(R.id.countrycodevalue);
        _subscribe = (CheckBox)findViewById(R.id.checkBoxsubscribe);
        _countrycode.setText(cellPhoneCountry());
        _buttonUser.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkUserPword()){
					_userasynctask = new UserRegAsyncTask(mContext);
					if(_subscribe.isChecked()){
						subscribevalue="Y";
					}
					_userasynctask.execute(_newUser.getText().toString(),_newPass.getText().toString(),cellPhoneCountry(),subscribevalue,mContext);
				}else{
//					SimpleBox("Registration Error", jmessage);
				}
			}
        	
        });
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_registration, menu);
        return true;
    }
    
    public void SimpleBox(String Title, String message){
    	  Builder alertdialog;
    	  alertdialog =  new AlertDialog.Builder(this);  
    	  alertdialog.setTitle(Title);
    	  alertdialog.setMessage(message);
    	  alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    		
    		public void onClick(DialogInterface dialog, int which) {
    			// TODO Auto-generated method stub
    			_newPass.setBackgroundColor(Color.BLACK);
        		_confPass.setBackgroundColor(Color.BLACK);
        		jmessage="Passwords did not match";
    		}
    	});
    	  
    	
    	  alertdialog.show();
    	  
    	  
      }	

    public String cellPhoneCountry(){
    	
    	return mContext.getResources().getConfiguration().locale.getDisplayCountry();
    }
    public String encryptHttpPassword(String password){
    
    	byte[] defaultBytes = password.getBytes();
    	try {
    	MessageDigest algorithm = MessageDigest.getInstance("MD5");
    	algorithm.reset();
    	algorithm.update(defaultBytes);
    	byte messageDigest[] = algorithm.digest();

    	StringBuffer hexString = new StringBuffer();
    	for (int co = 0; co < messageDigest.length; co++) {
    	hexString.append(Integer.toHexString(0xFF & messageDigest[co]));
    	}
    	return hexString.toString().toLowerCase();
    	} catch (NoSuchAlgorithmException nsae){
    		Log.i("DEBUG", nsae.toString());
    	return null;
    	}
    }
    
    public String encryptLocalPassword(String password){
    	mobilescope ms = new mobilescope();
    	return ms.encryPassword(password);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    
    public boolean checkUserPword(){
    	String txtPassword = _newPass.getText().toString();
    	String txtConfPassword = _confPass.getText().toString();
    	String txtNewUser =  _newUser.getText().toString();
    	boolean cancel = false;
		View focusView = null;
		_newPass.setError(null);
		_confPass.setError(null);
		_newUser.setError(null);
    	if(!checkStringConstrain(txtPassword, _newPass)){
    			Log.i("Debug","password value short");
    			_newPass.setError(getString(R.string.userreg_short));
        		focusView = _newPass;
        		cancel=true;
    	}
    		if(!checkStringConstrain(txtConfPassword,_confPass)){
    		_confPass.setError(getString(R.string.userreg_short));
    		focusView = _confPass;
    		cancel=true;
    	}
    	if(! txtPassword.equals(txtConfPassword)){
    		_newPass.setError(getString(R.string.userreg_password_notmatch));
    		
    		_confPass.setError(getString(R.string.userreg_password_notmatch));
    		focusView = _newPass;
    		cancel=true;
    	}
    	
    	if(isNonCharacter(txtPassword)){
    		Log.i("Debug","password non standarded");
			_newPass.setError(getString(R.string.userreg_nonsupport));
    		focusView = _newPass;
    		cancel=true;
    	}
    	if(isNonCharacter(txtConfPassword)){
    		Log.i("Debug","password non standarded");
			_confPass.setError(getString(R.string.userreg_nonsupport));
    		focusView = _newPass;
    		cancel=true;
    	}
    	if(!isEmailPattern(txtNewUser)){
    		Log.i("Debug","password non standarded");
			_newUser.setError(getString(R.string.userreg_emailpattern));
    		focusView = _newUser;
    		cancel=true;
    	}
    	if(cancel){
    		focusView.requestFocus();
    		return false;
    	}
    	
    	return true;
    	
    }

	public TextView get_newPass() {
		return _newPass;
	}

	public void set_newPass(EditText _newPass) {
		this._newPass = _newPass;
	}

	public TextView get_confPass() {
		return _confPass;
	}

	public void set_confPass(EditText _confPass) {
		this._confPass = _confPass;
	}
	public boolean isNonCharacter(String value){
		return android.util.Patterns.GOOD_IRI_CHAR.matches(value);
	}
	
	public boolean isEmailPattern(String value){
		return android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches();
	}
	
	public boolean checkStringConstrain(String value, TextView textView){
		if (value.equals(null) || value.equals(" ") || value.length()<4 || value.contains(" ") ){
			
			return false;
		}
		
		return true;
	}

}
