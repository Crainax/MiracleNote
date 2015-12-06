package com.ruffneck.cloudnote;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ruffneck.cloudnote.models.note.attach.Attach;
import com.ruffneck.cloudnote.models.note.attach.Note;

import java.util.List;

public class AttachAdapter extends RecyclerView.Adapter<AttachAdapter.ViewHolder> {

    private List<Attach> attachList;
    private OnMoreClickListener onMoreClickListener;
    private OnMoreLongClickListener onMoreLongClickListener;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private ImageLoader loader;
    private DisplayImageOptions options;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface OnMoreClickListener {
        void onMoreClick(View view);
    }

    public interface OnMoreLongClickListener {
        void onMoreLongClick(View view);
    }

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        this.onMoreClickListener = onMoreClickListener;
    }

    public void setOnMoreLongClickListener(OnMoreLongClickListener onMoreLongClickListener) {
        this.onMoreLongClickListener = onMoreLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public AttachAdapter(List<Attach> attachList) {
        this.attachList = attachList;
        initImageLoader();
    }

    public AttachAdapter(Note note) {
        this.attachList = note.getAttachList();
        initImageLoader();
    }

    /**
     * Initialize the image loader.
     */
    private void initImageLoader() {
        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_loading)
                .showImageOnFail(R.drawable.pic_loading)
                .showImageOnFail(R.drawable.pic_loading)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attach, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == attachList.size()) {
            loader.displayImage("drawable://"+R.drawable.ic_add_pic,holder.imageView,options);
        } else {
//            int maxSize = 1024 * 1024 * 100;
//            Picasso picasso = new Picasso.Builder(holder.itemView.getContext())
//                    .memoryCache(new LruCache(maxSize))
//                    .build();

            loader.displayImage("file://"+attachList.get(position).localURL,holder.imageView,options);
        }

        bindClickListener(holder.itemView, position);
    }


    /**
     * Bind the onclick listener to the view.
     *
     * @param itemView
     * @param position
     */
    private void bindClickListener(View itemView, final int position) {
        if (position == attachList.size()) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMoreClickListener != null)
                        onMoreClickListener.onMoreClick(v);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onMoreLongClickListener != null)
                        onMoreLongClickListener.onMoreLongClick(v);
                    return true;
                }
            });
        } else {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return attachList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View ImageView) {
            super(ImageView);
            imageView = (ImageView) ImageView.findViewById(R.id.iv_album);
        }
    }
}
