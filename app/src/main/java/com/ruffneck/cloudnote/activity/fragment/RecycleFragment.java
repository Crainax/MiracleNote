package com.ruffneck.cloudnote.activity.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.adapter.NoteAdapter;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.db.NoteDAO;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;

public class RecycleFragment extends NoteBookFragment {

    public static RecycleFragment newInstance(NoteBook noteBook) {
        RecycleFragment recycleFragment = new RecycleFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("notebook", noteBook);
        recycleFragment.setArguments(bundle);
        return recycleFragment;
    }

    @Override
    public void initFab(FloatingActionButton fab) {
        fab.setImageResource(R.drawable.ic_delete_black_24dp);
        fab.setColorFilter(Color.WHITE);
    }

    @Override
    public void onFabClick(FloatingActionButton fab) {

    }

    @Override
    public int optionsMenu() {
        return 0;
    }

    @Override
    protected void initAdapter(NoteAdapter noteAdapter) {
        super.initAdapter(noteAdapter);
        this.noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Note note) {
                //Show the popup menu.
                mChooseNote = note;
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_recycle, popupMenu.getMenu());
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
        this.noteAdapter.setOnItemLongClickListener(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_note_restore:
                AlertDialogUtils.show(getActivity(), "注意", "是否还原该笔记?", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {
                        long noteBookId = NoteDAO.getInstance(getActivity()).restoreFromRecycleBin(mChooseNote);
                        NoteBook noteBook = NoteBookDAO.getInstance(getActivity()).queryById(noteBookId);
                        removeNoteFromList();

                        assert noteBook != null;
                        Toast.makeText(getActivity(), "成功还原到:" + noteBook.getName() + ".", Toast.LENGTH_SHORT).show();
                    }
                }, null);

                break;
            case R.id.action_note_delete_forever:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
