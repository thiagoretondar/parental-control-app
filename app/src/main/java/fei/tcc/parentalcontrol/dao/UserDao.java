package fei.tcc.parentalcontrol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thiagoretondar on 19/11/16.
 */
public class UserDao extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "user";

    public UserDao(Context context) {
        super(context, "UserDao", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "user_id INTEGER," +
                "device_id TEXT," +
                "parent_id INTEGER" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insertParent(Integer parentId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("parent_id", parentId);

        db.insert(TABLE_NAME, null, values);
    }

    public void insertUser(Integer userId, String deviceId, Integer parentId) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("device_id", deviceId);

        db.update(TABLE_NAME, values, "parent_id = " + parentId, null);
    }

    public Long selectIdFromUser() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        return cursor.getLong(cursor.getColumnIndex("user_id"));
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

        String sql = "SELECT count(*) as count FROM " + TABLE_NAME + " WHERE user_id IS NOT NULL and device_id IS NOT NULL";

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndex("count"));

        return (count == 1) ? true : false;
    }

    public boolean existsParent() {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT count(*) as count FROM " + TABLE_NAME + " WHERE parent_id IS NOT NULL";

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndex("count"));

        return (count == 1) ? true : false;
    }

    public Integer selectParentId() {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT parent_id FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        int parentId = cursor.getInt(cursor.getColumnIndex("parent_id"));

        return parentId;
    }
}
