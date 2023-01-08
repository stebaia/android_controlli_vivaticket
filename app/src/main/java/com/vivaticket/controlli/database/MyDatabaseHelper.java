package com.supergianlu.controlli.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.supergianlu.controlli.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.supergianlu.controlli.util.Helper.COLUMN_KEY_ID;
import static com.supergianlu.controlli.util.Helper.COLUMN_PROVINCE_CODE;
import static com.supergianlu.controlli.util.Helper.COLUMN_PROVINCE_NAME;
import static com.supergianlu.controlli.util.Helper.COLUMN_STATE_CODE;
import static com.supergianlu.controlli.util.Helper.COLUMN_STATE_NAME;
import static com.supergianlu.controlli.util.Helper.TABLE_PROVINCE;
import static com.supergianlu.controlli.util.Helper.TABLE_STATE;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDatabase";

    private static final int DATABASE_VERSION = 3;

    private Context context;

    // Database creation sql statement
    private static final String SQL_CREATE_STATE =
            "CREATE TABLE " + TABLE_STATE + " (" +
            COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_STATE_NAME + " TEXT," +
            COLUMN_STATE_CODE + " TEXT)";

    private static final String SQL_CREATE_PROVINCE =
            "CREATE TABLE " + TABLE_PROVINCE + " (" +
                    COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PROVINCE_NAME + " TEXT," +
                    COLUMN_PROVINCE_CODE + " TEXT)";

    private static final String DELETE_STATE = "DROP TABLE IF EXISTS " + TABLE_STATE;
    private static final String DELETE_PROVINCE = "DROP TABLE IF EXISTS " + TABLE_PROVINCE;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_STATE);
        database.execSQL(SQL_CREATE_PROVINCE);

        //AGGIUNGO GLI STATI
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.states);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                String name = line;
                String code = "";
                if((line = bufferedReader.readLine()) != null) {
                    code = line;
                }
                insertState(database, name, code);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //AGGIUNGO LE PROVINCE
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.provinces);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                String name = line;
                String code = "";
                if((line = bufferedReader.readLine()) != null) {
                    code = line;
                }
                insertProvince(database, name, code);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method is called during an upgrade of the database (application)
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL(DELETE_STATE);
        database.execSQL(DELETE_PROVINCE);
        onCreate(database);
    }

    public List<State> getStates(){
        final List<State> states = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STATE, null);
        while(cursor.moveToNext()) {
            State state = new State(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATE_CODE)));
            states.add(state);
        }
        cursor.close();
        return states;
    }

    public List<Province> getProvinces(){
        final List<Province> provinces = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PROVINCE, null);
        while(cursor.moveToNext()) {
            Province province = new Province(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVINCE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVINCE_CODE)));
            provinces.add(province);
        }
        cursor.close();
        return provinces;
    }

    private void insertState(SQLiteDatabase db, String name, String code){
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATE_NAME, name);
        values.put(COLUMN_STATE_CODE, code);
        db.insert(TABLE_STATE, null, values);
    }

    private void insertProvince(SQLiteDatabase db, String name, String code){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROVINCE_NAME, name);
        values.put(COLUMN_PROVINCE_CODE, code);
        db.insert(TABLE_PROVINCE, null, values);
    }


}