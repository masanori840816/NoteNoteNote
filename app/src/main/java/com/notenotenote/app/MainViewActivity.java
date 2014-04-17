package com.notenotenote.app;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;

public class MainViewActivity extends Activity {

    private Button[] _btnNotes;
    private List<Integer> _lstNoteId;
    private View.OnClickListener _oclClickListener;

    private LinearLayout _llyMain;
    // 最新のNote or 0件の場合は新規作成を表示するボタンのLayout
    private LinearLayout _llyLastNote;

    private int _intNoteCount = 0;

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
        DataAccesser datAccesser = new DataAccesser(this);
        SQLiteDatabase sqlDb = datAccesser.getWritableDatabase();
        Cursor csrNote = datAccesser.prepareExistingNote(sqlDb);

        _intNoteCount = csrNote.getCount();

        // DBにデータが1件以上ある場合はID, Noteを取得する.
        if(0 < _intNoteCount){
            WindowManager mngWindow = (WindowManager)getSystemService(WINDOW_SERVICE);

            // ボタンサイズを調整するために画面サイズを取得
            Display dspDisplay = mngWindow.getDefaultDisplay();
            Point pntDisplaySize = new Point();
            dspDisplay.getSize(pntDisplaySize);

            // 2件目以降のLayout
            LinearLayout[] llyOtherNotes = new LinearLayout[(int)Math.ceil(_intNoteCount / 3)];
            int intLayoutCount = 0;

            llyOtherNotes[intLayoutCount] = new LinearLayout(this);
            int intItemCount = 0;

            _btnNotes = new Button[_intNoteCount];
            _lstNoteId = new ArrayList<Integer>();

        	for(int i = 0; _intNoteCount > i; i++){
                if(csrNote.moveToNext()) {

                    _btnNotes[i] = new Button(this);

                    int intNoteId = csrNote.getInt(csrNote.getColumnIndex(datAccesser.TABLE_NOTEID));
                    _btnNotes[i].setId(intNoteId);
                    _btnNotes[i].setText(csrNote.getString(csrNote.getColumnIndex(datAccesser.TABLE_NOTE)));
                    // Clickイベント追加
                    _btnNotes[i].setOnClickListener(_oclClickListener);

                    if(i == 0){
                        // 左右のマージン分マイナス
                        _btnNotes[i].setWidth(pntDisplaySize.x);
                        _btnNotes[i].setHeight(pntDisplaySize.y / 3);
                        // RelativeLayoutに追加
                        _llyLastNote.addView(_btnNotes[i]);
                    }else{
                        _btnNotes[i].setWidth(pntDisplaySize.x / 3 - 10);
                        _btnNotes[i].setHeight(pntDisplaySize.y / 6);

                        if(3 > intItemCount){
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
        }
        sqlDb.close();
    }
    private void prepareOnClickListener(){
        _oclClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //System.out.println(_lstNoteId.indexOf(view.getId()) + " idIndex" );
                    Intent ittMoveView = new Intent(MainViewActivity.this, EditViewActivity.class);
                    // 次画面のアクティビティ起動
                    startActivity(ittMoveView);
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
      int id = item.getItemId();
      if (id == R.id.action_settings) {
          return true;
      }
      return super.onOptionsItemSelected(item);
    }
}
