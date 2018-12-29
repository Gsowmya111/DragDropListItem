package com.example.lenovo.dragdroplistitem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.ID;



public class DataBaseHandler1 extends SQLiteOpenHelper {
    //database name initialising
    public static final String DATABASE_NAME = "sample.db";
    //databse version initialisng
    public static final String DATABASE_VERSION = "1";
    public static final String TABLE_NAME = "Positionstore_table";

    //creating ID's/columns to table
    public static final String ID = "ID";
    public static final String ROOMNAME = "roomname";
    public static final String ROOMNO = "roomno";
    public static final String POSITIONNO = "positionno";
    public static final String IMGNAME= "imgname";

    public static SQLiteDatabase db = null;

    //creating table
    private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY , " +
            ROOMNAME + " TEXT ," + ROOMNO + " TEXT ," + POSITIONNO + " TEXT ," + IMGNAME + " TEXT " + ");";


    //default constructor
    public DataBaseHandler1(Context context) {
        super(context, DATABASE_NAME, null, 1);

        //  mcontext = context;
        //  DATABASE_PATH = "/data/data/" + mcontext.getPackageName() + "/databases/";

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //   db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT,PASSWORD TEXT,MOBILENO TEXT,LOGIN_TYPE TEXT)");

        db.execSQL(CREATE_TABLE);
        Log.d("TAG","DATABASE TABLE1 CREATED");

    }
    //updating the database to new version if old version exist
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    //inserting data into table
    public  boolean insertData(String name, String password,String mobile,String usertype) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        //  contentValues1.put(ID, id);
        contentValues1.put(ROOMNAME, name);
        contentValues1.put(ROOMNO, password);
        contentValues1.put(POSITIONNO, mobile);
        contentValues1.put(IMGNAME, usertype);
        //  long result = db.insertWithOnConflict(TABLE_NAME, null, contentValues1,SQLiteDatabase.CONFLICT_REPLACE);
        boolean result = db.insert(TABLE_NAME, null, contentValues1)>0;
        return  result;
        /*if (result == -1)
            return false;
        else
            return true;*/
    }




    //getting number of rows from table
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TABLE_NAME);
        return numRows;
    }





    //updating database
    public boolean updateData(String roomname, String roomno, String positionno,String imagename) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // contentValues.put(COL_1, id);
        contentValues.put(ROOMNAME, roomname);
        contentValues.put(ROOMNO, roomno);
        contentValues.put(POSITIONNO, positionno);
        contentValues.put(IMGNAME, imagename);
        //   long result1 = db.insertWithOnConflict(TABLE_NAME, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        db.update(TABLE_NAME, contentValues, "POSITIONNO = ?", new String[]{imagename});
        return true;
    }


    public boolean updateposition( String oldpositionno,String newpositionnumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POSITIONNO, newpositionnumber);
        //   long result1 = db.insertWithOnConflict(TABLE_NAME, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE);
       boolean  val= db.update(TABLE_NAME, contentValues, "POSITIONNO = ?", new String[]{POSITIONNO})>0;
        return val;
    }

    public boolean updatepos(String oldpositionno,String newpositionnumber){
        SQLiteDatabase db = this.getWritableDatabase();
        oldpositionno="'"+oldpositionno+"'";
        ContentValues args = new ContentValues();
        args.put(POSITIONNO, newpositionnumber);
        boolean  val=  db.update(TABLE_NAME, args, POSITIONNO + "=" + oldpositionno, null)>0;
        return true;
    }


    public boolean updateroompos_whr_nameis(String roomname,String newpos) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean status;
        roomname="'"+roomname+"'";
            ContentValues args = new ContentValues();
            args.put(POSITIONNO, newpos);
        status= db.update(TABLE_NAME, args, ROOMNAME + "=" + roomname, null)>0;
        return status;
    }


    //getting data based on roomnames
    public List<String> getData1() {
        List<String> arr=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + TABLE_NAME +" ORDER BY " + POSITIONNO + " ASC",null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(ROOMNAME));

                arr.add(name);
                cursor.moveToNext();
            }
        }

        return arr;
    }

    //ORDER BY " + SQLiteHelper.listDate+ " ASC"

    //getting data based on roomnames
    public List<String> getimagenames() {
        List<String> arrimg=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + TABLE_NAME +" ORDER BY " + POSITIONNO + " ASC" ,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(IMGNAME));

                arrimg.add(name);
                cursor.moveToNext();
            }
        }

        return arrimg;
    }

    //get all position values
    public int[] getposvalues( int size) {
        List<String> arrpos=new ArrayList<String>();
       int poss[]=new int[size];int h=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + TABLE_NAME  ,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //String name = cursor.getString(cursor.getColumnIndex(POSITIONNO));
                 poss[h]=Integer.parseInt(cursor.getString(cursor.getColumnIndex(POSITIONNO)));
                h++;
               // arrpos.add(name);
                cursor.moveToNext();
            }
        }


        return poss;
    }


    //getting data based on logintype
    public Cursor getposition(List<String> pos) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where IMGNAME="+pos, null );
        if (res != null)
            res.moveToFirst();

        return res;
    }

















    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}