package com.ruffneck.cloudnote.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.models.note.Note;
import com.ruffneck.cloudnote.utils.FormatUtils;

import java.util.List;

public class NoteAdapter extends ImageLoaderAdapter<NoteAdapter.ViewHolder> {

    private List<Note> noteList;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Note note);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, Note note);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_info, null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Note note = noteList.get(position);

        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        holder.tvUpdate.setText("最后更新:"+FormatUtils.formatDate(note.getModify()));

        bindClickListener(holder.itemView, position);
    }

    private void bindClickListener(View itemView, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(v, noteList.get(position));
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null)
                    onItemLongClickListener.onItemLongClick(v, noteList.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvContent;
        private final TextView tvUpdate;
        private final ImageView iv;
        private final TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_note_info_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_note_info_content);
            tvUpdate = (TextView) itemView.findViewById(R.id.tv_note_info_update);
            iv = (ImageView) itemView.findViewById(R.id.iv_note_info);
        }
    }

}
