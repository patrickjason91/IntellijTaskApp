package net.patrickjasonlim.intellijtaskapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static net.patrickjasonlim.intellijtaskapp.data.DataConstants.*;

public class AppDatabase extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "taskapp.db";

    private static AppDatabase sAppDb;

    public static AppDatabase getSingleInstance(Context context) {
        if (sAppDb == null) {
            sAppDb = new AppDatabase(context);
        }
        return sAppDb;
    }

    private AppDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TB_TASKS
                + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_NAME
                + " TEXT," + COL_TIMESTAMP + " INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_TASKS);
        onCreate(db);
    }
}
