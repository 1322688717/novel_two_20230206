package cc.ixcc.noveltwo.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;

import cc.ixcc.noveltwo.R;

import java.util.ArrayList;

import butterknife.BindView;
import cc.ixcc.noveltwo.common.MyActivity;
import cc.ixcc.noveltwo.ui.adapter.ClassificationPagerAdapter;

public class WealActivity extends MyActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mStrings = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weal;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        mStrings.add(getString(R.string.classification_man));
        viewPager.setAdapter(new ClassificationPagerAdapter(getSupportFragmentManager(), mStrings, mFragmentList));
    }

    @Override
    protected void initData() {

    }
}