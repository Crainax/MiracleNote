package com.ruffneck.cloudnote.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ruffneck.cloudnote.R;

public abstract class BaseActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentResId());
        initToolbar();
    }

    protected void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected ActionBar getNewActionBar(){
        return actionBar;
    }

    protected Toolbar getToolbar(){
        return toolbar;
    }

    protected abstract int setContentResId();
}
