package com.notenotenote.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class MainViewActivity extends Activity {

    private static DataAccesser _datAccesser;
    private static SQLiteDatabase _db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main_view);
      this.prepareNoteData();
    }
    private void prepareNoteData(){
        _datAccesser = new DataAccesser(this);
        _db = _datAccesser.getWritableDatabase();
        _datAccesser.prepareExistingNote(_db);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

      // Inflate the menu; this adds items to the action bar if it is present.
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
