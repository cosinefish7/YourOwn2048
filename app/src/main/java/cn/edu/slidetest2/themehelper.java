package cn.edu.slidetest2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class themehelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DB_NAME = "My4.db";
    public static final String TABLE_NAME = "theeme";

    themehelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(theme VARCHAR,cards VARCHAR,PRIMARY KEY (theme))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}