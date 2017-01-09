package saleelkhan.com.gpstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by saleel on 1/8/2017.
 */

public class Database {
    Helper helper;
    SQLiteDatabase sb;
    Context context;
    public Database(Context context)
    {
        this.context = context;
        helper = new Helper(context,"RobertBosch",null,1);

    }
    public void run()
    {
        sb = helper.getReadableDatabase();
        sb = helper.getWritableDatabase();

    }

    public void insertUserDetails(String name,String password,String userId)
    {
        ContentValues c = new ContentValues();
        c.put("USERNAME",name);//USERNAME
        c.put("PASSWORD",password);
        c.put("USERID",userId);

        sb.insert("UserDetails",null,c);

    }
    public void updateAddress(double lat,double lon,String name,String password,String address)
    {

        //sb.update("MEASUREMENT_REPORT", c, "MID =" + mId + " AND CHECKID =" + 1, null);

        Log.d("updateAddress",name+lat+lon);
        ContentValues c = new ContentValues();
        c.put("latitude",""+lat);
        c.put("langitude",""+lon);
        c.put("address",address);
        sb.update("UserDetails",c,"PASSWORD =" + password,null);
    }

    public void insertAdminDetails(String name,String password)
    {
        ContentValues c = new ContentValues();
        c.put("ADMINNAME",name);
        c.put("ADMINPASSWORD",password);


        sb.insert("AdminDetails",null,c);

    }


    public Cursor getUserDetails(String name)
    {
        Cursor cursor =null;
        cursor=sb.query("UserDetails",null,"USERNAME=?",new String[]{name},null,null,null);
        return cursor;

    }
    public Cursor getUserDetails()
    {
        Cursor cursor =null;
        cursor=sb.query("UserDetails",null,null,null,null,null,null);
        return cursor;

    }
    public class Helper extends SQLiteOpenHelper
    {

        public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table UserDetails(ID INTEGER NOT NULL PRIMARY KEY,USERNAME TEXT,PASSWORD TEXT,USERID TEXT,latitude text,langitude text,address text)");
            db.execSQL("create table AdminDetails(ID INTEGER NOT NULL PRIMARY KEY,ADMINNAME TEXT,ADMINPASSWORD TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
