package com.nikolay.cashtracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    public final static int DB_VERSION = 4;
    public final static String DB_NAME = "CashTracker.db";

//    Table for expenses
    public final static String EXPENSES_TABLE_NAME = "Expenses";
    public final static String KEY_EXPENSE_ID = "_id";
    public final static String KEY_EXPENSE_AMOUNT = "amount";
    public final static String KEY_EXPENSE_CATEGORY = "category";
    public final static String KEY_EXPENSE_DATE = "date";

//    Table for incomes
    public final static String INCOMES_TABLE_NAME = "Incomes";
    public final static String KEY_INCOME_ID = "_id";
    public final static String KEY_INCOME_AMOUNT = "amount";
    public final static String KEY_INCOME_SOURCE = "source";
    public final static String KEY_INCOME_DATE = "date";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

// Rewrite logic. Make separate tables and therefore, for each table there will be according queries

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createExpensesTable = "CREATE TABLE " + EXPENSES_TABLE_NAME + " ("
                + KEY_EXPENSE_ID + " INTEGER PRIMARY KEY,"
                + KEY_EXPENSE_AMOUNT + " INTEGER,"
                + KEY_EXPENSE_CATEGORY + " TEXT,"
                + KEY_EXPENSE_DATE + " TEXT)";

        String createIncomesTable = "CREATE TABLE " + INCOMES_TABLE_NAME + " ("
                + KEY_INCOME_ID + " INTEGER PRIMARY KEY,"
                + KEY_INCOME_AMOUNT + " INTEGER,"
                + KEY_INCOME_SOURCE + " TEXT,"
                + KEY_INCOME_DATE + " TEXT)";

        db.execSQL(createExpensesTable);
        db.execSQL(createIncomesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + EXPENSES_TABLE_NAME);
        onCreate(db);
    }

    public Boolean addData(Map<String, Object> dataMap, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        dataMap.forEach((key, value) -> {
            contentValues.put(key, (String) value);
        });
        long result = db.insert(tableName, null, contentValues);
        return result != -1;
    }

    public Boolean deleteData(int id, String tableName, String columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, columnId + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.getCount() > 0) {
            db.delete(tableName, columnId + " = ?", new String[]{String.valueOf(id)});
            return true;
        }

        cursor.close();
        return false;
    }

    public Cursor readData(ArrayList<String> columnNames, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String columns;

        if(columnNames.size() > 1) columns = TextUtils.join(", ", columnNames);
        else columns = columnNames.get(0);

        if (!columns.contains(KEY_EXPENSE_ID) && !columns.contains(KEY_INCOME_ID)) {
            columns = KEY_EXPENSE_ID + ", " + columns;
        }

        String selectQuery = "SELECT " + columns + " FROM " + tableName;

        return db.rawQuery(selectQuery, null);
    }
}
