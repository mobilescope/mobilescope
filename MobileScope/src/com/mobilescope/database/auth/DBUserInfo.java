package com.mobilescope.database.auth;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBUserInfo extends SQLiteOpenHelper {

    private static final String DB_NAME = "pokerplayerssqlite.db";
    private static final int DB_VERSION_NUMBER = 1;
    private static final String DB_TABLE_NAME = "PokerUserInfo";
    private static final String DB_TABLE_NAME1 = "PokerPlayers";
    private static final String DB_LEADER_CATEGORY ="PokerLeaderCategory";
    private static final String DB_LEADER_CATEGORY_DEFAULT ="PokerLeaderCategoryDefault";
    private static final String DB_LEADER ="PokerLeader";
    private static final String DB_PLAYERHISTORY ="PlayerSearchHistory";
    private static final String DB_TOURHISTORY ="TourSearchHistory";
    
    private static final String db_user_name = "user_name";
    private static final String db_encrytPassword = "encrytPassword";
    private static final String db_metadatahash = "metadatahash";
    private static final String DB_CREATE_SCRIPT = "create table " + DB_TABLE_NAME +
            " (_id integer primary key autoincrement , user_name text not null , encrytPassword text not null, metadatahash text)";
    private static final String DB_CREATE_SCRIPT1 = "create table " + DB_TABLE_NAME1 +
    " (_id integer primary key autoincrement, player_name text not null, player_network text not null, player_prefix text)";

    private static final String DB_LEADER_CATEGORY_SCRIPT = "create table " + DB_LEADER_CATEGORY +
    " (_id integer primary key autoincrement, year text not null, subcategory text, subCategoryDisplayOrder text,categoryDisplayOrder text, category text)";


    private static final String DB_LEADER_CATEGORY_DEFAULT_SCRIPT = "create table " + DB_LEADER_CATEGORY_DEFAULT +
    " (_id integer primary key autoincrement, year text not null, subcategory text, subCategoryDisplayOrder text,categoryDisplayOrder text, category text)";

    
    private static final String DB_LEADER_SCRIPT = "create table " + DB_LEADER +
    " (_id integer primary key autoincrement,year text,category text, subcategory text,rankingStatisticTitle text, currency text, position text, name text, network text,value text, count text)";

    private static final String DB_PLAYERHISTORY_SCRIPT = "create table " + DB_PLAYERHISTORY +
    	    " (_id integer primary key autoincrement,playername text, playernetwork text, lookdate datetime  default (datetime(current_timestamp)))";

    private static final String DB_TRANSCATIONHISTORY_SCRIPT = "create table " + DB_TOURHISTORY +
    	    " (_id integer primary key autoincrement,tourid text, tournetwork text, lookdate datetime  default (datetime(current_timestamp)))";
	    
    
    private SQLiteDatabase sqliteDBInstance = null;

    public AssetManager assetManager;

    public void setAssetManager(AssetManager assetManager){
        this.assetManager=assetManager;
    }

    public AssetManager getAssetManager(){
        return this.assetManager;
    }

    
    public DBUserInfo(Context context)    {
        super(context, DB_NAME, null, DB_VERSION_NUMBER);
        
    }
    
    public DBUserInfo(){
    	
    	super(null, DB_NAME,null,DB_VERSION_NUMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)    {
    // TODO: Implement onUpgrade
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDBInstance)    {
        Log.i("onCreate", "Creating the database...");
      
    }

    public void openDB() throws SQLException    {
       
        if(this.sqliteDBInstance == null)        {
            Log.i("openDB", "Creating sqliteDBInstance is null...");
            try{
            	this.sqliteDBInstance = this.getWritableDatabase();
            }catch(Exception e){
            	Log.i("openDB", "Database is not writable");
//            	createDB();
            }
        }
    }

    public void createDB(){
        	this.sqliteDBInstance.execSQL(DB_CREATE_SCRIPT);
            this.sqliteDBInstance.execSQL(DB_CREATE_SCRIPT1);
            this.sqliteDBInstance.execSQL(DB_LEADER_CATEGORY_SCRIPT);
            this.sqliteDBInstance.execSQL(DB_LEADER_CATEGORY_DEFAULT_SCRIPT);
            this.sqliteDBInstance.execSQL(DB_LEADER_SCRIPT);
            this.sqliteDBInstance.execSQL(DB_PLAYERHISTORY_SCRIPT);
            this.sqliteDBInstance.execSQL(DB_TRANSCATIONHISTORY_SCRIPT);
            
     }
   
    public void createDefault(){
    	insertLeaderCategoryTable("2012", "$101-$300", "70", "10","Any Game", DB_LEADER_CATEGORY_DEFAULT);
    }
    
    public boolean checkDB() throws SQLException{
    	 Log.i("openDB", "Checking DBInstance Open ..."+this.sqliteDBInstance.isOpen());
    	 if(!isTableExist(DB_TABLE_NAME)){
    	     createDB();
    	     createDefault();
    	     Log.i("openDB","Database created ..");
    	     return true;
    	 }
    	 if(isTableHasRow(DB_TABLE_NAME)<1){
    		 
    		 return true;
    	 }
    	 
        return false;
    }

    public int isTableHasRow(String tablename){
    	Cursor cursor = this.sqliteDBInstance.rawQuery("Select count(*) from "+tablename, null);
    	int rowCount;
        if (cursor.moveToFirst()){
        	Log.i("DEBUG","Is Table Row has "+cursor.getInt(0));
//        	cursor.close()
        	rowCount = cursor.getInt(0);
        	cursor.close();
            return rowCount;
    	}
        cursor.close();   
    	return 0;
    }
    
    public void closeDB()    {
        if(this.sqliteDBInstance != null)        {
            if(this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance.close();
        }
    }

    public void insertPlayer(String user_name, String encrytPassword, String metadatahash)    {
        String insertSQL = "INSERT INTO "+DB_TABLE_NAME+ " (user_name , encrytPassword ,  metadatahash )";
        System.out.println("In insertPlayer:"+user_name);
        insertSQL += " VALUES (?,?,?)";
        Object[] bindArgs = new Object[3];
        bindArgs[0] = user_name;
        bindArgs[1] = encrytPassword;
        bindArgs[2] = metadatahash;
        this.sqliteDBInstance.execSQL(insertSQL, bindArgs);
    }

    public void insertLeaderCategory(String year, String subcategory,String subCategoryDisplayOrder ,String categoryDisplayOrder,String category){
    	insertLeaderCategoryTable(year , subcategory , subCategoryDisplayOrder ,categoryDisplayOrder, category,DB_LEADER_CATEGORY );
    }
    
    public void insertLeaderCategoryTable(String year, String subcategory,String subCategoryDisplayOrder ,String categoryDisplayOrder,String category,String TableName)    {
        String insertSQL = "INSERT INTO "+TableName+ " (year , subcategory , subCategoryDisplayOrder ,categoryDisplayOrder, category )";
        insertSQL += " VALUES (?,?,?,?,?)";
        Object[] bindArgs = new Object[5];
        bindArgs[0] = year;
        bindArgs[1] = subcategory;
        bindArgs[2] = subCategoryDisplayOrder;
        bindArgs[3] = categoryDisplayOrder;
        bindArgs[4] = category;
        this.sqliteDBInstance.execSQL(insertSQL, bindArgs);
    }
    
    public boolean removePlayer(String user_name)    {
        int result = this.sqliteDBInstance.delete(DB_TABLE_NAME, "user_name='" + user_name + "'", null);
        if(result > 0)
            return true;
        else
            return false;
    }

    public boolean removeAllPlayers()    {
        int result = this.sqliteDBInstance.delete(DB_TABLE_NAME, "user_name != ''", null);
        if(result > 0)
            return true;
        else
            return false;
    }

    public boolean checkPlayer(String user_name)    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {db_user_name}, "user_name='" + user_name + "'", null, null, null, null);
        if(cursor.getCount() >0){
            cursor.close();
            return true;
        }
        else{
               cursor.close();
            return false;
        }
    }

    public String getValue(String columname, String fieldname, String value_name)    {
    	
//        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {columname}, fieldname+"='" + value_name + "'", null, null, null, null);
    	Cursor cursor = this.sqliteDBInstance.rawQuery("Select "+columname+" from "+DB_TABLE_NAME+" where "+fieldname+"='" + value_name + "'", null);
        String value=null;
    	if (cursor != null){
            cursor.moveToPosition(0);
    	    value = cursor.getString(0);
//            Log.i("DEBUG","Getting column Name "+columname);
//            Log.i("DEBUG","Getting column value "+value);
    	}
        cursor.close();
    	return value;

    }
    
    public String getSingleTableValue(String columname,String fieldname,String value_name, String table_name){
    	String queryString = "Select "+columname+" from "+table_name;
    	if (fieldname != "0"){
    		queryString = "where "+fieldname+"='" + value_name + "'";
    	}
    	Cursor cursor = this.sqliteDBInstance.rawQuery(queryString, null);
        String value=null;
    	if (cursor != null){
            cursor.moveToPosition(0);
    	    value = cursor.getString(0);
//            Log.i("DEBUG","Getting column Name "+columname);
//            Log.i("DEBUG","Getting column value "+value);
    	}
        cursor.close();
    	return value;

    }
    
    

    
    public boolean checkPlayerPrefix(String user_name)    {
        String selectSQL = "SELECT * from " + DB_TABLE_NAME + " where user_name='" + user_name + "'"  ;
        Cursor cursor = this.sqliteDBInstance.rawQuery(selectSQL, null);
        //Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_3_NAME}, "player_prefix='" + playerName + "'", null, null, null, null);
        return cursor.getCount() > 0;
    }


    public long updateDataInfo(String ColumnName,String fieldname, String oldValue, String newValue){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnName, newValue);
        return this.sqliteDBInstance.update(DB_TABLE_NAME, contentValues, fieldname+"='" + oldValue + "'", null);
    }

    public String[] getAllPlayers()    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {db_user_name}, null, null, null, null, null);
        if(cursor.getCount() >0)        {
            String[] str = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext())            {
                str[i] = cursor.getString(cursor.getColumnIndex(db_user_name)).replaceAll("'", "");
                i++;
            }
            return str;
        }
        else
        {
            return new String[] {};
        }
    }
    
    public String getColumnValue(String columname){
    	String value=null;
    	Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {columname},null, null, null, null, null);
    	int columnIndex = cursor.getColumnIndex(columname);
    	if (cursor.getColumnCount()<0){
    		return value;
    	}
    	
    	
        cursor.moveToPosition(0);
        value = cursor.getString(0);
