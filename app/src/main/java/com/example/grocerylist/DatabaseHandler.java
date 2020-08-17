package com.example.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "grlist.db";
    private static final String TABLE_NAME = "GroceryList";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ITEM = "item";
    private static final String COLUMN_BASKET = "in_basket";
    private static final String FIRST_CLICK = "first_click";

    SQLiteDatabase database;

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + " int PRIMARY KEY, " + COLUMN_ITEM + " TEXT, " + COLUMN_BASKET + " INTEGER, " + FIRST_CLICK +" INTEGER)");
        db.execSQL("CREATE TABLE GroceryList (id INTEGER PRIMARY KEY, item TEXT, in_basket INTEGER, first_click INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertIntoDatabase(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("item",item);
        contentValues.put("in_basket",0);
        contentValues.put("first_click",1);

        long result = db.insert("GroceryList",null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean addToBasket(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("in_basket",1);
        contentValues.put("first_click",0);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{String.valueOf(id)});
        return true;

    }
    public boolean removeFromBasket(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BASKET,0);
        contentValues.put(FIRST_CLICK,1);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{String.valueOf(id)});
        return true;

    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from GroceryList ORDER BY in_basket ASC, id DESC",null);
        return cursor;
    }
    public void deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("GroceryList", null, null);
    }

}
