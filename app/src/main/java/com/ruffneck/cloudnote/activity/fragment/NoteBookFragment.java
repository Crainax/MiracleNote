package com.ruffneck.cloudnote.activity.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.MainActivity;
import com.ruffneck.cloudnote.activity.NewActivity;
import com.ruffneck.cloudnote.activity.adapter.DividerItemDecoration;
import com.ruffneck.cloudnote.activity.adapter.NoteAdapter;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.db.NoteDAO;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;
import com.ruffneck.cloudnote.utils.ColorsUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NoteBookFragment extends MainFragment {

    /**
     * Start the activity that edit thte note.
     */
    public static final int REQUEST_EDIT_NOTE = 0x124;
    public static final int REQUEST_ADD_NOTE = 0x125;
    private List<Note> noteList;
    private NoteAdapter noteAdapter;

    @InjectView(R.id.tv_notebook_detail)
    TextView tvNotebookDetail;
    @InjectView(R.id.rv_note)
    RecyclerView rvNote;

    private NoteBook noteBook;

    public static NoteBookFragment newInstance(NoteBook noteBook) {
        NoteBookFragment noteBookFragment = new NoteBookFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("notebook", noteBook);
        noteBookFragment.setArguments(bundle);

        return noteBookFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_notebook, null);
        ButterKnife.inject(this, contentView);

        tvNotebookDetail.setText(noteBook.getDetail());

        initAdapter();

        return contentView;

    }

    @Override
    public void onFabClick(FloatingActionButton fab) {
        Intent intent = new Intent(getActivity(), NewActivity.class);
        intent.putExtra(DBConstants.Note.COLUMN_NOTEBOOK, noteBook.getId());
        startActivityForResult(intent, REQUEST_ADD_NOTE);
    }

    @Override
    public void initFab(FloatingActionButton fab) {
        fab.setImageResource(R.drawable.ic_add_white);
    }

    private void initAdapter() {
        noteList = NoteDAO.getInstance(getActivity()).queryByNoteBookId(noteBook.getId());
        rvNote.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvNote.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        noteAdapter = new NoteAdapter(noteList);
        rvNote.setAdapter(noteAdapter);
        rvNote.setItemAnimator(new DefaultItemAnimator());
        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Note note) {
                Intent intent = new Intent(getActivity(), NewActivity.class);
                intent.putExtra("note", note);
                startActivityForResult(intent, REQUEST_EDIT_NOTE);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        noteBook = bundle.getParcelable("notebook");
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initToolbar(Toolbar toolbar) {

        if (noteBook != null) {

            int color = (int) noteBook.getColor();
            int reverseColor = ColorsUtils.getReverseColor(color);

            toolbar.setBackgroundColor(color);
            toolbar.setTitle(noteBook.getName());
            toolbar.setTitleTextColor(reverseColor);
            getMainActivity().getWindow().setStatusBarColor(color);

//            toolbar.setSubtitleTextColor(reverseColor);
//            toolbar.setSubtitle(noteBook.getDetail());

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Note note;
            switch (requestCode) {
                case REQUEST_EDIT_NOTE:
                    note = data.getParcelableExtra("note");
                    int index = noteList.indexOf(note);
                    noteList.set(index, note);
                    noteAdapter.notifyItemChanged(index);
                    break;
                case REQUEST_ADD_NOTE:
                    note = data.getParcelableExtra("note");
                    noteList.add(note);
                    noteAdapter.notifyItemInserted(noteList.size());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public int optionsMenu() {
        return R.menu.menu_notebook;
    }

    @Override
    public int optionsMenuItemColor() {
        return ColorsUtils.getReverseColor((int) noteBook.getColor());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialogUtils.show(getActivity(), "注意", "确认要删除<" + noteBook.getName() + ">吗?(此过程不可逆!)", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {
                        NoteBookDAO.getInstance(getActivity()).delete(noteBook);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.refreshNotebookSubMenu();
                        mainActivity.setDefaultFragment();
                    }
                }, null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
