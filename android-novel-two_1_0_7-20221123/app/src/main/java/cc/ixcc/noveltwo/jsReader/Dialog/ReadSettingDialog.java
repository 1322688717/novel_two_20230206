package cc.ixcc.noveltwo.jsReader.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.noveltwo.R;
import cc.ixcc.noveltwo.jsReader.adapter.PageStyleAdapter;
import cc.ixcc.noveltwo.jsReader.model.local.ReadSettingManager;
import cc.ixcc.noveltwo.jsReader.page.PageLoader;
import cc.ixcc.noveltwo.jsReader.page.PageMode;
import cc.ixcc.noveltwo.jsReader.page.PageStyle;
import cc.ixcc.noveltwo.jsReader.utils.BrightnessUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-18.
 */

public class ReadSettingDialog extends Dialog {
    private static final String TAG = "ReadSettingDialog";
    private static final int DEFAULT_TEXT_SIZE = 49;
    private static final int TEXT_SIZE_STRIDE = 5;
    private static final int TEXT_SIZE_MIN = 24;
    private static final int TEXT_SIZE_MAX = 80;

    private static final int DEFAULT_SPACING_SIZE = 25;
    private static final int SPACING_SIZE_STRIDE = 5;
    private static final int SPACING_SIZE_MIN = 10;
    private static final int SPACING_SIZE_MAX = 80;

    @BindView(R.id.read_setting_iv_brightness_minus)
    ImageView mIvBrightnessMinus;
    @BindView(R.id.read_setting_sb_brightness)
    SeekBar mSbBrightness;
    @BindView(R.id.read_setting_iv_brightness_plus)
    ImageView mIvBrightnessPlus;
    @BindView(R.id.read_setting_cb_brightness_auto)
    CheckBox mCbBrightnessAuto;
    @BindView(R.id.read_setting_tv_font_minus)
    TextView mTvFontMinus;
    @BindView(R.id.read_setting_tv_font)
    TextView mTvFont;
    @BindView(R.id.read_setting_tv_font_plus)
    TextView mTvFontPlus;
    @BindView(R.id.read_setting_cb_font_default)
    CheckBox mCbFontDefault;
    @BindView(R.id.read_setting_rg_page_mode)
    RadioGroup mRgPageMode;

    @BindView(R.id.read_setting_tv_spacing)
    TextView mTvSpacing;
    @BindView(R.id.read_setting_tv_spacing_minus)
    TextView mTvSpacingMinus;
    @BindView(R.id.read_setting_tv_spacing_plus)
    TextView mTvSpacingPlus;
    @BindView(R.id.read_setting_cb_spacing_default)
    CheckBox mCbSpacingDefault;

    @BindView(R.id.read_setting_rb_simulation)
    RadioButton mRbSimulation;
    @BindView(R.id.read_setting_rb_cover)
    RadioButton mRbCover;
    @BindView(R.id.read_setting_rb_slide)
    RadioButton mRbSlide;
    @BindView(R.id.read_setting_ll_menu)
    LinearLayout menu;
    @BindView(R.id.read_setting_rb_scroll)
    RadioButton mRbScroll;
    @BindView(R.id.read_setting_rb_none)
    RadioButton mRbNone;
    @BindView(R.id.read_setting_rv_bg)
    RecyclerView mRvBg;
    @BindView(R.id.fenge_bg_1)
    FrameLayout fengeBg1;
    @BindView(R.id.fenge_bg_2)
    FrameLayout fengeBg2;
    @BindView(R.id.fenge_1)
    View fenge1;
    @BindView(R.id.fenge_2)
    View fenge2;
//    @BindView(R.id.read_setting_tv_more)
//    TextView mTvMore;
    /************************************/
    private PageStyleAdapter mPageStyleAdapter;
    private ReadSettingManager mSettingManager;
    private PageLoader mPageLoader;
    private Activity mActivity;

    private PageMode mPageMode;
    private PageStyle mPageStyle;

    private int mBrightness;
    private int mTextSize;

