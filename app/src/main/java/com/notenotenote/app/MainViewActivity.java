package com.notenotenote.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;

public class MainViewActivity extends Activity {

    private static DataAccesser _datAccesser;
    private static SQLiteDatabase _db;

    private int[] _intNoteIds;
    private String[] _strNotes;
    private int _intNoteCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main_view);
      this.prepareNoteData();
    }
    private void prepareNoteData(){
        _datAccesser = new DataAccesser(this);
        _db = _datAccesser.getWritableDatabase();
        Cursor csrNote = _datAccesser.prepareExistingNote(_db);

        _intNoteCount = csrNote.getCount();
        // DBにデータが1件以上ある場合はID, Noteを取得する.
        if(0 < _intNoteCount){
        	_intNoteIds = new int[_intNoteCount];
        	_strNotes = new String[_intNoteCount];
        	for(int i = 0; _intNoteCount > i; i++){
                if(csrNote.moveToNext()) {
                    _intNoteIds[i] = csrNote.getInt(csrNote.getColumnIndex(_datAccesser.TABLE_NOTEID));
                    _strNotes[i] = csrNote.getString(csrNote.getColumnIndex(_datAccesser.TABLE_NOTE));
                }
        	}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main_view, menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      if (id == R.id.action_settings) {
          return true;
      }
      return super.onOptionsItemSelected(item);
    }
    public void openEditView(View v){
        Intent ittMoveView = new Intent(MainViewActivity.this, EditViewActivity.class);
        // 次画面のアクティビティ起動
        startActivity(ittMoveView);
    }
}
