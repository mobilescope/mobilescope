package com.mobilescope.search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.mobilescope.R;
import com.mobilescope.SSMobilePlayerStatus;
import com.mobilescope.processdata.ProcessPlayerStatus;
import com.mobilescope.search.graph.DetailGraph;
import com.mobilescope.database.auth.PlayerDetail;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: 12/5/11
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerGraph extends Activity{
	PlayerDetail playerDetail;
	ProcessPlayerStatus processPlayerStatus;
	Intent intent = null;
	PlayerGraphAsyncTask _playergraphasynctask = new PlayerGraphAsyncTask();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playergraph);
        this.setTitle("MobileScope");
        _playergraphasynctask.execute();

    }
    
    private class PlayerGraphAsyncTask extends AsyncTask<Void, Void, Void>{
        ProgressDialog progressDialog1;
        
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog1.cancel();
			 startActivity(intent);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog1 = new ProgressDialog(PlayerGraph.this);
			progressDialog1
					.setMessage("Getting player information, please wait for mobilescope to get all the data.");
			progressDialog1.setCancelable(false);
			progressDialog1.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			processPlayerStatus = ((ProcessPlayerStatus) getApplicationContext());
	        playerDetail = processPlayerStatus.getPlayerd();
//	        intent = new DetailGraph().execute(this,playerDetail.get_lCumulativeProfitGrossSeries());
	        intent = new DetailGraph().execute(PlayerGraph.this,playerDetail.get_lCumulativeProfitSeries());
			return null;
		}
    	
    }

}
