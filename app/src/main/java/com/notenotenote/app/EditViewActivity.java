package com.notenotenote.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

public class EditViewActivity extends Activity {

    private static DataAccesser _datAccesser;
    private static SQLiteDatabase _sqlDb;
    private EditText _etxEditedNote;
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
        _etxEditedNote.setText(getIntent().getStringExtra(_datAccesser.TABLE_NOTE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case R.id.action_share:
            this.shareNote();
            return true;
        case R.id.action_save:
            this.saveNote();
            return true;
        }
        return false;
    }
    private void saveNote(){
        _datAccesser = new DataAccesser(this);
        _sqlDb = _datAccesser.getWritableDatabase();

        _ssbEditedNote = (SpannableStringBuilder)_etxEditedNote.getText();

        long lngResult = -1;

        if(0 >= getIntent().getIntExtra(_datAccesser.TABLE_NOTEID, 0)){
            lngResult = _datAccesser.insertNewNote(_sqlDb, _ssbEditedNote.toString());
        }else{
            lngResult = _datAccesser.updateNote(_sqlDb, getIntent().getIntExtra(_datAccesser.TABLE_NOTEID, 0), _ssbEditedNote.toString());
        }
        if(0 > lngResult){
            Toast.makeText(this, "保存に失敗しました。", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "保存しました。", Toast.LENGTH_LONG).show();
        }
        _sqlDb.close();
    }
    private void shareNote(){
        // Noteを他のアプリに共有する
        _ssbEditedNote = (SpannableStringBuilder)_etxEditedNote.getText();

        Intent iitShare = new Intent();
        iitShare.setAction(Intent.ACTION_SEND);
        iitShare.setType("text/plain");
        iitShare.putExtra(Intent.EXTRA_TEXT, _ssbEditedNote.toString());
        startActivity(iitShare);
    }
}
