package com.steve.surfacetv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.steve.surfacetv.R;
import com.steve.surfacetv.datasource.vo.DramaVo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class DramaListAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<DramaVo> mList;

    private OnDramaListItemClickListener listener;

    public DramaListAdapter(Context context, List<DramaVo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.recycleitem_drama, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DramaVo dataBean = mList.get(position);
        viewHolder.textName.setText(dataBean.getName());
        viewHolder.textRating.setText(String.valueOf(dataBean.getRating()));
        viewHolder.textCreatedAt.setText(dataBean.getCreatedAt());

        Glide.with(mContext)
                .load(dataBean.getThumb())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(viewHolder.imageThumb);

        viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClickListener(dataBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private final View itemView;

        private final ImageView imageThumb;
        private final TextView textName;
        private final TextView textRating;
        private final TextView textCreatedAt;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.imageThumb = itemView.findViewById(R.id.recycleitem_drama_image);
            this.textName = itemView.findViewById(R.id.recycleitem_drama_name);
            this.textRating = itemView.findViewById(R.id.recycleitem_drama_rating_text);
            this.textCreatedAt = itemView.findViewById(R.id.recycleitem_drama_created_at);
        }

        public View getItemView() {
            return this.itemView;
        }
    }

    public void setItemClickListener(OnDramaListItemClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public interface OnDramaListItemClickListener {
        void onItemClickListener(DramaVo dataBean);
    }
}
