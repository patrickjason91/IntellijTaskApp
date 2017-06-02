package net.patrickjasonlim.intellijtaskapp.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.patrickjasonlim.intellijtaskapp.data.AppDatabase;
import net.patrickjasonlim.intellijtaskapp.data.DataConstants;

import static net.patrickjasonlim.intellijtaskapp.data.DataConstants.*;

public class Task {

    private static final String TAG = Task.class.getSimpleName();
    public long id;
    public String name;
    public long timestamp;


    public static Task getTaskByCursor(Cursor cursor) {
        if (cursor != null) {
            Task task = new Task();

            task.id = cursor.getLong(cursor.getColumnIndex(_ID));
            task.name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            task.timestamp = cursor.getLong(cursor.getColumnIndex(COL_TIMESTAMP));

            return task;
        } else {
            return null;
        }
    }

    public static Cursor getTaskListCursor(Context context) {
        AppDatabase appDatabase = AppDatabase.getSingleInstance(context);
        SQLiteDatabase db = appDatabase.getWritableDatabase();

        Cursor cursor = db.query(TB_TASKS,null,null, null, null, null, null);
        Log.d(TAG, "gettaskListCursor cursor coutn: " + cursor.getCount());
        return cursor;
    }

    public static Task getTaskById(Context context, long taskId) {
        Task task = new Task();
        AppDatabase appDatabase = AppDatabase.getSingleInstance(context);
        SQLiteDatabase db = appDatabase.getWritableDatabase();

        Cursor cursor = db.query(true, TB_TASKS,null, _ID + " = ?", new String[] {String.valueOf(taskId)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            task.id = cursor.getLong(cursor.getColumnIndex(_ID));
            task.name = cursor.getString(cursor.getColumnIndex(COL_NAME));
            task.timestamp = cursor.getLong(cursor.getColumnIndex(COL_TIMESTAMP));
        }
        cursor.close();

        return task;
    }

    public void updateTask(Context context) {
        AppDatabase appDatabase = AppDatabase.getSingleInstance(context);
        SQLiteDatabase db = appDatabase.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(DataConstants.COL_NAME, name);
        vals.put(DataConstants.COL_TIMESTAMP, timestamp);

        db.update(TB_TASKS, vals, _ID + " = ?", getWhereArg());
    }

    public void deleteTask(Context context) {
        AppDatabase appDatabase = AppDatabase.getSingleInstance(context);
        SQLiteDatabase db = appDatabase.getWritableDatabase();

        db.delete(TB_TASKS, _ID + " = ?", getWhereArg());
    }

    public String[] getWhereArg() {

        return new String[] {String.valueOf(id)};
    }

    public void saveTask(Context context) {
        AppDatabase appDatabase = AppDatabase.getSingleInstance(context);
        SQLiteDatabase db = appDatabase.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(DataConstants.COL_NAME, name);
        vals.put(DataConstants.COL_TIMESTAMP, timestamp);

        long id = db.insert(TB_TASKS, null, vals);
        this.id = id;
    }
}
