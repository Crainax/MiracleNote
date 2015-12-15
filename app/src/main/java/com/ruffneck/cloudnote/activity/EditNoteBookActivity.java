package com.ruffneck.cloudnote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.dialog.ColorPicker;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;
import com.ruffneck.cloudnote.utils.ColorsUtils;
import com.ruffneck.cloudnote.utils.SnackBarUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class EditNoteBookActivity extends BaseActivity {

    @InjectView(R.id.til_new_name)
    TextInputLayout tilNewName;
    @InjectView(R.id.til_new_detail)
    TextInputLayout tilNewDetail;
    @InjectView(R.id.tv_choose_color)
    TextView tvChooseColor;

    //The notebook's color.
    private int color = Color.BLUE;
    private NoteBook noteBook;

    @OnClick(R.id.tv_choose_color)
    void chooseColor(View view) {
        ColorPicker colorPicker = new ColorPicker(this, color, new ColorPicker.OnConfirmListener() {
            @Override
            public void onConfirm(int color) {
                setColor(color);
            }
        });
        colorPicker.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        initNoteBook();

        tvChooseColor.setBackground(new ColorDrawable(color));
    }

    private void initNoteBook() {
        noteBook = getIntent().getParcelableExtra("notebook");

        if (noteBook == null) {
            noteBook = new NoteBook();
        } else {
            tilNewName.getEditText().setText(noteBook.getName());
            tilNewDetail.getEditText().setText(noteBook.getDetail());
            setColor((int) noteBook.getColor());
            getToolbar().setTitle("编辑笔记本");
        }

    }

    private void setColor(int color) {
        EditNoteBookActivity.this.color = color;
        int reverseColor = ColorsUtils.getReverseColor(color);
        tvChooseColor.setBackground(new ColorDrawable(color));
        tvChooseColor.setTextColor(reverseColor);
    }

    @Override
    protected int setContentResId() {
        return R.layout.fragment_addnewnotebook;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        getToolbar().setTitle("新建笔记本");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                saveData();
                break;
            case android.R.id.home:
                AlertDialogUtils.show(this, "确认", "确认离开么?离开后内容将不保存", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {
                        EditNoteBookActivity.this.finish();
                    }
                }, null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        String name = tilNewName.getEditText().getText().toString();
        if (TextUtils.isEmpty(name)) {
            SnackBarUtils.showSnackBar(tilNewDetail, "标题不能为空!", 3000, "OK");
            return;
        }
        noteBook.setName(name);
        noteBook.setDetail(tilNewDetail.getEditText().getText().toString());
        noteBook.setColor(color);
        noteBookDAO.insertUpdate(noteBook);
        Intent data = new Intent();
        data.putExtra("notebook", noteBook);
        setResult(RESULT_OK, data);
        finish();
    }
}
