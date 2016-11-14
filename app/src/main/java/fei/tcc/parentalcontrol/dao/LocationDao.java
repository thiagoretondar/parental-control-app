package fei.tcc.parentalcontrol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by thiagoretondar on 03/11/16.
 */
public class LocationDao extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "location";

    public LocationDao(Context context) {
        super(context, "LocationDao", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "lat REAL NOT NULL," +
                "lon REAL NOT NULL," +
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

    public void insert(Double lat, Double lon, Long timestamp) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("lat", lat);
        values.put("lon", lon);
        values.put("usage_timestamp", timestamp);

        db.insert(TABLE_NAME, null, values);
    }

    public Map<Long, List<Double>> selectAllLocations() {
        Map<Long, List<Double>> allLocationInfo = new HashMap<>();
        Long usageTimestamp;
        Double lat, lon;

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            usageTimestamp = cursor.getLong(cursor.getColumnIndex("usage_timestamp"));
            lat = cursor.getDouble(cursor.getColumnIndex("lat"));
            lon = cursor.getDouble(cursor.getColumnIndex("lon"));

            // first is latitude, in second is longitude
            allLocationInfo.put(usageTimestamp, asList(lat, lon));
        }

        cursor.close();

        return allLocationInfo;
    }

    public void deleteAllUntil(Long lastDateLocationUsage) {
        SQLiteDatabase db = getWritableDatabase();

        String[] param = {lastDateLocationUsage.toString()};
        db.delete(TABLE_NAME, "usage_timestamp <= ?", param);
    }

    public int countAll() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);

        return cursor.getCount();
    }

    public long getMaxTimestamp() {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT MAX(usage_timestamp) as usage_timestamp FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor.getLong(cursor.getColumnIndex("usage_timestamp"));
    }

}
