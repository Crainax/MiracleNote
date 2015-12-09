package com.ruffneck.cloudnote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ruffneck.cloudnote.AttachAdapter;
import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.models.note.attach.ImageAttach;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewActivity extends BaseActivity {


    private static final int REQUEST_IMAGE = 0x000001;

    @InjectView(R.id.til_title)
    TextInputLayout tilTitle;
    @InjectView(R.id.til_content)
    TextInputLayout tilContent;
    @InjectView(R.id.iv_alarm)
    ImageView ivAlarm;
    @InjectView(R.id.group_alarm)
    RelativeLayout groupAlarm;
    @InjectView(R.id.rv_attach)
    RecyclerView rvAttach;


    private Note note = null;

    private List<Attach> attachList = new ArrayList<>();
    private AttachAdapter attachAdapter = new AttachAdapter(attachList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);


        initNoteInfo();

        initRecyclerViewAdapter();
    }

    private void initNoteInfo() {
        if (note == null) {
            note = new Note();
            long id = noteDAO.insert(note);
            note.setId(id);
            System.out.println("NewActivity.initNoteInfo");
        }
    }

    /**
     * Initialize the toolbar.
     */
    @Override
    protected void initToolbar() {
        super.initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialogUtils.show(NewActivity.this, "确认", "确认离开吗?", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }, null);
                break;
            case R.id.action_save:
                saveNote();
                noteDAO.update(note);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int setContentResId() {
        return R.layout.activity_new_note;
    }

    private void saveNote() {
        note.setTitle(tilTitle.getEditText().getText().toString());
        note.setContent(tilContent.getEditText().getText().toString());
        note.setCreate(new Date());
        note.setModify(new Date());
    }

    /**
     * Initialize the attack recyclerView in the card.
     */
    private void initRecyclerViewAdapter() {
        rvAttach.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvAttach.setAdapter(attachAdapter);
        attachAdapter.setOnMoreClickListener(new AttachAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
        attachAdapter.setOnItemClickListener(new AttachAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Attach attach) {
                Toast.makeText(NewActivity.this, attach.localURL, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri queryUri = data.getData();
            Cursor cursor = getContentResolver().query(queryUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {

                while (cursor.moveToNext()) {
                    String pathUri = cursor.getString(0);
//                    System.out.println("pathUri = " + pathUri);
                    ImageAttach newAttach = new ImageAttach(pathUri, DBConstants.Type.TYPE_IMAGE, note.getId());
                    attachList.add(newAttach);
//                    System.out.println("note.getAttachList() = " + note.getAttachList());
//                    attachAdapter.notifyDataSetChanged();
                    attachAdapter.notifyItemInsertedEnd();
                    attachDAO.insertUpdate(newAttach);
                }

                cursor.close();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
