package com.ruffneck.cloudnote.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ruffneck.cloudnote.AttachAdapter;
import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.models.note.attach.ImageAttach;
import com.ruffneck.cloudnote.models.note.attach.Note;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewActivity extends BaseActivity {


    private static final int REQUEST_IMAGE = 0x000001;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.til_title)
    TextInputLayout tilTitle;
    @InjectView(R.id.cardview)
    CardView cardview;
    @InjectView(R.id.til_content)
    TextInputLayout tilContent;
    @InjectView(R.id.iv_alarm)
    ImageView ivAlarm;
    @InjectView(R.id.group_alarm)
    RelativeLayout groupAlarm;
    @InjectView(R.id.rv_attach)
    RecyclerView rvAttach;

    private Note note = new Note();
    private AttachAdapter attachAdapter = new AttachAdapter(note);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.inject(this);
        initRecyclerViewAdapter();
    }


    /**
     * Initialize the attack recyclerView in the card.
     */
    private void initRecyclerViewAdapter() {
        rvAttach.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
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
            public void onItemClick(View view, int position) {
                Toast.makeText(NewActivity.this, note.getAttachList().get(position).localURL, Toast.LENGTH_SHORT).show();
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
                    System.out.println("pathUri = " + pathUri);
                    note.getAttachList().add(new ImageAttach(pathUri));
                    System.out.println("note.getAttachList() = " + note.getAttachList());
                    attachAdapter.notifyDataSetChanged();
                }

                cursor.close();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
