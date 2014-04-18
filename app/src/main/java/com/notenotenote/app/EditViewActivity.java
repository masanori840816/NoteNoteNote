package com.notenotenote.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

public class EditViewActivity extends Activity {

    private static DataAccesser _datAccesser;
    private static SQLiteDatabase _sqlDb;
    private EditText _etxEditedNote;
    private Intent _iitEditView;
    private SpannableStringBuilder _ssbEditedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);
        this.prepareNoteEditor();
    }
    private void prepareNoteEditor(){
        _etxEditedNote = (EditText)findViewById(R.id.txtEditView);
        // MainViewActivityからテキストを受け取り、表示する
        _iitEditView = getIntent();
        _etxEditedNote.setText(_iitEditView.getStringExtra("Note"));
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
            this.insertNote();
            return true;
        }
        return false;
    }
    private void insertNote(){
        _datAccesser = new DataAccesser(this);
        _sqlDb = _datAccesser.getWritableDatabase();

        _ssbEditedNote = (SpannableStringBuilder)_etxEditedNote.getText();

        long lngResult = _datAccesser.insertNewNote(_sqlDb, _ssbEditedNote.toString());

        if(0 > lngResult){
            System.out.println("failed");
        }else{
            System.out.println("Succeeded");
        }
        _sqlDb.close();
    }
}
