package com.mobilescope.tournament;


import com.mobilescope.R;
import com.mobilescope.asynctask.TourSearchAsyncTask;
import com.mobilescope.tournament.util.TournamentUtils;
import com.mobilescope.util.AppConstant;
import com.mobilescope.util.RangeSeekBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TableLayout;
import android.widget.TextView;

public class TourStaticSearchActivity extends Activity {
	SeekBar _seekentrants;
	TextView _txtentrants;
	SeekBar _seekstake;
	TextView _txtstake;
	Button _networkButton;
	Button _gameClassButton;
	Intent i;
	String signid;
	String pid;
	TournamentUtils _lstseekentrants = new TournamentUtils();
	private int buttonText;
	SlidingDrawer _slideDrawer;
	TourSearchAsyncTask toursearch; 
	TableLayout _toursearch;
	private DisplayMetrics displayMetrics;
	Context _context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_tourstatics_search);
		 
		 
			i = getIntent();
			Bundle b = i.getExtras();
			SharedPreferences sharedPrefs = this.getSharedPreferences("com.mobilescope", Context.MODE_PRIVATE);
			signid = sharedPrefs.getString("sign_id", "NULL");
//			signid = b.getString("SIGNID");
			pid= b.getString("PID");
			_context=this;
		 
		 _txtentrants =(TextView)findViewById(R.id.txtentrants);
		 _networkButton=(Button)findViewById(R.id.tournetwork);
		 _slideDrawer=(SlidingDrawer)findViewById(R.id.slidingDrawer1);
		 _toursearch = (TableLayout)findViewById(R.id.tabletourselector);
		 
		 _gameClassButton=(Button)findViewById(R.id.tourgame);
		 
		 
		 _txtstake = (TextView)findViewById(R.id.txtstake);
		 
		 /*_seekstake = (SeekBar)findViewById(R.id.seekBar2);
		 _seekentrants = (SeekBar)findViewById(R.id.seekBar1);
		 _seekentrants.setMax(_lstseekentrants._entrantlist.size()-1);
		 _seekstake.setMax(_lstseekentrants._stacklist.size()-1);
		 */
		
	
		 i.setClassName("com.mobilescope", "com.mobilescope.SelectNetworkActivity");
		 _networkButton.setOnClickListener(new OnClickListener(){
				
				

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					startActivityForResult(i,buttonText);
				}
	       	
	        });
		 
		 _gameClassButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertCreateDialog(_gameClassButton);
			}
			 
		 });
	/*	 _seekentrants.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
				_txtentrants.setText(_lstseekentrants._entrantlist.get(progress));
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			 
		 });
		 
		 
		 _seekstake.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
			
				_txtstake.setText(_lstseekentrants._stacklist.get(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			 
		 });
*/		 
		 
		 _slideDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener(){

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
//				toursearch.cancel(true);
				toursearch= new TourSearchAsyncTask(_context,displayMetrics);
			}
			 
		 });
		 
		 _slideDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener(){

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				String urlString=drawerUrlStr();
				System.out.println(signid+pid);
				toursearch.execute(signid,pid,urlString,_toursearch);
				
			}

			private String drawerUrlStr() {
				// TODO Auto-generated method stub
//				networks/Merge%2CRevolution/activeTournaments?Filter=Entrants:4~*;Class:SCHEDULED
				String classvalue="";
				String buttonValue = (String) _gameClassButton.getText();
				if (!buttonValue.equals("Game")){
					classvalue=";Class:"+ findValue(buttonValue);
				}
				/*System.out.print("networks/"+_networkButton.getText()+"/activeTournaments?Filter=Entrants:"+_txtentrants.getText()+"~*;StakePlusRake:USD"+_txtstake.getText()+"~*"+classvalue);
				return "networks/"+_networkButton.getText()+"/activeTournaments?Filter=Entrants:"+_txtentrants.getText()+"~*;StakePlusRake:USD"+_txtstake.getText()+"~*"+classvalue;
				*/
				System.out.print("networks/"+_networkButton.getText()+"/activeTournaments?Filter=Entrants:"+_txtentrants.getText()+";StakePlusRake:USD"+_txtstake.getText()+""+classvalue);
				return "networks/"+_networkButton.getText()+"/activeTournaments?Filter=Entrants:"+_txtentrants.getText()+";StakePlusRake:USD"+_txtstake.getText()+classvalue;
				 
			}
			 
		 });
		 displayMetrics = getResources().getDisplayMetrics();
		 
		 RangeSeekBar<Integer> seekBarStake = new RangeSeekBar<Integer>(1, _lstseekentrants._stacklist.size()-1, this.getBaseContext());
	        seekBarStake.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
	                private String TAG="seekBarStake";

					public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
	                        // handle changed range values
	                        Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
	                        
	                        _txtstake.setText(_lstseekentrants._stacklist.get(minValue)+"~"+_lstseekentrants._stacklist.get(maxValue));
	                }
	        });

	        // add RangeSeekBar to pre-defined layout
	        ViewGroup layout = (ViewGroup) findViewById(R.id.seekBar2);
	        layout.addView(seekBarStake);
	        
 		 RangeSeekBar<Integer> seekBarEntrants = new RangeSeekBar<Integer>(1,_lstseekentrants._entrantlist.size()-1, this.getBaseContext());
 		 seekBarEntrants.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
		                private String TAG="seekBarentrants";

						public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
		                        // handle changed range values
		                        Log.i(TAG, "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
		                        
		                        _txtentrants.setText(_lstseekentrants._entrantlist.get(minValue)+"~"+_lstseekentrants._entrantlist.get(maxValue));
		                }
		        });

		        // add RangeSeekBar to pre-defined layout
		        ViewGroup layout1 = (ViewGroup) findViewById(R.id.seekBar1);
		        layout1.addView(seekBarEntrants);
		 
		 
	}
	
	public String findValue(String buttonValue){
		if(buttonValue.equals("Sit & Go")){
			return "SNG";
		}
		return "SCHEDULED";
			
	}
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		  super.onActivityResult(requestCode, resultCode, data);
		_networkButton = (Button)findViewById(R.id.tournetwork);
		  if (requestCode == buttonText){
		   TextView text = null;
		if (resultCode == RESULT_OK) {
			_networkButton.setText(data.getStringExtra("BUTTONTEXT").toString());
		   }
		   else{
			   _networkButton.setText("Canceled");
		   }
		  }
		}
	
	public void AlertCreateDialog( final Button bName){
		   
			AppConstant _constant = new AppConstant();
			AlertDialog alert = null;
		  final String[] value=_constant.valueCategory;
		   AlertDialog.Builder builder = new AlertDialog.Builder(this);
		   builder.setTitle("Select Game");
		   builder.setSingleChoiceItems(value, -1, new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int item) {
		           Toast.makeText(getApplicationContext(), value[item], Toast.LENGTH_SHORT).show();
		             bName.setText(value[item]);
		             dialog.dismiss();
		       }
		    

		   });
		   alert = builder.create();
		   alert.show();
		}

}
