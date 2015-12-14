package com.ruffneck.cloudnote.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.db.AttachDAO;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.db.NoteDAO;

public abstract class BaseActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private ActionBar actionBar;
    protected NoteBookDAO noteBookDAO;
    protected AttachDAO attachDAO;
    protected NoteDAO noteDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentResId());
        initToolbar();

        initDAO();
    }

    private void initDAO() {

        noteBookDAO = NoteBookDAO.getInstance(this);
        attachDAO = AttachDAO.getInstance(this);
        noteDAO = NoteDAO.getInstance(this);
    }

    protected void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
//        toolbar.setCollapsible(false);
    }

    public ActionBar getNewActionBar(){
        return actionBar;
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    protected abstract int setContentResId();


}
