package com.ruffneck.cloudnote.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.adapter.AllNoteBookAdapter;
import com.ruffneck.cloudnote.activity.adapter.DividerItemDecoration;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.models.note.NoteBook;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllNoteBookFragment extends MainFragment {


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

    }

    private void initAdapter() {
        noteBookList = NoteBookDAO.getInstance(getActivity()).queryAll();

        rvNotebook.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvNotebook.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        allNoteBookAdapter = new AllNoteBookAdapter(noteBookList);
        rvNotebook.setAdapter(allNoteBookAdapter);
        rvNotebook.setItemAnimator(new DefaultItemAnimator());
        allNoteBookAdapter.setOnItemClickListener(new AllNoteBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NoteBook noteBook) {

            }
        });
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
}
