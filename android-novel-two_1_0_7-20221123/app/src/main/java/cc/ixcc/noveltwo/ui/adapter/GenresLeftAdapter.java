package cc.ixcc.noveltwo.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cc.ixcc.noveltwo.R;
import cc.ixcc.noveltwo.bean.StackRoomBookBean;
import cc.ixcc.noveltwo.bean.StackRoomItemBean;

public class GenresLeftAdapter extends RecyclerView.Adapter<GenresLeftAdapter.ViewHolder> {
    private List<StackRoomBookBean.ColumnBean> mDataList = new ArrayList<>();
    //用来标记选中的位置
    private int mCurrentPosition = 0;
    //左侧Item的点击事件
    private OnLeftItemClickListener mOnItemClickListener = null;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_genres_left, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StackRoomBookBean.ColumnBean mDataBean = mDataList.get(position);
        holder.mTextName.setText(mDataBean.getTitle());
        if (mCurrentPosition == position) {
            //选中的背景颜色和字体颜色
            holder.mTextName.setBackgroundColor(Color.parseColor("#33f55793"));
            holder.mTextName.setTextColor(Color.parseColor("#f55793"));
        } else {
            //未选中的背景颜色和字体颜色
            holder.mTextName.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.mTextName.setTextColor(Color.parseColor("#333333"));
        }
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null && mCurrentPosition != position) {
                mCurrentPosition = position;
                mOnItemClickListener.onLeftItemClick(mDataBean);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //设置数据
    public void setData(List<StackRoomBookBean.ColumnBean> mData) {
        if (mData != null) {
            mDataList.clear();
            mDataList.addAll(mData);
            notifyDataSetChanged();
        }
        if (mDataList.size() > 0) {
            mOnItemClickListener.onLeftItemClick(mDataList.get(mCurrentPosition));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextName = (TextView) itemView.findViewById(R.id.item_left_text_name);
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnLeftItemClickListener {
        void onLeftItemClick(StackRoomBookBean.ColumnBean item);
    }
}