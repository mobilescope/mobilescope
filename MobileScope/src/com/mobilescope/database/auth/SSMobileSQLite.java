package com.mobilescope.database.auth;

import com.mobilescope.util.UserUtil;

import android.database.*;
import android.database.sqlite.*;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;

/**
 * User: Al
 * Date: 11/6/11
 * Time: 1:28 AM
 */
public class SSMobileSQLite extends SQLiteOpenHelper {

    private static final String DB_NAME = "pokerplayerssqlite.db";
    private static final int DB_VERSION_NUMBER = 1;
    private static final String DB_TABLE_NAME = "PokerPlayers";
    private static final String DB_COLUMN_1_NAME = "player_name";
    private static final String DB_COLUMN_2_NAME = "player_network";
    private static final String DB_COLUMN_3_NAME = "player_prefix";
    private static final String DB_CREATE_SCRIPT = "create table " + DB_TABLE_NAME +
            " (_id integer primary key autoincrement, player_name text not null, player_network text not null, player_prefix text)";

    private SQLiteDatabase sqliteDBInstance = null;

    public SSMobileSQLite(Context context)    {
        super(context, DB_NAME, null, DB_VERSION_NUMBER);
    }

    public SSMobileSQLite()    {
        super(null, DB_NAME, null, DB_VERSION_NUMBER);
    }

    public SSMobileSQLite(UserUtil userUtil) {
		// TODO Auto-generated constructor stub
    	 super(null, DB_NAME, null, DB_VERSION_NUMBER);
	}

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)    {
    // TODO: Implement onUpgrade
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDBInstance)    {
//       Log.i("onCreate", "Creating the database...");
//       sqliteDBInstance.execSQL(DB_CREATE_SCRIPT);
//    	 sqliteDBInstance =
    }

    public void openDB() throws SQLException    {
        Log.i("openDB", "Checking sqliteDBInstance...");
        if(this.sqliteDBInstance == null)        {
            Log.i("openDB", "Creating sqliteDBInstance...");
            this.sqliteDBInstance = this.getWritableDatabase();
        }
    }

    public void closeDB()    {
        if(this.sqliteDBInstance != null)        {
            if(this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance.close();
        }
    }

    public void insertPlayer(String playerName, String network, String prefix)    {
        String insertSQL = "INSERT INTO "+DB_TABLE_NAME+ " (" + DB_COLUMN_1_NAME + ", " + DB_COLUMN_2_NAME + ", " + DB_COLUMN_3_NAME + ")";
        insertSQL += " VALUES (?,?,?)";
        Object[] bindArgs = new Object[3];
        bindArgs[0] = playerName;
        bindArgs[1] = network;
        bindArgs[2] = prefix;
        this.sqliteDBInstance.execSQL(insertSQL, bindArgs);
    }

    public boolean removePlayer(String playerName)    {
        int result = this.sqliteDBInstance.delete(DB_TABLE_NAME, "player_name='" + playerName + "'", null);
        if(result > 0)
            return true;
        else
            return false;
    }

    public boolean removeAllPlayers()    {
        int result = this.sqliteDBInstance.delete(DB_TABLE_NAME, "player_name != ''", null);
        if(result > 0)
            return true;
        else
            return false;
    }

    public boolean checkPlayer(String playerName)    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME}, "player_name='" + playerName + "'", null, null, null, null);
        if(cursor.getCount() >0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }

    public boolean checkPlayerPrefix(String playerName)    {
        String selectSQL = "SELECT * from " + DB_TABLE_NAME + " where player_prefix='" + playerName + "'"  ;
        Cursor cursor = this.sqliteDBInstance.rawQuery(selectSQL, null);
        //Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_3_NAME}, "player_prefix='" + playerName + "'", null, null, null, null);
        if(cursor.getCount() >0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }


    public long updatePlayer(String oldPlayerName, String newPlayerName)    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_COLUMN_1_NAME, newPlayerName);
        return this.sqliteDBInstance.update(DB_TABLE_NAME, contentValues, "player_name='" + oldPlayerName + "'", null);
    }

    public String[] getAllPlayers()    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, new String[] {DB_COLUMN_1_NAME}, null, null, null, null, null);
        if(cursor.getCount() >0)        {
            String[] str = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext())            {
                str[i] = cursor.getString(cursor.getColumnIndex(DB_COLUMN_1_NAME)).replaceAll("'", "");
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

    public String[] getUniquePlayers()    {
        String query="Select DISTINCT player_name from PokerPlayers";
        return processSingleRowQuery(query,DB_COLUMN_1_NAME );
    }

    public String [] getPlayerNetwork(String playername){
           String query="Select DISTINCT player_network from PokerPlayers where player_name ='"+playername+"'";
           return processSingleRowQuery(query,DB_COLUMN_2_NAME );
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


}
