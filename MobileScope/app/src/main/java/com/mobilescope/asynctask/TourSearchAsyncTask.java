package com.mobilescope.asynctask;

import java.util.ArrayList;

import com.beatme.restclient.http.HttpCreater;
import com.mobilescope.R;
import com.mobilescope.SSMobilePlayerStatus;
import com.mobilescope.history.HistoryDAO;
import com.mobilescope.tournament.backend.TournamentMain;
import com.mobilescope.tournament.backend.TournamentResult;
import com.mobilescope.util.UIObjects;
import com.mobilescope.util.UserUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TourSearchAsyncTask extends AsyncTask<Object, Void,String> {
	
	HttpCreater httpcreator = new HttpCreater();
	UserUtil util = new UserUtil();
	String filename="tourSearch.txt";
	String authResponse;
	TableLayout _touract;
	ArrayList<TournamentResult> listtourresult;
	TournamentResult tourresult;
	TournamentMain tourmain = new TournamentMain();
	Context _context;
	float sizeInDip = 3f;
	private DisplayMetrics _displayMetrics;
	String signid;
	String pid;
	ProgressDialog progressDialog1;
	
	
	public TourSearchAsyncTask(Context _context, DisplayMetrics _displayMetrics) {
		super();
		this._context = _context;
		this._displayMetrics = _displayMetrics;
	}

	@Override
	protected String doInBackground(Object... params) {
		signid= (String)params[0];
		pid = (String)params[1];
		String urlString = (String)params[2];
		_touract=(TableLayout)params[3];
//		_context=(Context)params[4];
//		 _displayMetrics=(DisplayMetrics)params[5];
		String formUrl = httpcreator.formUrl(urlString);
		authResponse = httpcreator.HttpProcessUserAccess(formUrl, signid, pid, filename, null);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		System.out.println(authResponse);
		
		listtourresult = tourmain.processTourSearch(authResponse);
		progressDialog1.cancel();
		populateHistoryUI();
		super.onPostExecute(result);
	}

	public void populateHistoryUI(){
		if (!listtourresult.isEmpty()) {
			
			UIObjects uiObjects = new UIObjects();
			int pixel = uiObjects.convertToPixel(sizeInDip, _displayMetrics);
			for (int i1 = 0; i1 < listtourresult.size(); i1++) {
				tourresult = listtourresult.get(i1);
				TableRow tr = addTableRow(tourresult, uiObjects, i1, pixel);
				tr.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						v.setBackgroundColor(_context.getResources().getColor(R.color.ms_select));
						System.out.println("Row clicked: " + v.getId());
						Intent i = new Intent();
						i.putExtra("TOURNAMEID", listtourresult.get(v.getId()).get_id()
								);
						i.putExtra("NETWORK", listtourresult.get(v.getId())
								.get_network());
						i.putExtra("SIGNID", signid);
						i.putExtra("PID", pid);
//						i.setClassName("com.mobilescope","com.mobilescope.SSMobilePlayerStatus");
						
						
				    	i.setClassName("com.mobilescope", "com.mobilescope.tour.detail.TourDetails");
						_context.startActivity(i);

					}
				});
				tr.setOnTouchListener(new OnTouchListener(){

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						
						// TODO Auto-generated method stub
						return false;
					}
					
				});
				_touract.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
	}

	public TableRow addTableRow(TournamentResult tourresult, UIObjects uiObjects,int i, int pixel) {
		TableRow tr = new TableRow(_context);


		String curValue = "#";

		tr.setId(i);
		tr.setPadding(0, pixel, 0, 0);
		// tr.setId(Integer.valueOf(Id));
		tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		View layoutInflater = LayoutInflater.from(_context).inflate(
				R.layout.textviewlayout, tr, false);
		View layoutInflater1 = LayoutInflater.from(_context).inflate(
				R.layout.textviewlayout, tr, false);
		View layoutInflater2 = LayoutInflater.from(_context).inflate(
				R.layout.textviewlayout, tr, false);
		View layoutInflater3 = LayoutInflater.from(_context).inflate(
				R.layout.textviewlayout, tr, false);

		TextView tourId = addTextView(tourresult.get_id(), "0", layoutInflater, false, false,	uiObjects, .28);
		SpannableString content = new SpannableString(tourresult.get_id());
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		tourId.setText(content);
		TextView tourNetwork = addTextView(tourresult.get_network(), "0", layoutInflater1, false,false, uiObjects, .30);
		TextView tourDate = addTextView(util.convertEpochTime(tourresult.get_date()), "0", layoutInflater2, false,false, uiObjects, .25);
		TextView tourStake = addTextView(tourresult.get_stake(), "0", layoutInflater3, false,false, uiObjects, .22);

		tr.addView(tourId);
		tr.addView(tourNetwork);
		tr.addView(tourDate);
		tr.addView(tourStake);
		return tr;

	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog1 = new ProgressDialog(_context);
		progressDialog1
				.setMessage("Getting tournament details, please wait for mobilescope to get all the data");
		progressDialog1.setCancelable(false);
		progressDialog1.show();
	}

	public TextView addTextView(String value, String width,
			View layoutInflater, boolean type, boolean rowvalue,
			UIObjects uiObjects, Double weight) {

		return uiObjects.createTextViewSize(value, width, layoutInflater, type,
				rowvalue, Integer.valueOf(10), weight);

	}
	
}
