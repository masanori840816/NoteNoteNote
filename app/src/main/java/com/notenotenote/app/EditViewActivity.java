package com.notenotenote.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

public class EditViewActivity extends Activity {

    private static DataAccesser _datAccesser;
    private static SQLiteDatabase _db;
    private EditText _etxEditedNote;
    private SpannableStringBuilder _ssbEditedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            // ここからDBに登録。このBool値は何処に戻る？
            this.insertNote();
            return true;
        }
        return false;
    }
    private void insertNote(){
        _datAccesser = new DataAccesser(this);
        _db = _datAccesser.getWritableDatabase();

        _etxEditedNote = (EditText)findViewById(R.id.txtEditView);
        _ssbEditedNote = (SpannableStringBuilder)_etxEditedNote.getText();

        System.out.println(_ssbEditedNote.toString());

        long lngResult = _datAccesser.insertNewNote(_db, _ssbEditedNote.toString());

        if(0 > lngResult){
            System.out.println("failed");
        }else{
            System.out.println("Succeeded");
        }
    }
}
