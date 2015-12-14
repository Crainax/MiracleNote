package com.ruffneck.cloudnote.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruffneck.cloudnote.R;
import com.ruffneck.cloudnote.models.note.NoteBook;
import com.ruffneck.cloudnote.utils.ColorsUtils;

import java.util.List;
import java.util.Random;

public class AllNoteBookAdapter extends ImageLoaderAdapter<AllNoteBookAdapter.ViewHolder> {

    public static final int MARGIN_VIEW = 20;
    public static final int RANDOM_HEIGHT = 100;

    List<NoteBook> noteBookList;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public AllNoteBookAdapter(List<NoteBook> noteBookList){
        this.noteBookList = noteBookList;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NoteBook noteBook);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, NoteBook noteBook);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FrameLayout view = (FrameLayout)(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notebook, null, false));

        //Set the margin to emerge the cardview shadow.
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(MARGIN_VIEW,MARGIN_VIEW,MARGIN_VIEW,MARGIN_VIEW);
        view.setPadding(0,0,0,new Random().nextInt(100));
        view.setLayoutParams(layoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int color = (int) noteBookList.get(position).getColor();
        int reverseColor = ColorsUtils.getReverseColor(color);

        holder.ivNotebook.setColorFilter(color);

        //set the staggered effect.
        int random = new Random().nextInt(RANDOM_HEIGHT);
        holder.fl.setPadding(0,random,0,random);

        holder.tvTitle.setTextColor(reverseColor);
//        holder.cvNotebook.setCardBackgroundColor(reverseColor);
        holder.tvTitle.setText(noteBookList.get(position).getName());

        bindClickListener(holder.itemView, position);
    }

    private void bindClickListener(View itemView, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(v, noteBookList.get(position));
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null)
                    onItemLongClickListener.onItemLongClick(v, noteBookList.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteBookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivNotebook;
        private final TextView tvTitle;
        private final FrameLayout fl;

        public ViewHolder(View view) {
            super(view);
            ivNotebook = (ImageView) view.findViewById(R.id.iv_notebook);
            tvTitle = (TextView) view.findViewById(R.id.tv_notebook_title);
            fl = (FrameLayout) view.findViewById(R.id.fl);
        }
    }
}
