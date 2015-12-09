package com.ruffneck.cloudnote.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;
import com.ruffneck.cloudnote.utils.SnackBarUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewBookActivity extends BaseActivity {

    @InjectView(R.id.til_new_name)
    TextInputLayout tilNewName;
    @InjectView(R.id.til_new_detail)
    TextInputLayout tilNewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
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
                        NewBookActivity.this.finish();
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
        NoteBook noteBook = new NoteBook();
        noteBook.setName(name);
        noteBook.setDetail(tilNewDetail.getEditText().getText().toString());
        noteBookDAO.insert(noteBook);
        setResult(RESULT_OK);
        finish();
    }
}
