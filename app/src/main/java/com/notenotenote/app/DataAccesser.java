package com.notenotenote.app;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by m_masanori on 2014/04/12.
 */
public class DataAccesser extends SQLiteOpenHelper{
    private final static String DB_NAME = "notenotenote.db";
    private final static String DB_TABLENAME = "MyNotes";
    private final static int DB_VER = 1;

    public final static String TABLE_NOTEID = "NoteID";
    public final static String TABLE_NOTE = "Note";
    public final static String TABLE_LASTUPDATEDATE = "LastUpdateDate";

    private ContentValues _ctvContents;

    private final static String QUERY_CREATE = "CREATE TABLE IF NOT EXISTS "
        + DB_TABLENAME
        + " ("
        + TABLE_NOTEID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + TABLE_NOTE
        + " TEXT, "
        + TABLE_LASTUPDATEDATE
        + " DATETIME)";

    public DataAccesser(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }
    // DBの作成
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int intOldVer, int intNewVer) {

    }
    // Noteの新規追加
    public long insertNewNote(SQLiteDatabase db, String strNewNote){
        _ctvContents = new ContentValues();
        long lngResult = -1;

        _ctvContents.put(TABLE_NOTE, strNewNote);
        _ctvContents.put(TABLE_LASTUPDATEDATE, java.lang.System.currentTimeMillis());
        lngResult = db.insert(DB_TABLENAME, null, _ctvContents);
        return lngResult;
    }
    // Noteの更新
    public void updateNote(SQLiteDatabase db, String strNoteId, String strEditedNote){
    }
    // 登録済みのNoteデータを取得(MainViewActivityに表示)
    public Cursor prepareExistingNote(SQLiteDatabase db){
        return db.query(DB_TABLENAME, null, null, null, null, null, TABLE_LASTUPDATEDATE + " desc", null);
    }
}
