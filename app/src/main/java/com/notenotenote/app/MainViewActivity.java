package com.notenotenote.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Point;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;

public class MainViewActivity extends Activity {

    private Button[] _btnNotes;
    private List<Integer> _lstNoteId;
    private View.OnClickListener _oclClickListener;

    DataAccesser _datAccesser;
    private LinearLayout _llyMain;
    // 最新のNote or 0件の場合は新規作成を表示するボタンのLayout
    private LinearLayout _llyLastNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        _llyMain = (LinearLayout) findViewById(R.id.llyMainView);
        _llyLastNote = (LinearLayout) findViewById(R.id.llyLastNote);

        this.prepareOnClickListener();
        this.prepareNoteData();
    }
    private void prepareNoteData(){

        _datAccesser = new DataAccesser(this);
        SQLiteDatabase sqlDb = _datAccesser.getWritableDatabase();
        Cursor csrNote = _datAccesser.prepareExistingNote(sqlDb);

        int intNoteCount = csrNote.getCount();

        WindowManager mngWindow = (WindowManager)getSystemService(WINDOW_SERVICE);

        // ボタンサイズを調整するために画面サイズを取得
        Display dspDisplay = mngWindow.getDefaultDisplay();
        Point pntDisplaySize = new Point();
        dspDisplay.getSize(pntDisplaySize);

        _lstNoteId = new ArrayList<Integer>();

        // DBにデータが1件以上ある場合はID, Noteを取得する.
        if(0 < intNoteCount){

            LinearLayout.LayoutParams lypOtherNote
                     = new LinearLayout.LayoutParams((pntDisplaySize.x / 2 - 15), (pntDisplaySize.y / 4));
            lypOtherNote.setMargins(10, 10, 0, 0);

            // 2件目以降のLayout。小数点切り上げのためにfloatで計算
            LinearLayout[] llyOtherNotes = new LinearLayout[(int)Math.ceil((float)intNoteCount / 2)];
            int intLayoutCount = 0;

            llyOtherNotes[intLayoutCount] = new LinearLayout(this);
            int intItemCount = 0;

            _btnNotes = new Button[intNoteCount];

        	for(int i = 0; intNoteCount > i; i++){
                if(csrNote.moveToNext()) {

                    _btnNotes[i] = new Button(this);

                    int intNoteId = csrNote.getInt(csrNote.getColumnIndex(_datAccesser.TABLE_NOTEID));
                    _btnNotes[i].setId(intNoteId);
                    _btnNotes[i].setText(csrNote.getString(csrNote.getColumnIndex(_datAccesser.TABLE_NOTE)));
                    _btnNotes[i].setTextSize(10f);
                    _btnNotes[i].setBackgroundColor(Color.rgb(230, 230, 255));
                    // Clickイベント追加
                    _btnNotes[i].setOnClickListener(_oclClickListener);

                    if(i == 0){
                        _btnNotes[i].setWidth(pntDisplaySize.x);
                        _btnNotes[i].setHeight(pntDisplaySize.y / 3);
                        // RelativeLayoutに追加
                        _llyLastNote.addView(_btnNotes[i]);
                    }else{
                        _btnNotes[i].setLayoutParams(lypOtherNote);

                        if(2 > intItemCount){
                            // 最新のNote以外は横に3件ずつ並べる
                            llyOtherNotes[intLayoutCount].addView(_btnNotes[i]);
                            intItemCount++;
                        }else{
                            _llyMain.addView(llyOtherNotes[intLayoutCount]);

                            intLayoutCount++;
                            // 次のアイテムを入れるLayout
                            llyOtherNotes[intLayoutCount] = new LinearLayout(this);
                            llyOtherNotes[intLayoutCount].addView(_btnNotes[i]);
                            intItemCount = 1;
                        }
                    }

                    _lstNoteId.add(intNoteId);
                }
        	}
            _llyMain.addView(llyOtherNotes[intLayoutCount]);
        }else{
            // データが1件も無い場合は新規作成ボタンを表示する
            _btnNotes = new Button[1];
            _btnNotes[0] = new Button(this);
            _btnNotes[0].setId(0);
            _btnNotes[0].setText("");
            _btnNotes[0].setBackgroundColor(Color.rgb(230, 230, 255));
            // Clickイベント追加
            _btnNotes[0].setOnClickListener(_oclClickListener);
            _btnNotes[0].setWidth(pntDisplaySize.x);
            _btnNotes[0].setHeight(pntDisplaySize.y / 3);
            // RelativeLayoutに追加
            _llyLastNote.addView(_btnNotes[0]);

            _lstNoteId.add(0);
        }
        sqlDb.close();
    }
    private void prepareOnClickListener(){
        _oclClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ittMainView = new Intent(MainViewActivity.this, EditViewActivity.class);
                    ittMainView.putExtra(_datAccesser.TABLE_NOTEID, _btnNotes[_lstNoteId.indexOf(view.getId())].getId());
                    ittMainView.putExtra(_datAccesser.TABLE_NOTE, _btnNotes[_lstNoteId.indexOf(view.getId())].getText());
                    // 次画面のアクティビティ起動
                    startActivity(ittMainView);
                }
            };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main_view, menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case R.id.action_new:
            this.addNewNote();
            return true;
        case R.id.action_select:
            return true;
        case R.id.action_settings:
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addNewNote(){
        // ActionBarからNoteを追加する
        Intent ittMainView = new Intent(MainViewActivity.this, EditViewActivity.class);
        ittMainView.putExtra(_datAccesser.TABLE_NOTEID, 0);
        ittMainView.putExtra(_datAccesser.TABLE_NOTE, "");
        // 次画面のアクティビティ起動
        startActivity(ittMainView);
    }
}
