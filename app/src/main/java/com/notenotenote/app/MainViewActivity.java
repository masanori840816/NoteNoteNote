package com.notenotenote.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Point;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import java.util.ArrayList;

public class MainViewActivity extends Activity {

    private ArrayList<Integer> _arlNoteId;

    private DataAccesser _datAccesser;
    // 最新のNote or 0件の場合は新規作成を表示するGridView
    private GridView _gvwNote;
    private ArrayList<String> _arlNote = new ArrayList<String>();

    private ActionMode _atmMultipleSelector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        _gvwNote = (GridView) findViewById(R.id.gvwNote);
        _gvwNote.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        _gvwNote.setMultiChoiceModeListener(new MainMultipleSelector());

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

        _arlNoteId = new ArrayList<Integer>();

        // DBにデータが1件以上ある場合はID, Noteを取得する.
        if(0 < intNoteCount){
        	for(int i = 0; intNoteCount > i; i++){
                if(csrNote.moveToNext()) {
                    _arlNoteId.add(csrNote.getInt(csrNote.getColumnIndex(_datAccesser.TABLE_NOTEID)));
                    _arlNote.add(csrNote.getString(csrNote.getColumnIndex(_datAccesser.TABLE_NOTE)));
                }
        	}
        }else{
            _arlNote.add("");
            _arlNoteId.add(0);
        }
        ArrayAdapter<String> _aadNote = new ArrayAdapter<String>(this, R.layout.layout_main_note, _arlNote);

        // Clickイベント追加
        _gvwNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView avwParent, View viwView, int intPosition, long lngId) {
                    // LastNoteの分を追加
                    moveToEditView((int)lngId, _arlNote.get((int)lngId));
                }
            });
        _gvwNote.setAdapter(_aadNote);

        sqlDb.close();
    }
    public void moveToEditView(int intItemNum, String strNote){
        Intent ittMainView = new Intent(MainViewActivity.this, EditViewActivity.class);
        ittMainView.putExtra(_datAccesser.TABLE_NOTEID, _arlNoteId.get(intItemNum));
        ittMainView.putExtra(_datAccesser.TABLE_NOTE, strNote);
        // 次画面のアクティビティ起動
        startActivity(ittMainView);
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
            _atmMultipleSelector = startActionMode(new MainMultipleSelector());
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

    public class MainMultipleSelector implements GridView.MultiChoiceModeListener {
        private ArrayList<Integer> _arlCheckedId = new ArrayList<Integer>();
        private final static int _intBtnDiscardId = 0;

        // 長押しでアイテム選択+削除
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // ActionBarにボタンを追加
            menu.add(0, _intBtnDiscardId, Menu.NONE,"Discard").setIcon(R.drawable.ic_action_discard);

            return true;
        }
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // ActionBarのボタンが選択されたら実行
            switch(item.getItemId()) {
            case _intBtnDiscardId:
                for(int i=0; _arlCheckedId.size() > i; i++){
                    System.out.println("ID " + _arlCheckedId.get(i));
                }
                break;
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
            // 選択モードが解除されたら配列をクリア
            _arlCheckedId.clear();
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            int selectCount = _gvwNote.getCheckedItemCount();

            int intIdNum = _arlCheckedId.indexOf((int)id);
            // 未選択のIDは配列に追加し、選択済みのIDは配列から削除する
            if(0 <= intIdNum) {
                _arlCheckedId.remove(intIdNum);
            }else{
                _arlCheckedId.add((int) id);
            }
            // 選択したアイテム数を表示する
            switch (selectCount) {
                case 1:
                    mode.setTitle("One item selected");
                    break;
                default:
                    mode.setTitle("" + selectCount + " items selected");
                    break;
            }
        }
    }

}
