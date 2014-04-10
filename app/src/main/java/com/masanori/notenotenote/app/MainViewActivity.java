package com.masanori.notenotenote.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;

public class MainViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
    }
    public void openEditView(View v){
		Intent ittMoveView = new Intent(MainViewActivity.this, EditViewActivity.class);
		// 次画面のアクティビティ起動
		startActivity(ittMoveView);
	}
}
