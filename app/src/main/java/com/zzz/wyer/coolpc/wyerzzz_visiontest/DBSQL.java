package com.zzz.wyer.coolpc.wyerzzz_visiontest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by coolpc on 2017/5/14.
 */
public class DBSQL extends SQLiteOpenHelper {
    private static DBSQL instance = null;
    public static DBSQL getInstance(Context ctx){
        if (instance == null){
            instance = new DBSQL(ctx,"testData.db",null,1);
            Log.d("DBSQL","created");
        }
        return instance;
    }
    private DBSQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE testTable (" +
                    "_id INTEGER PRIMARY KEY NOT NULL," +
                    "col_Date VARCHAR," +
                    "col_Name VARCHAR," +
                    "col_Left VARCHAR," +
                    "col_Right VARCHAR)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //insert
    public long dataInsert(String dateTime,String name,String left_vision,String right_vision){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("col_Date",dateTime);
        contentValues.put("col_Name",name);
        contentValues.put("col_Left",left_vision);
        contentValues.put("col_Right",right_vision);

        long id = db.insert("testTable",null,contentValues);
        Log.d("SQL/Insert",id+"");

        return id;
    }
    //query
    public Cursor dataQuery(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("testTable",null,null,null,null,null,null);
        Log.d("SQL/tag","Query");
        return cursor;
    }
    //delete
    public void dataDelete(String position){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("testTable","_id="+position,null);
        Log.d("SQL/delete",i+"");
    }
}
