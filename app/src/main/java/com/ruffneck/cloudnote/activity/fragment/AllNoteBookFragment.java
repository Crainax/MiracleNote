package com.ruffneck.cloudnote.activity.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.EditNoteBookActivity;
import com.ruffneck.cloudnote.activity.adapter.AllNoteBookAdapter;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.models.note.NoteBook;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllNoteBookFragment extends MainFragment {

    public static final int SPAN_NOTEBOOK = 3;

    @InjectView(R.id.rv_notebook)
    RecyclerView rvNotebook;
    private List<NoteBook> noteBookList;

    private AllNoteBookAdapter allNoteBookAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_notebook, null);
        ButterKnife.inject(this, view);

        initAdapter();

        return view;
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {


        getMainActivity().startActivityForResult(new Intent(getMainActivity(), EditNoteBookActivity.class),
                getMainActivity().REQUEST_CODE_NEW_BOOK, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());


//        AVObject avObject = new AVObject("test");
//        avObject.put("testFile", avfile);
//        avObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                Toast.makeText(getActivity(), "成功啦>>>", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void initAdapter() {
        noteBookList = NoteBookDAO.getInstance(getActivity()).queryAllNoteBook();

        rvNotebook.setLayoutManager(new StaggeredGridLayoutManager(SPAN_NOTEBOOK, StaggeredGridLayoutManager.VERTICAL));
//        rvNotebook.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        allNoteBookAdapter = new AllNoteBookAdapter(noteBookList);
        rvNotebook.setAdapter(allNoteBookAdapter);
        rvNotebook.setItemAnimator(new DefaultItemAnimator());
        allNoteBookAdapter.setOnItemClickListener(new AllNoteBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NoteBook noteBook) {
                getMainActivity().startNoteBookFragment((int) noteBook.getId());
            }
        });

        //The default animation.
        LayoutAnimationController lac = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_all_notebook);
        rvNotebook.setLayoutAnimation(lac);
    }

    @Override
    public void initFab(FloatingActionButton fab) {
        fab.setImageResource(R.drawable.ic_add_white);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("所有笔记本");
    }
}
