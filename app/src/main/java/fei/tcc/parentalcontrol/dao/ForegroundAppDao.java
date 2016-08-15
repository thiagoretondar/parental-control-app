package fei.tcc.parentalcontrol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

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
}
