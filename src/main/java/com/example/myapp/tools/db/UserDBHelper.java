package com.example.myapp.tools.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDBHelper extends SQLiteOpenHelper {
    public static final String USER_DBNAME = "user.db";

    public UserDBHelper(Context context) {
        super(context, USER_DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table IF NOT EXISTS user"
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId TEXT, token TEXT,updateAt TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE user ADD COLUMN other TEXT");
    }

    public String getToken(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from user where userId=?",
                new String[]{userId});
        if (c.moveToFirst()) {
            return c.getString(c.getColumnIndex("token"));
        } else {
            return null;
        }
    }

    public void setToken(String userId, String token) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from user where userId=?",
                new String[]{userId});
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (c.moveToFirst()) {
            db.execSQL(
                    "update user set token=?,updateAt=? where userId=?",
                    new Object[]{token,df.format(new Date()).toString(), userId});
            db.close();
        } else {
            db.execSQL(
                    "insert into user (userId,token) values(?,?)",
                    new Object[]{userId, token});
        }
        c.close();
        db.close();
    }

    public String getUid(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from user order by _id desc limit 1",
                new String[]{});
        if (c.moveToFirst()) {
            return c.getString(c.getColumnIndex("userId"));
        } else {
            return null;
        }
    }

}
