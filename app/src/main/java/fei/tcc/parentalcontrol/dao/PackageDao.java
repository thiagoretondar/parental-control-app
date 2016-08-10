package fei.tcc.parentalcontrol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiagoretondar on 8/9/16.
 */
public class PackageDao extends SQLiteOpenHelper {


    public PackageDao(Context context) {
        super(context, "PackageUsage", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE package (" +
                "id INTEGER PRIMARY KEY, " +
                "packagename TEXT NOT NULL)";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE package";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insert(String packagename) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("packagename", packagename);


        db.insert("package", null, values);
    }

    public List<String> findAllPackages() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * FROM package";

        List<String> packages = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            packages.add(cursor.getString(cursor.getColumnIndex("packagename")));
        }

        cursor.close();

        return packages;
    }

}
