package com.notenotenote.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by m_masanori on 2014/04/12.
 */
public class DataAccesser extends SQLiteOpenHelper{
    private final static String DB_NAME = "notenotenote.db";
    private final static String DB_TABLENAME = "MyNotes";
    private final static int DB_VER = 1;

    private final static String QUERY_CREATE = "CREATE TABLE IF NOT EXISTS "
        + DB_TABLENAME
        + " (ArticleID INTEGER PRIMARY KEY AUTOINCREMENT"
        + " ,Article TEXT"
        + " ,LastUpdateDate DATETIME)";

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
    public void insertNewNote(SQLiteDatabase db, String strNewNote){

    }
    // Noteの更新
    public void updateNote(SQLiteDatabase db, String strNoteId, String strEditedNote){
    }
    // 登録済みのNoteデータを取得(MainViewActivityに表示)
    public void prepareExistingNote(SQLiteDatabase db){
        System.out.println("Select");
    }
}