//            Log.i("DEBUG","Getting column Name "+columname);
//            Log.i("DEBUG","Getting column value "+value);
        cursor.close();
    	return value;
    }
    
    public boolean isTableExist(String tablename){
    	boolean tableExist=false;
    	Cursor cursor=null;
        
    	try{
    	 cursor = this.sqliteDBInstance.query(tablename, null, null, null, null, null, null);
    	 tableExist=true;
     	cursor.close();
    	}catch(Exception e){
    		
    	}

    	return tableExist;
    }

	public void insertLeader(String year, String subcategory,
			String subCategoryDisplayOrder, String categoryDisplayOrder,
			String category, String value, String position, String count,
			String network, String name) {
			String insertSQL = "INSERT INTO "+DB_LEADER+ " (year,category, subcategory, rankingStatisticTitle, currency, position, name, network,value,count)";
	        insertSQL += " VALUES (?,?,?,?,?,?,?,?,?,?)";
	        Object[] bindArgs = new Object[10];
	        bindArgs[0] = year;
	        bindArgs[1] = category;
	        bindArgs[2] = subcategory;
	        bindArgs[3] = subCategoryDisplayOrder;
	        bindArgs[4] = categoryDisplayOrder;
	        bindArgs[5] = position;
	        bindArgs[6] = name;
	        bindArgs[7] = network;
	        bindArgs[8] = value;
	        bindArgs[9] = count;
	        
	        this.sqliteDBInstance.execSQL(insertSQL, bindArgs);
		
	}
	
	 public boolean removeLeader()    {
	        int result = this.sqliteDBInstance.delete(DB_LEADER, "year != ''", null);
	        if(result > 0)
	            return true;
	        else
	            return false;
	    }
    
	 public String[] processSingleRowQuery(String query,String columnName){
	        Cursor cursor = this.sqliteDBInstance.rawQuery(query,null);
	        if(cursor.getCount() >0)        {
	            String[] str = new String[cursor.getCount()];
	            int i = 0;
	            while (cursor.moveToNext())            {
	                str[i] = cursor.getString(cursor.getColumnIndex(columnName)).replaceAll("'", "");
	                i++;
	            }
	            cursor.close();
	            return str;
	        }
	        else
	        {   cursor.close();
	            return new String[] {};
	        }
	    }
	 
	 public Cursor processSingleRowQuery(String query,Cursor cursor){
		 try{
		 cursor = this.sqliteDBInstance.rawQuery(query,null);
		 }catch(Exception e){
			 return cursor;
		 }
		 return cursor;
	 }
	 
	 public void insertHistory(String TableName,String name, String network) {
				String insertSQL = "INSERT INTO "+TableName;
				if(TableName.equals(DB_PLAYERHISTORY)){
					insertSQL += "(playername,playernetwork)";
				}else{
					insertSQL += "(tourid,tournetwork)";
				}
					
			    insertSQL += " VALUES (?,?)";
		        Object[] bindArgs = new Object[2];
		        bindArgs[0] = name;
		        bindArgs[1] = network;
		        
		        this.sqliteDBInstance.execSQL(insertSQL, bindArgs);
			
		}


}
