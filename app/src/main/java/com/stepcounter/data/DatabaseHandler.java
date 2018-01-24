package com.stepcounter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private ReportModel reportModel;

    private static final int DATABASE_VERSION       = 1;
	  
    private static final String DATABASE_NAME       = "Steps.db";

    private static final String TABLE_STEPS         = "table_steps";
    private static final String KEY_ID_STEPS        = "id_steps";
    private static final String KEY_COUNT_STEPS     = "count_steps";
    private static final String KEY_MAX_STEPS       = "max_steps";
    private static final String KEY_TOTAL_TIME      = "total_time";
    private static final String KEY_TIMESTAMP_STEPS = "timestamp_steps";

 	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

        String CREATE_STEPS_TABLE = "CREATE TABLE IF NOT EXISTS " +TABLE_STEPS+ "("
                + KEY_ID_STEPS + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COUNT_STEPS + " INTEGER,"+ KEY_MAX_STEPS + " INTEGER,"
                + KEY_TOTAL_TIME + " INTEGER,"+ KEY_TIMESTAMP_STEPS + " DEFAULT CURRENT_TIMESTAMP" +")";

        db.execSQL(CREATE_STEPS_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        if (newVersion>oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        }
        onCreate(db);

    }  // method body finish

    public void addSteps(int steps,int max_steps,int total_time) {
        createTables();
        SQLiteDatabase db    = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COUNT_STEPS,steps);
        values.put(KEY_MAX_STEPS,max_steps);
        values.put(KEY_TOTAL_TIME,total_time);

        System.out.println("================================================================");
        System.out.println(steps);
        System.out.println(max_steps);
        System.out.println(total_time);
        System.out.println("================================================================");

        db.insert(TABLE_STEPS, null, values);
        db.close(); // Closing database connection
    }
    public ArrayList<ReportModel> getSteps(String current_date){
        createTables();
        String selectQuery="";
        ArrayList<ReportModel> mlist  =new ArrayList<ReportModel>();
        if (current_date.equals("0"))
            selectQuery = "SELECT SUM(count_steps), SUM(total_time), max_steps, date(timestamp_steps, 'localtime') FROM " + TABLE_STEPS+" GROUP BY date(timestamp_steps, 'localtime')";
        else
            selectQuery = "SELECT SUM(count_steps), SUM(total_time), max_steps, date(timestamp_steps, 'localtime') FROM " + TABLE_STEPS+" where date(timestamp_steps, 'localtime') = '" + current_date + "' GROUP BY date(timestamp_steps, 'localtime')";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                reportModel =new ReportModel();
                reportModel.setTotal_steps(cursor.getInt(0));
                reportModel.setTotal_time(cursor.getInt(1));
                reportModel.setMax_steps(cursor.getInt(2));
                reportModel.setDate(cursor.getString(3));
                mlist.add(reportModel);

            } while (cursor.moveToNext());
        }// if complete

        db.close();
        return mlist;
    }



    public void createTables(){
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_STEPS_TABLE = "CREATE TABLE IF NOT EXISTS " +TABLE_STEPS+ "("
                + KEY_ID_STEPS + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COUNT_STEPS + " INTEGER,"+ KEY_MAX_STEPS + " INTEGER,"
                + KEY_TOTAL_TIME + " INTEGER,"+ KEY_TIMESTAMP_STEPS + " DEFAULT CURRENT_TIMESTAMP" +")";
        db.execSQL(CREATE_STEPS_TABLE);
        db.close();
    }

    public String getTime(int duration) {

        int hr = duration / 3600;
        int v = duration % 3600;
        int min = v / 60;
        int sec = v % 60;

        String time="";
        if(hr!=0){

                    if(min!=0)
                    {
                              if(sec!=0)
                                  time =""+hr+"hour"+" "+min+"min"+" "+sec+"sec";
                              else
                                  time =""+hr+"hour"+" "+min+"min";
                    }
                    else{
                            if(sec!=0)
                                time =""+hr+"hour"+" "+sec+"sec";
                            else
                                time =""+hr+"hour";
                     }
        }
        else{
                    if(min!=0)
                    {
                            if(sec!=0)
                                time = ""+min+"min"+" "+sec+"sec";
                            else
                                time = ""+min+"min";
                    }
                    else{
                        time = ""+sec+"sec";
                    }
        }

         return time;
    }
}
