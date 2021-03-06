package com.ruffneck.cloudnote.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ruffneck.cloudnote.CustomApplication;
import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.activity.adapter.AttachAdapter;
import com.ruffneck.cloudnote.db.DBConstants;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.models.note.attach.ImageAttach;
import com.ruffneck.cloudnote.utils.AlertDialogUtils;
import com.ruffneck.cloudnote.utils.DateUtils;
import com.ruffneck.cloudnote.utils.FormatUtils;
import com.ruffneck.cloudnote.utils.PixelUtil;
import com.ruffneck.cloudnote.view.NoteRichEditor;
import com.ruffneck.cloudnote.view.NoteRichEditorHandler;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class EditNoteActivity extends BaseActivity {


    private static final int REQUEST_IMAGE = 0x000001;
    private static final int REQUEST_DOODLE = 0x000002;
    private static final int REQUEST_EDITOR_IMAGE = 0x000003;

    @InjectView(R.id.til_title)
    TextInputLayout tilTitle;
    @InjectView(R.id.richEditor)
    NoteRichEditor richEditor;
    @InjectView(R.id.iv_alarm)
    ImageView ivAlarm;
    @InjectView(R.id.tv_alarm)
    TextView tvAlarm;
    @InjectView(R.id.rv_attach)
    RecyclerView rvAttach;
    @InjectView(R.id.tv_note_create)
    TextView tvNoteCreate;
    @InjectView(R.id.tv_note_modify)
    TextView tvNoteModify;
    @InjectView(R.id.editorHandler)
    NoteRichEditorHandler mEditorHandler;


    @OnClick(R.id.group_alarm)
    void setAlarmDate(View view) {

        final Calendar calendar = Calendar.getInstance();
        if (note.getAlarm().getTime() < DateUtils.getCurrentDate().getTime()) {
            calendar.setTime(DateUtils.getCurrentDate());
        } else {
            calendar.setTime(note.getAlarm());
        }

//        calendar.add(Calendar.HOUR, 1);
        //used to test , add 10 second.
        calendar.add(Calendar.SECOND, 10);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //选完日期就要开始选择时间了
                new TimePickerDialog(EditNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        note.setAlarm(calendar.getTime());
                        updateAlarmView();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    @OnLongClick(R.id.group_alarm)
    boolean cancelAlarmDate(View view) {
        note.setAlarm(new Date(0));
        updateAlarmView();
        return true;
    }

    private Note note = null;
    private List<Attach> attachList;
    private AttachAdapter attachAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode());
        getWindow().setEnterTransition(new Slide());
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        initEditorHandler();
        initNoteInfo();
        initRecyclerViewAdapter();
        updateAlarmView();
    }

    private void initEditorHandler() {

        mEditorHandler.setRichEditor(richEditor);
        mEditorHandler.setOnImageClickListener(new NoteRichEditorHandler.OnImageClickListener() {
            @Override
            public void onImageClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_EDITOR_IMAGE);
            }
        });
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
            richEditor.setHtml(note.getContent());

            //Initialize the text view of the date : create and modify.
            tvNoteCreate.setText("创建于:" + FormatUtils.formatDate(note.getCreate()));
            tvNoteModify.setText("修改于:" + FormatUtils.formatDate(note.getModify()));
            System.out.println(richEditor.getHtml());
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
        Intent intent;
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
        note.setContent(richEditor.getHtml());
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
            public void onItemClick(View view, int postion) {
                Toast.makeText(EditNoteActivity.this, attachList.get(postion).getLocalURL(), Toast.LENGTH_SHORT).show();
                System.out.println(attachList.get(postion).getLocalURL());
            }
        });

        attachAdapter.setOnItemLongClickListener(new AttachAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialogUtils.show(EditNoteActivity.this, "注意", "确认要删除该附件吗?", "确认", "取消", new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {

                        Attach attach = attachList.remove(position);
                        attachAdapter.notifyDataSetChanged();
                        attachDAO.delete(attach);

                    }
                }, null);
            }
        });
    }

    /**
     * Update the alarm textView and the imageView
     */
    private void updateAlarmView() {

        if (note.getAlarm().getTime() > DateUtils.getCurrentDate().getTime()) {
            tvAlarm.setText("提醒:" + FormatUtils.formatDate(note.getAlarm()));
            ivAlarm.setImageResource(R.drawable.ic_alarm_clock);
        } else {
            tvAlarm.setText("点击添加提醒");
            ivAlarm.setImageResource(R.drawable.ic_alarm_add_black_18dp);
        }

        CustomApplication.getInstance().updateAlarm(note);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (data == null)
                return;
            switch (requestCode) {
                case REQUEST_IMAGE:
                    String pathUri = getPathUrl(data);
                    if (pathUri != null) {
                        Attach newAttach = new ImageAttach(pathUri, DBConstants.Type.TYPE_IMAGE, note.getId());
                        insertAttach(newAttach);
                    }
                    break;
                case REQUEST_DOODLE:
                    URI uri = (URI) data.getSerializableExtra("uri");
                    Attach newAttach = new ImageAttach(uri.getPath(), DBConstants.Type.TYPE_IMAGE, note.getId());
                    insertAttach(newAttach);
                    break;
                case REQUEST_EDITOR_IMAGE:
                    String imgUrl = getPathUrl(data);
                    if (imgUrl != null) {
                        mEditorHandler.insertImage("file://", imgUrl, "图片", PixelUtil.px2dp(mEditorHandler.getWidth()) - 30);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPathUrl(Intent data) {
        String pathUri = null;
        Uri queryUri = data.getData();
        Cursor cursor = getContentResolver().query(queryUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (cursor != null) {

            while (cursor.moveToNext()) {
                pathUri = cursor.getString(0);
            }
            cursor.close();
        }
        return pathUri;
    }

    private void insertAttach(Attach attach) {
        attachList.add(attach);
        attachAdapter.notifyItemInsertedEnd();
        attachDAO.insert(attach);
    }

}
