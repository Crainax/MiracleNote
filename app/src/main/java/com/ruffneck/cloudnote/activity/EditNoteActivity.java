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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.adapter.AttachAdapter;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.models.note.attach.ImageAttach;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;
import com.ruffneck.cloudnote.utils.FormatUtils;

import java.net.URI;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditNoteActivity extends BaseActivity {


    private static final int REQUEST_IMAGE = 0x000001;
    private static final int REQUEST_DOODLE = 0x000002;


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
    @InjectView(R.id.tv_note_create)
    TextView tvNoteCreate;
    @InjectView(R.id.tv_note_modify)
    TextView tvNoteModify;


    private Note note = null;

    private List<Attach> attachList;
    private AttachAdapter attachAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        initNoteInfo();

        initRecyclerViewAdapter();
    }

    private void initNoteInfo() {

        Intent intent = getIntent();
        note = intent.getParcelableExtra("note");

        if (note == null) {
            note = new Note();
            long id = noteDAO.insert(note);
            note.setId(id);
            tvNoteCreate.setVisibility(View.GONE);
            tvNoteModify.setVisibility(View.GONE);

            //set the what the note belong to.

            note.setNotebook(intent.getLongExtra(DBConstants.Note.COLUMN_NOTEBOOK, 1));
        } else {
            tilTitle.getEditText().setText(note.getTitle());
            tilContent.getEditText().setText(note.getContent());

            //Initialize the text view of the date : create and modify.
            tvNoteCreate.setText("创建于:" + FormatUtils.formatDate(note.getCreate()));
            tvNoteModify.setText("修改于:" + FormatUtils.formatDate(note.getModify()));

        }

        attachList = attachDAO.queryByNoteId(note.getId());

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
        Intent intent = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialogUtils.show(EditNoteActivity.this, "确认", "确认离开吗?", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }, null);
                break;
            case R.id.action_save:
                saveNote();
                noteDAO.update(note);
                intent = new Intent();
                intent.putExtra("note", note);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.action_attach_image:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.action_attach_doodle:
                intent = new Intent(this, DoodleActivity.class);
                startActivityForResult(intent, REQUEST_DOODLE);
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
        if (note.getCreate() == null)
            note.setCreate(new Date());
        note.setModify(new Date());
    }

    /**
     * Initialize the attack recyclerView in the card.
     */
    private void initRecyclerViewAdapter() {
        attachAdapter = new AttachAdapter(attachList);

        rvAttach.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvAttach.setAdapter(attachAdapter);
        attachAdapter.setOnMoreClickListener(new AttachAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClick(View view) {

                //show the popup menu.
//                int gravity = Gravity.CENTER;
                PopupMenu popupMenu = new PopupMenu(EditNoteActivity.this, view, Gravity.CENTER);

                getMenuInflater().inflate(R.menu.menu_attach_list, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        EditNoteActivity.this.onOptionsItemSelected(item);
                        return true;
                    }
                });

                popupMenu.show();

            }
        });
        attachAdapter.setOnItemClickListener(new AttachAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Attach attach) {
                Toast.makeText(EditNoteActivity.this, attach.localURL, Toast.LENGTH_SHORT).show();
            }
        });

        attachAdapter.setOnItemLongClickListener(new AttachAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final Attach attach) {
                AlertDialogUtils.show(EditNoteActivity.this, "注意", "确认要删除该附件吗?", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {

                        int index = attachList.indexOf(attach);
                        attachList.remove(index);
                        attachAdapter.notifyItemRemoved(index);
                        attachDAO.delete(attach);

                    }
                }, null);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    if (data == null)
                        return;

                    Uri queryUri = data.getData();
                    Cursor cursor = getContentResolver().query(queryUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (cursor != null) {

                        while (cursor.moveToNext()) {
                            String pathUri = cursor.getString(0);
//                    System.out.println("pathUri = " + pathUri);
                            Attach newAttach = new ImageAttach(pathUri, DBConstants.Type.TYPE_IMAGE, note.getId());
                            insertAttach(newAttach);
                        }

                        cursor.close();
                    }
                    break;
                case REQUEST_DOODLE:
                    if (data == null)
                        return;

                    URI uri = (URI) data.getSerializableExtra("uri");
                    Attach newAttach = new ImageAttach(uri.getPath(), DBConstants.Type.TYPE_IMAGE, note.getId());
                    insertAttach(newAttach);

                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insertAttach(Attach attach) {
        attachList.add(attach);
        attachAdapter.notifyItemInsertedEnd();
        attachDAO.insert(attach);
    }

}
