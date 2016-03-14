package com.mobilescope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobilescope.network.NetworkMasterList;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
//import android.support.v4.app.NavUtils;
//import android.support.v4.view.ViewPager.LayoutParams;

public class SelectNetworkActivity extends ExpandableListActivity {

    ExpandableListView explvlist;
    private static final int[][] GROUP_STATE_SETS = null;
    NetworkMasterList _networkMaster = new NetworkMasterList();
    Intent intent = new Intent();
    @Override
    public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           
           setContentView(R.layout.activity_select_network);
           _networkMaster.generateList();

           explvlist = (ExpandableListView) findViewById(android.R.id.list);
          
           explvlist.setAdapter(new ParentLevel(this));
           explvlist.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView v=(TextView) arg1.findViewById(R.id.groupchildname);
//				Toast.makeText(getApplicationContext(), "selected Item Name is "+v.getText(), Toast.LENGTH_LONG).show();
				
			}
        	   
           }

			
           );
           
           explvlist.setOnGroupClickListener(new OnGroupClickListener(){

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				
				TextView textView = (TextView) v.findViewById(R.id.groupname);
				Toast.makeText(getApplicationContext(), "selected Item Name is "+textView.getText(), Toast.LENGTH_LONG).show();
				if( _networkMaster.getChildList(textView.getText().toString()) != null){
//					Toast.makeText(getApplicationContext(), "selected Item Name is not returing", Toast.LENGTH_LONG).show();
				}else{
//					Toast.makeText(getApplicationContext(), "selected Item Name is returing", Toast.LENGTH_LONG).show();
					intent.putExtra("BUTTONTEXT", textView.getText().toString());
					if(getParent() == null){
						setResult(RESULT_OK,intent);
						}else{
							getParent().setResult(RESULT_OK, intent);
						}
						
					finish();
				}
				// TODO Auto-generated method stub
				return false;
			}
           
           });

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu, menu);
        return true;
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())
        {
        case R.id.menu_home:
        	Intent i = new Intent();
            i.setClassName("com.mobilescope", "com.mobilescope.MobileScopeMain");
		   	startActivity(i);
        default:
        }
		return super.onOptionsItemSelected(item);
	}

    public class ParentLevel extends BaseExpandableListAdapter {
            Context context;
            private final  int[] EMPTY_STATE_SET = {};
            private final  int[] GROUP_EXPANDED_STATE_SET =
                    {android.R.attr.state_expanded};
            private  final int[][] GROUP_STATE_SETS = {
                 EMPTY_STATE_SET, // 0
                 GROUP_EXPANDED_STATE_SET // 1
        	};
            
            
            public ParentLevel(Context context){
            	this.context = context;
            }
			private String[] topLevelgroup={"All networks","Live Events","Player Group","Other regions","Everleaf","Merge","Revolution"};
    		private String[][] secondlevelchild={
    				{},
    				{},
    				{},
    				{"Spanish Sites","Swedish Sites","French Sites","Non US","Finnish Sites","Italian Site","Closed network"},
    				{},// Everleaf
    				{},//Merge
    				{} //Revolution
    				
    		};
           @Override
           public Object getChild(int arg0, int arg1) {
                  return secondlevelchild[arg0][arg1];
           }

           @Override
           public long getChildId(int groupPosition, int childPosition) {
                  return childPosition;
           }

           @Override
           public View getChildView(int groupPosition, int childPosition,
                        boolean isLastChild, View convertView, ViewGroup parent) {
                  CustExpListview SecondLevelexplv = new CustExpListview(SelectNetworkActivity.this);
                  SecondLevelexplv.setAdapter(new SecondLevelAdapter(secondlevelchild[groupPosition][childPosition],context, topLevelgroup[groupPosition]));
                  SecondLevelexplv.setGroupIndicator(null);
                  SecondLevelexplv.setOnChildClickListener(new OnChildClickListener(){

					@Override
					public boolean onChildClick(ExpandableListView arg0,
							View arg1, int arg2, int arg3, long arg4) {
						// TODO Auto-generated method stub
						TextView v=(TextView) arg1.findViewById(R.id.groupchildname);
//						Toast.makeText(getApplicationContext(), "selected Item Name .is "+v.getText(), Toast.LENGTH_LONG).show();
						intent.putExtra("BUTTONTEXT", v.getText().toString());
						if(getParent() == null){
						setResult(RESULT_OK,intent);
						}else{
							getParent().setResult(RESULT_OK, intent);
						}
						finish();
						return false;
					}
                	  
                  });
                  return SecondLevelexplv;
           }

           @Override
           public int getChildrenCount(int groupPosition) {
                  return secondlevelchild[groupPosition].length;
           }

           @Override
           public Object getGroup(int groupPosition) {
                  return topLevelgroup[groupPosition];
           }

           @Override
           public int getGroupCount() {
                  return topLevelgroup.length;
           }

           @Override
           public long getGroupId(int groupPosition) {
                  return groupPosition;
           }

           @Override
           public View getGroupView(int groupPosition, boolean isExpanded,
                        View convertView, ViewGroup parent) {
                 
                  if (convertView == null) {
                      LayoutInflater infalInflater = (LayoutInflater) context
                              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                      convertView = infalInflater.inflate(R.layout.group_row, null);
                  }
                View ind= convertView.findViewById(R.id.explist_indicator);
                 
                 
        		if( ind != null ) {
        			ImageView indicator = (ImageView)ind;
        			if( getChildrenCount( groupPosition ) == 0 ) {
        				indicator.setVisibility( View.INVISIBLE );
        			} else {
        				indicator.setVisibility( View.VISIBLE );
        				int stateSetIndex = ( isExpanded ? 1 : 0) ;
        				Drawable drawable = indicator.getDrawable();
        				drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
        			}

        			   TextView tv = (TextView) convertView.findViewById(R.id.groupname);
                       tv.setText(getGroup(groupPosition).toString());

        		}
        		
        		
                  return convertView;
           }

           @Override
           public boolean hasStableIds() {
                  return true;
           }

           @Override
           public boolean isChildSelectable(int groupPosition, int childPosition) {
        	   if (topLevelgroup[groupPosition].length() >0){
        		 System.out.println("Selected Text:"+topLevelgroup[groupPosition]);  
                  return true;
        	   }else{
        		   return true;
        	   }
           }


    }

    public class CustExpListview extends ExpandableListView {

           int intGroupPosition, intChildPosition, intGroupid;

           public CustExpListview(Context context) {
                  super(context);
           }

           protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                  widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,
                               MeasureSpec.AT_MOST);
                  heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,
                               MeasureSpec.AT_MOST);
                  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
           }
    }

    public class SecondLevelAdapter extends BaseExpandableListAdapter {
   
        private final  int[] EMPTY_STATE_SET = {};
        private final  int[] GROUP_EXPANDED_STATE_SET =
                {android.R.attr.state_expanded};
        private  final int[][] GROUP_STATE_SETS = {
             EMPTY_STATE_SET, // 0
             GROUP_EXPANDED_STATE_SET // 1
    	};
        NetworkMasterList _networkMaster = new NetworkMasterList();
    	   private String childValue;
    	   Context context;
    	   private String groupName;
    	   
    	   public SecondLevelAdapter(String childValue, Context context,String groupName){
    		   this.childValue = childValue;
    		   this.context = context;
    		   this.groupName = groupName;
    	   }
    	   
           @Override
           public Object getChild(int groupPosition, int childPosition) {
        	     String[] networkList = _networkMaster.getChildList(childValue);
                  return  networkList[childPosition] ;
           }

           @Override
           public long getChildId(int groupPosition, int childPosition) {
                  return childPosition;
           }

           @Override
           public View getChildView(int groupPosition, int childPosition,
                   boolean isLastChild, View convertView, ViewGroup parent) {
          if (convertView == null) {
              LayoutInflater infalInflater = (LayoutInflater) context
                      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              convertView = infalInflater.inflate(R.layout.groupchild_row, null);
          }
//        View ind= convertView.findViewById(R.id.explist_indicator);
//         
//         
//		if( ind != null ) {
//			ImageView indicator = (ImageView)ind;
//			if( getChildrenCount( groupPosition ) == 0 ) {
//				indicator.setVisibility( View.INVISIBLE );
//			} else {
//				indicator.setVisibility( View.VISIBLE );
//				int stateSetIndex = ( isLastChild ? 1 : 0) ;
//				Drawable drawable = indicator.getDrawable();
//				drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
//			}

			   TextView tv = (TextView) convertView.findViewById(R.id.groupchildname);
               tv.setText(getChild(groupPosition,childPosition).toString());

//		}
		return convertView;
     }

           @Override
           public int getChildrenCount(int groupPosition) {
        	   int childCount = _networkMaster.getChildList(childValue).length;
                  return childCount;
           }

           @Override
           public Object getGroup(int groupPosition) {
                  return childValue;
           }

           @Override
           public int getGroupCount() {
                  return 1;
           }

           @Override
           public long getGroupId(int groupPosition) {
                  return groupPosition;
           }

           @Override
           public View getGroupView(int groupPosition, boolean isExpanded,
                        View convertView, ViewGroup parent) {
        	   if (convertView == null) {
                   LayoutInflater infalInflater = (LayoutInflater) context
                           .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   convertView = infalInflater.inflate(R.layout.child_row, null);
               }
             View ind= convertView.findViewById(R.id.explistchild_indicator);
              
              
     		if( ind != null ) {
     			ImageView indicator = (ImageView)ind;
     			if( getChildrenCount( groupPosition ) == 0 ) {
     				indicator.setVisibility( View.INVISIBLE );
     			} else {
     				indicator.setVisibility( View.VISIBLE );
     				int stateSetIndex = ( isExpanded ? 1 : 0) ;
     				Drawable drawable = indicator.getDrawable();
     				drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
     			}

     			   TextView tv = (TextView) convertView.findViewById(R.id.childname);
                    tv.setText(getGroup(groupPosition).toString());

     		}
     		return convertView;
           }

           @Override
           public boolean hasStableIds() {
                  // TODO Auto-generated method stub
                  return true;
           }

           @Override
           public boolean isChildSelectable(int groupPosition, int childPosition) {
        	     
                  // TODO Auto-generated method stub
                  return true;
           }

    }
}