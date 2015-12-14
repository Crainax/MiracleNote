package com.ruffneck.cloudnote.activity.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.MainActivity;
import com.ruffneck.cloudnote.utils.ColorsUtils;

public abstract class MainFragment extends Fragment {

    protected Toolbar toolbar;
    protected ActionBar actionBar;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = ((MainActivity) getActivity()).getToolbar();
        actionBar = ((MainActivity) getActivity()).getNewActionBar();
        fab = ((MainActivity) getActivity()).getFab();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClick(fab);
            }
        });
        initFab(fab);

        initToolbar(toolbar);

        getMainActivity().invalidateOptionsMenu();
    }

    public abstract void onFabClick(FloatingActionButton fab);

    public abstract void initFab(FloatingActionButton fab);

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public void initToolbar(Toolbar toolbar) {

        int color = getActivity().getResources().getColor(R.color.colorPrimary);
        int reverseColor = ColorsUtils.getReverseColor(color);
        toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(reverseColor);
        getMainActivity().getWindow().setStatusBarColor(color);
//        toolbar.setSubtitleTextColor(reverseColor);
        toolbar.setTitle(" ");
//        toolbar.setSubtitle(" ");

    }


    public int optionsMenu() {
        return 0;
    }

    public int optionsMenuItemColor() {

        return Color.WHITE;
    }
}
