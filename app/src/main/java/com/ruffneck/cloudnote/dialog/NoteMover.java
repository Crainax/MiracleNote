package com.ruffneck.cloudnote.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.models.note.NoteBook;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A dialog can provide which notebook you need to move the note to.
 */
public class NoteMover extends Dialog {

    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    @InjectView(R.id.spinner_move)
    Spinner spinnerMove;

    @OnClick(R.id.bt_cancel)
    void OnCancel(View view) {
        this.dismiss();
    }

    public interface OnConfirmListener {
        void onConfirm(NoteBook noteBook);
    }

    private OnConfirmListener onConfirmListener;
    private List<NoteBook> noteBookList;
    private NoteBook noteBook;

    public NoteMover(Context context, NoteBook noteBook, OnConfirmListener onConfirmListener) {
        super(context);
        this.onConfirmListener = onConfirmListener;
        this.noteBook = noteBook;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notemover);
        ButterKnife.inject(this);
        initSpinner();
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoteMover.this.onConfirmListener != null) {
                    onConfirmListener.onConfirm(noteBook);
                    NoteMover.this.dismiss();
                }
            }
        });
    }

    private void initSpinner() {
        noteBookList = NoteBookDAO.getInstance(getContext()).queryAllNoteBook();

        String[] names = new String[noteBookList.size()];
        for (int i = 0; i < noteBookList.size(); i++) {
            names[i] = noteBookList.get(i).getName();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
        spinnerMove.setAdapter(arrayAdapter);
//        spinnerMove.setPrompt("选择移动到的笔记本");
        spinnerMove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noteBook = noteBookList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int index = noteBookList.indexOf(noteBook);
        spinnerMove.setSelection(index, true);
    }
}
