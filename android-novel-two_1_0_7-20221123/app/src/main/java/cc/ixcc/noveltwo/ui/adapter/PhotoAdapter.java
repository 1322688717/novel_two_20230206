package cc.ixcc.noveltwo.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.noveltwo.R;
import cc.ixcc.noveltwo.common.MyAdapter;


import java.util.List;

import butterknife.BindView;

/**

 *    time   : 2019/07/24
 *    desc   : 图片选择适配器
 */
public final class PhotoAdapter extends MyAdapter<String, RecyclerView.ViewHolder> {

    private final List<String> mSelectPhoto;

    public PhotoAdapter(Context context, List<String> data) {
        super(context);
        mSelectPhoto = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.iv_photo_image)
        ImageView mImageView;
        @BindView(R.id.iv_photo_check)
        CheckBox mCheckBox;

        ViewHolder() {
            super(R.layout.item_photo);
        }

        @Override
        public void onBindView(int position) {
            Glide.with(getContext())
                    .load(getItem(position))
                    .into(mImageView);

            mCheckBox.setChecked(mSelectPhoto.contains(getItem(position)));
        }
    }

    @Override
    protected RecyclerView.LayoutManager generateDefaultLayoutManager(Context context) {
        return new GridLayoutManager(context, 3);
    }
}