package com.ruffneck.cloudnote.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ruffneck.cloudnote.AttachAdapter;
import com.ruffneck.cloudnote.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.til_title)
    TextInputLayout tilTitle;
    @InjectView(R.id.cardview)
    CardView cardview;
    @InjectView(R.id.til_content)
    TextInputLayout tilContent;
    @InjectView(R.id.iv_alarm)
    ImageView ivAlarm;
    @InjectView(R.id.group_alarm)
    RelativeLayout groupAlarm;
    @InjectView(R.id.rv_attach)
    RecyclerView rvAttach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.inject(this);
        initRecyclerViewAdapter();
    }


    /**
     * Initialize the attack recyclerView in the card.
     */
    private void initRecyclerViewAdapter() {
        rvAttach.setLayoutManager(new GridLayoutManager());
        rvAttach.setAdapter(new AttachAdapter());
    }



}
