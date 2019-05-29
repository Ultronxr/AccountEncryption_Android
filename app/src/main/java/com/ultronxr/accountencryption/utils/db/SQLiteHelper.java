package com.ultronxr.accountencryption.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;


import com.ultronxr.accountencryption.utils.MD5Hash;
import com.ultronxr.accountencryption.utils.db.bean.Encryptor;
import com.ultronxr.accountencryption.utils.db.bean.Record;
import com.ultronxr.accountencryption.utils.encrypt.AES128ECBPKCS5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "AccountEncryptionDB.db";
    private static final String DB_PATH = "/storage/emulated/0/Android/data/com.ultronxr.accountencryption/db/";
    //private static final String DB_PATH = "/data/data/com.ultronxr.accountencryption/db/";
    private static final int DB_VERSION = 1;

    private static final String SQL_CREATE_TABLE_ENCRYPTOR =
            "CREATE TABLE IF NOT EXISTS encryptor (" +
                    "id INTEGER PRIMARY KEY," +
                    "pwd TEXT NOT NULL," +
                    "createTime datetime default(datetime('now', 'localtime')) NOT NULL," +
                    "lastModifyTime datetime default(datetime('now', 'localtime')) NOT NULL" +
            ");";
    private static final String SQL_CREATE_TABLE_Record =
            "CREATE TABLE IF NOT EXISTS record (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "createTime datetime default(datetime('now', 'localtime')) NOT NULL," +
                "lastModifyTime datetime default(datetime('now', 'localtime')) NOT NULL," +
                "category TEXT," +
                "accountName TEXT," +
                "accountNum TEXT," +
                "accountPwd TEXT," +
                "nick TEXT," +
                "email TEXT," +
                "phone TEXT," +
                "url TEXT," +
                "securityProblem TEXT," +
                "securityAnswer TEXT," +
                "note TEXT" +
            ");";

    public SQLiteHelper(Context context) {
        super(context, DB_PATH+DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_ENCRYPTOR);
        db.execSQL(SQL_CREATE_TABLE_Record);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                break;
            default:
                break;
        }
    }


    private ContentValues setEncryptorToContentValues(Encryptor encryptor){
        ContentValues contentValues = new ContentValues();
        contentValues.put("pwd", encryptor.getPwd());
        contentValues.put("lastModifyTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return contentValues;
    }

    private ContentValues setRecordToContentValues(Record record){
        ContentValues contentValues = new ContentValues();
        contentValues.put("lastModifyTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        contentValues.put("category", record.getCategory());
        contentValues.put("accountName", record.getAccountName());
        contentValues.put("accountNum", record.getAccountNum());
        contentValues.put("accountPwd", record.getAccountPwd());
        contentValues.put("nick", record.getNick());
        contentValues.put("email", record.getEmail());
        contentValues.put("phone", record.getPhone());
        contentValues.put("url", record.getUrl());
        contentValues.put("securityProblem", record.getSecurityProblem());
        contentValues.put("securityAnswer", record.getSecurityAnswer());
        contentValues.put("note", record.getNote());
        return contentValues;
    }

    public void replaceEncryptor(SQLiteDatabase db, Encryptor encryptor){
        ContentValues contentValues = setEncryptorToContentValues(encryptor);
        db.replace("encryptor", null, contentValues);
    }

    public String queryEncryptor(SQLiteDatabase db){
        String encryptor = "";

        Cursor cursor = db.query("encryptor", null, null, null, null, null, null, null);
        if(cursor.moveToNext()){
            encryptor = cursor.getString(1);
        }
        return encryptor;
    }

    public void insertRecord(SQLiteDatabase db, Record record){
        ContentValues contentValues = setRecordToContentValues(record);
        db.insert("record", null, contentValues);
    }

    public void deleteRecord(SQLiteDatabase db, int id){
        db.delete("record", "id=?", new String[]{String.valueOf(id)});
    }

    public void updateRecord(SQLiteDatabase db, Record record){
        ContentValues contentValues = setRecordToContentValues(record);
        db.update("record", contentValues, "id=?", new String[]{String.valueOf(record.getId())});
    }

    public List<Record> queryRecord(SQLiteDatabase db, String accountName){
        List<Record> recordList = new ArrayList<>();
        Cursor cursor;
        if(accountName == null)
            cursor = db.query("record", null, null, null, null,null, null,null);
        else
            cursor = db.query("record", null, null, null, null,null, null,null);

        while(cursor.moveToNext()){
            Record record =
                    new Record(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5),
                            cursor.getString(6), cursor.getString(7), cursor.getString(8),
                            cursor.getString(9), cursor.getString(10), cursor.getString(11),
                            cursor.getString(12), cursor.getString(13));
            recordList.add(record);
        }
        return recordList;
    }


}
