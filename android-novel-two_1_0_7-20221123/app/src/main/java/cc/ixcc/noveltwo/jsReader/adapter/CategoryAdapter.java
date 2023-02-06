package cc.ixcc.noveltwo.jsReader.adapter;

import android.view.View;
import android.view.ViewGroup;

import cc.ixcc.noveltwo.R;
import cc.ixcc.noveltwo.jsReader.page.TxtChapter;

/**
 * Created by newbiechen on 17-6-5.
 */

public class CategoryAdapter extends EasyAdapter<TxtChapter> {
    private int currentSelected = 0;
    private boolean mIsVip = false;
    private boolean night = false;

    public CategoryAdapter(boolean isVip) {
        super();
        mIsVip = isVip;
    }

    @Override
    protected IViewHolder<TxtChapter> onCreateViewHolder(int viewType) {
        return new CategoryHolder(mIsVip);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        CategoryHolder holder = (CategoryHolder) view.getTag();
        if (night){
            holder.mTvChapter.setTextColor(view.getContext().getResources().getColor(R.color.read_text_night_color));
            holder.mTvChapter.setBackgroundColor(view.getContext().getResources().getColor(R.color.nb_read_bg_night2));
        }else {
            holder.mTvChapter.setTextColor(view.getContext().getResources().getColor(R.color.black));
            holder.mTvChapter.setBackgroundColor(view.getContext().getResources().getColor(R.color.white));
        }
        if (position == currentSelected){
            holder.setSelectedChapter();
        }

        return view;
    }

    public void setChapter(int pos){
        currentSelected = pos;
        notifyDataSetChanged();
    }

    public void setNight(boolean night){
        this.night = night;
        notifyDataSetChanged();
    }

    public int getChapter(){
        return currentSelected;
    }
}
