package fei.tcc.parentalcontrol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thiagoretondar on 8/14/16.
 */
public class ForegroundAppDao extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "foregroundProccess";

    public ForegroundAppDao(Context context) {
        super(context, "ForegroundAppDao", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "package_name TEXT NOT NULL," +
                "usage_timestamp INTEGER NOT NULL" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insert(String packageName, Long usageTimeStamp) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("package_name", packageName);
        values.put("usage_timestamp", usageTimeStamp);

        db.insert(TABLE_NAME, null, values);
    }

    public Map<String, List<Long>> selectAllAppsUsage() {
        Map<String, List<Long>> allAppsUsage = new HashMap<>();
        String packageName;
        Long usageTimestamp;

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            packageName = cursor.getString(cursor.getColumnIndex("package_name"));
            usageTimestamp = cursor.getLong(cursor.getColumnIndex("usage_timestamp"));

            if (allAppsUsage.containsKey(packageName)) {
                allAppsUsage.get(packageName).add(usageTimestamp);
            } else {
                ArrayList<Long> newUsageTimeStampe = new ArrayList<>();
                newUsageTimeStampe.add(usageTimestamp);
                allAppsUsage.put(packageName, newUsageTimeStampe);
            }
        }

        cursor.close();

        return allAppsUsage;
    }

    public void deleteAllUntil(Long lastDateAppUsage) {
        SQLiteDatabase db = getWritableDatabase();

        String[] param = {lastDateAppUsage.toString()};
        db.delete(TABLE_NAME, "usage_timestamp <= ?", param);
    }

    public int countAll() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);

        return cursor.getCount();
    }
}
