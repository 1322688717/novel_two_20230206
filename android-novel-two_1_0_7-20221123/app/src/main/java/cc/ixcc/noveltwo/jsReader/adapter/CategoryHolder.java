package cc.ixcc.noveltwo.jsReader.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import cc.ixcc.noveltwo.R;
import cc.ixcc.noveltwo.jsReader.page.TxtChapter;


/**
 * Created by newbiechen on 17-5-16.
 */

public class CategoryHolder extends ViewHolderImpl<TxtChapter> {

    public TextView mTvChapter;
    public ImageView mIvPay;
    private boolean mIsVip = false;

    public CategoryHolder(boolean isVip) {
        super();
        mIsVip = isVip;
    }

    @Override
    public void initView() {
        mTvChapter = findById(R.id.category_tv_chapter);
        mIvPay = findById(R.id.iv_pay);
    }

    @Override
    public void onBind(TxtChapter value, int pos){
//        //棣栧厛鍒ゆ柇鏄惁璇ョ珷宸蹭笅杞�
//        Drawable drawable = null;
//
//        //鐩綍鏄剧ず璁捐鐨勬湁鐐逛笉濂斤紝闇€瑕侀潬鎴愬憳鍙橀噺鏄惁涓簄ull鏉ュ垽鏂€�
//        //濡傛灉娌℃湁閾炬帴鍦板潃琛ㄧず鏄湰鍦版枃浠�
//        if (value.getLink() == null){
//            drawable = ContextCompat.getDrawable(getContext(),R.drawable.selector_category_load);
//        }
//        else {
//            if (value.getBookId() != null
//                    && BookManager
//                    .isChapterCached(value.getBookId(),value.getTitle())){
//                drawable = ContextCompat.getDrawable(getContext(),R.drawable.selector_category_load);
//            }
//            else {
//                drawable = ContextCompat.getDrawable(getContext(), R.drawable.selector_category_unload);
//            }
//        }

//        mTvChapter.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        mTvChapter.setSelected(false);
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(),R.color.nb_text_default));
        mTvChapter.setText(value.getTitle());
        mIvPay.setVisibility(mIsVip? View.VISIBLE:(value.getCoin()==0?View.GONE:(value.isIs_pay()?View.GONE:View.VISIBLE)));
//        value.isIs_vip()
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category;
    }

    public void setSelectedChapter(){
        //mTvChapter.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        mTvChapter.setSelected(true);
    }

}
