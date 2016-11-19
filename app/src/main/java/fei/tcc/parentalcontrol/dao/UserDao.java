package fei.tcc.parentalcontrol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by thiagoretondar on 19/11/16.
 */
public class UserDao extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "user";

    public UserDao(Context context) {
        super(context, "ParentDao", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "user_id INTEGER NOT NULL," +
                "device_id TEXT NOT NULL" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insert(Integer userId, String deviceId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("device_id", deviceId);

        db.insert(TABLE_NAME, null, values);
    }

    public Integer selectIdFromUser() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex("user_id"));
    }

    public String selectDeviceIdFromUser() {

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        return cursor.getString(cursor.getColumnIndex("device_id"));
    }

    public boolean existsUser() {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT count(*) as count FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndex("count"));

        return (count == 1) ? true : false;
    }

}
