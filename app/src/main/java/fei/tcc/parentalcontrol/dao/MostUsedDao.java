package fei.tcc.parentalcontrol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thiagoretondar on 03/11/16.
 */
public class MostUsedDao extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "mostused";

    public MostUsedDao(Context context) {
        super(context, "MostUsedDao", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "package_name TEXT NOT NULL," +
                "total_time TEXT NOT NULL" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insert(String packageName, String totalTime) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("package_name", packageName);
        values.put("total_time", totalTime);

        db.insert(TABLE_NAME, null, values);
    }

}
