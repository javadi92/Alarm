package com.javadi.alarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TabHost;

public class DBHelper extends SQLiteOpenHelper {

    private static final String dbName="alarm.db";
    private static final int dbVersion=1;
    private static DBHelper instance=null;

    private DBHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    public synchronized static DBHelper getInstance(Context context){
        if(instance==null){
            instance=new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query="CREATE TABLE IF NOT EXISTS "+DBC.TABLE_NAME+
                " ("+DBC.ID+" INTEGER PRIMARY key AUTOINCREMENT,"+
                DBC.hour+" INTEGER,"+
                DBC.minute+" INTEGER,"+
                DBC.available+" INTEGER)";
        try{
            db.beginTransaction();
            db.execSQL(query);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertAlarm(int hour,int minute){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("hour",hour);
        contentValues.put("minute",minute);
        return db.insert(DBC.TABLE_NAME,null,contentValues);
    }

    public Cursor getAlarms(){
        Cursor cursor=null;
        SQLiteDatabase db= this.getReadableDatabase();
        try{
            cursor=db.rawQuery("SELECT * FROM "+DBC.TABLE_NAME,null);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return cursor;
    }
}
