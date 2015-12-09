package com.ruffneck.cloudnote.activity.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.MainActivity;
import com.ruffneck.cloudnote.db.NoteBookDAO;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NoteBookFragment extends Fragment {


    @InjectView(R.id.tv_notebook_name)
    TextView tvNotebookName;
    @InjectView(R.id.tv_notebook_detail)
    TextView tvNotebookDetail;
    @InjectView(R.id.notebook_color)
    View notebookColor;

    @OnClick(R.id.bt_delete_notebook)
    void deleteNoteBook(View view){
        AlertDialogUtils.show(getActivity(), "注意", "确认要删除<"+noteBook.getName()+">吗?(删除后可在回收站中恢复)", "确认", "取消", new AlertDialogUtils.OkCallBack() {
            @Override
            public void onOkClick(DialogInterface dialog, int which) {
                NoteBookDAO.getInstance(getActivity()).delete(noteBook);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.refreshNotebookSubMenu();
                mainActivity.setDefaultFragment();
            }
        },null);
    }

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

        tvNotebookName.setText(noteBook.getName());
        tvNotebookDetail.setText(noteBook.getDetail());
        if (noteBook.getColor() != 0) {
            notebookColor.setBackground(new ColorDrawable((int) noteBook.getColor()));
        }

        return contentView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        noteBook = bundle.getParcelable("notebook");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
