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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.EditNoteActivity;
import com.ruffneck.cloudnote.activity.EditNoteBookActivity;
import com.ruffneck.cloudnote.activity.MainActivity;
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
    protected NoteAdapter noteAdapter;
    protected Note mChooseNote;

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


        noteList = NoteDAO.getInstance(getActivity()).queryByNoteBookId(noteBook.getId());
        noteAdapter = new NoteAdapter(noteList);

        initAdapter(noteAdapter);

        return contentView;

    }

    @Override
    public void onFabClick(FloatingActionButton fab) {
        Intent intent = new Intent(getActivity(), EditNoteActivity.class);
        intent.putExtra(DBConstants.Note.COLUMN_NOTEBOOK, noteBook.getId());
        startActivityForResult(intent, REQUEST_ADD_NOTE);
    }

    @Override
    public void initFab(FloatingActionButton fab) {
        fab.setImageResource(R.drawable.ic_add_white);
    }

    protected void initAdapter(NoteAdapter noteAdapter) {
        rvNote.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvNote.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvNote.setAdapter(this.noteAdapter);
        rvNote.setItemAnimator(new DefaultItemAnimator());
        this.noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Note note) {
                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                intent.putExtra("note", note);
                startActivityForResult(intent, REQUEST_EDIT_NOTE);
            }
        });
        this.noteAdapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, Note note) {
                //Show the popup menu.
                mChooseNote = note;
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_note, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onOptionsItemSelected(item);
                        return true;
                    }
                });
                popupMenu.show();
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
                if (noteBook.getId() == DBConstants.NoteBook.ID_DEFAULT_NOTEBOOK)
                    AlertDialogUtils.show(getActivity(), "注意", "默认笔记本不能删除!", "确认", null, null, null);
                else
                    AlertDialogUtils.show(getActivity(), "注意", "确认要删除<" + noteBook.getName() + ">吗?(此过程不可逆!)", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                        @Override
                        public void onOkClick(DialogInterface dialog, int which) {
                            NoteBookDAO.getInstance(getActivity()).delete(noteBook);
                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.refreshNotebookSubMenu();
                            mainActivity.setDefaultFragment();
                            Toast.makeText(getActivity(), "删除成功,所有笔记已经移动到回收站.", Toast.LENGTH_SHORT).show();
                        }
                    }, null);
                break;
            case R.id.action_edit:
                Intent intent = new Intent(getMainActivity(), EditNoteBookActivity.class);
                intent.putExtra("notebook", noteBook);
                getMainActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_EDIT_BOOK);
                break;
            case R.id.action_move_note:

                break;
            case R.id.action_delete_note:
                AlertDialogUtils.show(getActivity(), "注意", "是否把该笔记移动到回收站?", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {
                        NoteDAO.getInstance(getActivity()).moveToRecycleBin(mChooseNote);
                        removeNoteFromList();
                        Toast.makeText(getActivity(), "成功移到回收站.", Toast.LENGTH_SHORT).show();
                    }
                }, null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Remove the mChooseNote from the noteList.
     */
    protected void removeNoteFromList() {
        int index = noteList.indexOf(mChooseNote);
        noteList.remove(index);
        noteAdapter.notifyItemRemoved(index);
    }
}