    private boolean isBrightnessAuto;
    private boolean isTextDefault;
    private boolean isSpacingDefault;


    public ReadSettingDialog(@NonNull Activity activity, PageLoader mPageLoader) {
        super(activity, R.style.ReadSettingDialog);
        mActivity = activity;
        this.mPageLoader = mPageLoader;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jsread_setting);
        ButterKnife.bind(this);
        setUpWindow();
        initData();
        initWidget();
        initClick();
    }

    //设置Dialog显示的位置
    private void setUpWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    private void initData() {
        mSettingManager = ReadSettingManager.getInstance();

        isBrightnessAuto = mSettingManager.isBrightnessAuto();
        mBrightness = mSettingManager.getBrightness();
        mTextSize = mSettingManager.getTextSize();
        isTextDefault = mSettingManager.isDefaultTextSize();
        isSpacingDefault = mSettingManager.isDefaultSpacingSize();
        mPageMode = mSettingManager.getPageMode();
        mPageStyle = mSettingManager.getPageStyle();
    }

    private void initWidget() {
        mSbBrightness.setProgress(mBrightness);
        mTvFont.setText(mTextSize + "");
        mCbBrightnessAuto.setChecked(isBrightnessAuto);
        mCbFontDefault.setChecked(isTextDefault);
        mCbSpacingDefault.setChecked(isSpacingDefault);
        initPageMode();
        setUpAdapter();
    }

    private void setUpAdapter() {
        Drawable[] drawables = {
                getDrawable(R.color.nb_read_bg_1)
                , getDrawable(R.color.nb_read_bg_2)
                , getDrawable(R.color.nb_read_bg_3)
                , getDrawable(R.color.nb_read_bg_4)
                , getDrawable(R.color.nb_read_bg_5)
                , getDrawable(R.color.nb_read_bg_6)};

        mPageStyleAdapter = new PageStyleAdapter();
        mRvBg.setLayoutManager(new GridLayoutManager(getContext(), 6));
        mRvBg.setAdapter(mPageStyleAdapter);
        mPageStyleAdapter.refreshItems(Arrays.asList(drawables));

        mPageStyleAdapter.setPageStyleChecked(mPageStyle);
    }

    private void initPageMode() {
        switch (mPageMode) {
            case SIMULATION:
                mRbSimulation.setChecked(true);
                break;
            case COVER:
                mRbCover.setChecked(true);
                break;
            case SLIDE:
                mRbSlide.setChecked(true);
                break;
            case NONE:
                mRbNone.setChecked(true);
                break;
            case SCROLL:
                mRbScroll.setChecked(true);
                break;
        }
    }

    private Drawable getDrawable(int drawRes) {
        return ContextCompat.getDrawable(getContext(), drawRes);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void show() {
        super.show();
        nightOrDaytime(mSettingManager.isNightMode());
    }


    /** 判断当前皮肤 */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void nightOrDaytime(boolean isNight){
        if (isNight){
            setDrawableColor(menu,getContext().getColor(R.color.nb_read_bg_night2));

            setNightDrawableAndTextColor(mTvFontMinus);
            setNightDrawableAndTextColor(mTvFontPlus);
            setNightDrawableAndTextColor(mTvSpacingMinus);
            setNightDrawableAndTextColor(mTvSpacingPlus);

            setNightResAndTextColor(mRbSimulation);
            setNightResAndTextColor(mRbCover);
            setNightResAndTextColor(mRbNone);

            fengeBg1.setBackgroundColor(getContext().getColor(R.color.nb_read_bg_night3));
            fengeBg2.setBackgroundColor(getContext().getColor(R.color.nb_read_bg_night3));
            fenge1.setBackgroundColor(getContext().getColor(R.color.nb_read_bg_night2));
            fenge2.setBackgroundColor(getContext().getColor(R.color.nb_read_bg_night2));

            setDrawableColor(mRgPageMode,getContext().getColor(R.color.nb_read_bg_night3));

        }else {
            setDrawableColor(menu,getContext().getColor(R.color.nb_read_menu_bg));

            setDaytimeDrawableAndTextColor(mTvFontMinus);
            setDaytimeDrawableAndTextColor(mTvFontPlus);
            setDaytimeDrawableAndTextColor(mTvSpacingMinus);
            setDaytimeDrawableAndTextColor(mTvSpacingPlus);
            setDaytimeResAndTextColor(mRbSimulation);
            setDaytimeResAndTextColor(mRbCover);
            setDaytimeResAndTextColor(mRbNone);

            fengeBg1.setBackgroundColor(getContext().getColor(R.color.read_setting_bg));
            fengeBg2.setBackgroundColor(getContext().getColor(R.color.read_setting_bg));
            fenge1.setBackgroundColor(getContext().getColor(R.color.gray));
            fenge2.setBackgroundColor(getContext().getColor(R.color.gray));

            setDrawableColor(mRgPageMode, Color.parseColor("#e0e0e0"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setNightDrawableAndTextColor(TextView t){
        t.setTextColor(getContext().getColor(R.color.read_text_night_color));
        setDrawableColor(t,getContext().getColor(R.color.nb_read_bg_night3));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setNightResAndTextColor(TextView t){
        t.setTextColor(getContext().getColor(R.color.read_text_night_color));
        t.setBackgroundResource(R.drawable.shape_btn_read_setting_select_2);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDaytimeResAndTextColor(TextView t){
        t.setTextColor(getContext().getColor(R.color.black));
        t.setBackgroundResource(R.drawable.shape_btn_read_setting_normal);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDaytimeDrawableAndTextColor(TextView t){
        t.setTextColor(getContext().getColor(R.color.black));
        setDrawableColor(t, Color.parseColor("#e0e0e0"));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDrawableColor(View view,int color){
        Drawable tDrawable = view.getBackground();
        DrawableCompat.setTint(tDrawable,color);
        view.setBackground(tDrawable);
    }

    private void initClick() {
        //亮度调节
        mIvBrightnessMinus.setOnClickListener(
                (v) -> {
                    if (mCbBrightnessAuto.isChecked()) {
                        mCbBrightnessAuto.setChecked(false);
                    }
                    int progress = mSbBrightness.getProgress() - 1;
                    if (progress < 0) return;
                    mSbBrightness.setProgress(progress);
                    BrightnessUtils.setBrightness(mActivity, progress);
                }
        );
        mIvBrightnessPlus.setOnClickListener(
                (v) -> {
                    if (mCbBrightnessAuto.isChecked()) {
                        mCbBrightnessAuto.setChecked(false);
                    }
                    int progress = mSbBrightness.getProgress() + 1;
                    if (progress > mSbBrightness.getMax()) return;
                    mSbBrightness.setProgress(progress);
                    BrightnessUtils.setBrightness(mActivity, progress);
                    //设置进度
                    ReadSettingManager.getInstance().setBrightness(progress);
                }
        );

        mSbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (mCbBrightnessAuto.isChecked()) {
                    mCbBrightnessAuto.setChecked(false);
                }
                //设置当前 Activity 的亮度
                BrightnessUtils.setBrightness(mActivity, progress);
                //存储亮度的进度条
                ReadSettingManager.getInstance().setBrightness(progress);
            }
        });

        mCbBrightnessAuto.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        //获取屏幕的亮度
                        BrightnessUtils.setBrightness(mActivity, BrightnessUtils.getScreenBrightness(mActivity));
                    } else {
                        //获取进度条的亮度
                        BrightnessUtils.setBrightness(mActivity, mSbBrightness.getProgress());
                    }
                    ReadSettingManager.getInstance().setAutoBrightness(isChecked);
                }
        );

        //字体大小调节
        mTvFontMinus.setOnClickListener(
                (v) -> {
                    if (mCbFontDefault.isChecked()) {
                        mCbFontDefault.setChecked(false);
                    }
                    int fontSize = Integer.valueOf(mTvFont.getText().toString()) - TEXT_SIZE_STRIDE;
                    if (fontSize < TEXT_SIZE_MIN) return;
                    mTvFont.setText(fontSize + "");
                    mPageLoader.setTextSize(fontSize);
                }
        );

        mTvFontPlus.setOnClickListener(
                (v) -> {
                    if (mCbFontDefault.isChecked()) {
                        mCbFontDefault.setChecked(false);
                    }
                    int fontSize = Integer.valueOf(mTvFont.getText().toString()) + TEXT_SIZE_STRIDE;
                    if (fontSize > TEXT_SIZE_MAX) return;
                    mTvFont.setText(fontSize + "");
                    mPageLoader.setTextSize(fontSize);
                }
        );

        mCbFontDefault.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        // int fontSize = ScreenUtils.dpToPx(DEFAULT_TEXT_SIZE);
                        int fontSize = DEFAULT_TEXT_SIZE;
                        mTvFont.setText(fontSize + "");
                        mPageLoader.setTextSize(fontSize);
                    }
                }
        );

        //行距大小调节
        mTvSpacingMinus.setOnClickListener(
                (v) -> {
                    //行距减
                    if (mCbSpacingDefault.isChecked()) {
                        mCbSpacingDefault.setChecked(false);
                    }
                    int spacingSize = Integer.valueOf(mTvSpacing.getText().toString()) - SPACING_SIZE_STRIDE;
                    if (spacingSize < SPACING_SIZE_MIN) {
                        return;
                    }

                    mTvSpacing.setText(spacingSize + "");
                    mPageLoader.setSpacingSize(spacingSize);
                }
        );
        mTvSpacingPlus.setOnClickListener(
                (v) -> {
                    //行距增加
                    if (mCbSpacingDefault.isChecked()) {
                        mCbSpacingDefault.setChecked(false);
                    }
                    int spacingSize = Integer.valueOf(mTvSpacing.getText().toString()) + SPACING_SIZE_STRIDE;
                    if (spacingSize > SPACING_SIZE_MAX) return;
                    mTvSpacing.setText(spacingSize + "");
                    mPageLoader.setSpacingSize(spacingSize);
                }
        );
        mCbSpacingDefault.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
//                        int spacingSize = ScreenUtils.dpToPx(DEFAULT_SPACING_SIZE);
                        int spacingSize = DEFAULT_SPACING_SIZE;
                        mTvSpacing.setText(spacingSize + "");
                        mPageLoader.setSpacingSize(spacingSize);
                    }
                }
        );


        //Page Mode 切换
        mRgPageMode.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    PageMode pageMode;
                    switch (checkedId) {
                        case R.id.read_setting_rb_simulation:
                            pageMode = PageMode.SIMULATION;
                            break;
                        case R.id.read_setting_rb_cover:
                            pageMode = PageMode.COVER;
                            break;
                        case R.id.read_setting_rb_slide:
                            pageMode = PageMode.SLIDE;
                            break;
                        case R.id.read_setting_rb_scroll:
                            pageMode = PageMode.SCROLL;
                            break;
                        case R.id.read_setting_rb_none:
                            pageMode = PageMode.NONE;
                            break;
                        default:
                            pageMode = PageMode.SIMULATION;
                            break;
                    }
                    mPageLoader.setPageMode(pageMode);
                }
        );

        //背景的点击事件
        mPageStyleAdapter.setOnItemClickListener(
                (view, pos) -> mPageLoader.setPageStyle(PageStyle.values()[pos])
        );

        //更多设置
//        mTvMore.setOnClickListener(
//                (v) -> {
//                    Intent intent = new Intent(getContext(), MoreSettingActivity.class);
//                    mActivity.startActivityForResult(intent, ReadActivity.REQUEST_MORE_SETTING);
//                    //关闭当前设置
//                    dismiss();
//                }
//        );
    }

    public boolean isBrightFollowSystem() {
        if (mCbBrightnessAuto == null) {
            return false;
        }
        return mCbBrightnessAuto.isChecked();
    }
}
