package cc.ixcc.noveltwo.jsReader.Activity;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import cc.ixcc.noveltwo.Constants;
import cc.ixcc.noveltwo.R;

import cc.ixcc.noveltwo.bean.BookDetailBean;
import cc.ixcc.noveltwo.bean.PayChapterBean;
import cc.ixcc.noveltwo.bean.RewordBean;
import cc.ixcc.noveltwo.bean.RewordRuleBean;
import cc.ixcc.noveltwo.bean.UserBean;
import cc.ixcc.noveltwo.http.AllApi;
import cc.ixcc.noveltwo.http.HttpCallback;
import cc.ixcc.noveltwo.http.HttpClient;
import cc.ixcc.noveltwo.jsReader.Contract.ReadContract;
import cc.ixcc.noveltwo.jsReader.Dialog.ReadSettingDialog;
import cc.ixcc.noveltwo.jsReader.Dialog.VipPageDialog;
import cc.ixcc.noveltwo.jsReader.adapter.CategoryAdapter;
import cc.ixcc.noveltwo.jsReader.model.bean.BookChapterBean;
import cc.ixcc.noveltwo.jsReader.model.bean.BookRecordBean;
import cc.ixcc.noveltwo.jsReader.model.bean.CollBookBean;
import cc.ixcc.noveltwo.jsReader.model.local.BookRepository;
import cc.ixcc.noveltwo.jsReader.model.local.ReadSettingManager;
import cc.ixcc.noveltwo.jsReader.page.PageLoader;
import cc.ixcc.noveltwo.jsReader.page.PageView;
import cc.ixcc.noveltwo.jsReader.page.TxtChapter;
import cc.ixcc.noveltwo.jsReader.presenter.ReadPresenter;
import cc.ixcc.noveltwo.jsReader.utils.BitmapUtils;
import cc.ixcc.noveltwo.jsReader.utils.BrightnessUtils;
import cc.ixcc.noveltwo.jsReader.utils.Constant;
import cc.ixcc.noveltwo.jsReader.utils.LogUtils;
import cc.ixcc.noveltwo.jsReader.utils.ScreenUtils;
import cc.ixcc.noveltwo.jsReader.utils.StringUtils;
import cc.ixcc.noveltwo.jsReader.utils.SystemBarUtils;
import cc.ixcc.noveltwo.jsReader.utils.ToastUtils;
import cc.ixcc.noveltwo.treader.util.AppUtils;
import cc.ixcc.noveltwo.ui.activity.my.ThirdLoginActivity;
import cc.ixcc.noveltwo.ui.activity.my.TopUpActivity;
import cc.ixcc.noveltwo.ui.activity.my.vip.OpenVipActivity;
import cc.ixcc.noveltwo.ui.dialog.AddShelveDialog;
import cc.ixcc.noveltwo.ui.dialog.RewordBottomDialog;
import cc.ixcc.noveltwo.ui.dialog.TipDialog;
import cc.ixcc.noveltwo.utils.BrightnessUtil;
import cc.ixcc.noveltwo.utils.GsonUtils;
import cc.ixcc.noveltwo.utils.KtUtils;
import cc.ixcc.noveltwo.utils.SpUtil;
import cc.ixcc.noveltwo.utils.StringUtil;

import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.ixcc.noveltwo.view.DialogView;

import static android.view.View.GONE;
import static android.view.View.LAYER_TYPE_SOFTWARE;
import static android.view.View.VISIBLE;

import org.jetbrains.annotations.NotNull;

/**
 * Created by newbiechen on 17-5-16.
 */
public class JsReadActivity extends BaseMVPActivity<ReadContract.Presenter> implements ReadContract.View {
    private static final String TAG = "JsReadActivity";
    public static final int REQUEST_MORE_SETTING = 1;
    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";
    public static final String START_CHAPTER = "start_chapter";
    public static final String SHELVE = "is_shelve";
    public static final String ISCATALOGUE = "is_catalogue";

    // ?????? Brightness ??? uri
    private final Uri BRIGHTNESS_MODE_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI = Settings.System.getUriFor("screen_auto_brightness_adj");

    private static final int WHAT_CATEGORY = 1;
    private static final int WHAT_CHAPTER = 2;

    @BindView(R.id.read_dl_slide)
    DrawerLayout mDlSlide;
    /*************top_menu_view*******************/
    @BindView(R.id.read_abl_top_menu)
    AppBarLayout mAblTopMenu;
    /***************content_view******************/
    @BindView(R.id.read_pv_page)
    PageView mPvPage;
    /***************bottom_menu_view***************************/
    @BindView(R.id.read_tv_page_tip)
    TextView mTvPageTip;

    @BindView(R.id.read_ll_bottom_menu)
    LinearLayout mLlBottomMenu;
    @BindView(R.id.read_tv_pre_chapter)
    TextView mTvPreChapter;
    @BindView(R.id.read_sb_chapter_progress)
    SeekBar mSbChapterProgress;
    @BindView(R.id.read_tv_next_chapter)
    TextView mTvNextChapter;
    @BindView(R.id.read_tv_category)
    TextView mTvCategory;
    @BindView(R.id.read_tv_night_mode)
    TextView mTvNightMode;
    @BindView(R.id.flContainer)
    LinearLayout mFlContainer;

    @BindView(R.id.read_tv_dashang)
    TextView ReadtvDashang;

    @BindView(R.id.read_tv_setting)
    TextView mTvSetting;
    /***************left slide*******************************/
    @BindView(R.id.read_iv_category)
    ListView mLvCategory;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tvBookName)
    TextView tvBookName;
    @BindView(R.id.tvAuth)
    TextView tvAuth;
    @BindView(R.id.tvTotalPage)
    TextView tvTotalPage;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_listen)
    ImageView ivLister;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.ad_show_bt)
    TextView showAd;

    @BindView(R.id.ad_parent)
    ConstraintLayout adParent;

    @BindView(R.id.sideslip)
    LinearLayout sideslip;
    private boolean order = true;
    /*****************view******************/
    private ReadSettingDialog mSettingDialog;
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CategoryAdapter mCategoryAdapter;
    private CollBookBean mCollBook;
    private boolean isShelve;
    private int StartChapter;
    private TxtChapter mStartChapter;
    private long startReadTime;
    private Context mContext;
    private MMKV mmkv = MMKV.defaultMMKV();
    private UserBean mInfoBean;
    private VipPageDialog vipPageDialog;
    private Bitmap adBitmap;
    private List<Bitmap> adBitmapList = new ArrayList<>();


    private UserBean userBean1;

    private int readMills = 0;
    private CountDownTimer cdt = new CountDownTimer(2400000000L, 1000L) {
        @Override
        public void onTick(long millisUntilFinished) {
            readMills++;
        }

        @Override
        public void onFinish() {

        }
    };

    //??????????????????
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_CATEGORY:
                    mLvCategory.setSelection(mPageLoader.getChapterPos());
                    break;
                case WHAT_CHAPTER:
                    mPageLoader.openChapter();
                    break;
            }
        }
    };

    // ??????????????????????????????????????????
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageLoader.updateBattery(level);
            }
            // ?????????????????????
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                mPageLoader.updateTime();
            }
        }
    };

    // ???????????????????????? Broadcast ?????????????????? ContentProvider ?????????????????????????????? Observer ????????? ContentProvider ??????????????????
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            // ????????????????????????????????????????????????????????????
            if (selfChange || !mSettingDialog.isBrightFollowSystem()) return;

            // ?????????????????????????????????????????? Activity ??????
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "??????????????????");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(JsReadActivity.this)) {
                Log.d(TAG, "??????????????????????????? ?????????");
                BrightnessUtils.setBrightness(JsReadActivity.this, BrightnessUtils.getScreenBrightness(JsReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(JsReadActivity.this)) {
                Log.d(TAG, "??????????????????????????? ?????????");
                BrightnessUtils.setDefaultBrightness(JsReadActivity.this);
            } else {
                Log.d(TAG, "???????????? ??????");
            }
        }
    };

    /***************params*****************/
    private boolean isCollected = false; // isFromSDCard
    private boolean is = false; // isFromSDCard
    private boolean isNightMode = false;
    private boolean isFullScreen = false;
    private boolean isRegistered = false;
    private String mBookId;
    private boolean isFirstLoadAd = true;

    public static JsReadActivity jsReadActivity = null;

    public static void startActivity(Context context, CollBookBean collBook, boolean isCollected) {
        Log.e("tagfuck", "isCollected: " + isCollected + " ,startChapter: ");
        context.startActivity(new Intent(context, JsReadActivity.class)
                .putExtra(EXTRA_IS_COLLECTED, isCollected)
                .putExtra(EXTRA_COLL_BOOK, collBook));
    }

    public static void startActivity(Context context, CollBookBean collBook, boolean isCollected, int startChapter, String shelve, boolean isCatalogue) {
        Log.e("tagfuck", "isCollected: " + isCollected + " ,startChapter: " + startChapter);
        context.startActivity(new Intent(context, JsReadActivity.class)
                .putExtra(EXTRA_IS_COLLECTED, isCollected)
                .putExtra(EXTRA_COLL_BOOK, collBook)
                .putExtra(START_CHAPTER, startChapter)
                .putExtra(SHELVE, shelve)
                .putExtra(ISCATALOGUE, isCatalogue)
        );
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_js_read;
    }

    @Override
    protected ReadContract.Presenter bindPresenter() {
        return new ReadPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //??????????????????
        mCollBook = getIntent().getParcelableExtra(EXTRA_COLL_BOOK);
        isCollected = getIntent().getBooleanExtra(EXTRA_IS_COLLECTED, false);
        StartChapter = getIntent().getIntExtra(START_CHAPTER, 0);
        isShelve = TextUtils.equals(getIntent().getStringExtra(SHELVE), "1");
        isNightMode = ReadSettingManager.getInstance().isNightMode();
        isFullScreen = ReadSettingManager.getInstance().isFullScreen();
        mBookId = mCollBook.get_id();

        if (mContext == null) {
            mContext = this;
        }
        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        inquireCoin();

    }


    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        //????????????
        toolbar.setTitle(mCollBook.getTitle());
        tvTitleName.setText(mCollBook.getTitle());
        //????????????StatusBar
        SystemBarUtils.transparentStatusBar(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mFlContainer.setVisibility(GONE);

        ivLister.setVisibility(Constants.IV_SHOW_LISTERVIEW ? VISIBLE : GONE);
        startReadTime = System.currentTimeMillis();
//        AdMobManager.GetInstance().SetOnUserEarnedRewardListener(new AdMobManager.OnUserEarnedRewardListener() {
//            @Override
//            public void onUserEarnedReward() {
//                adParent.setVisibility(GONE);
//                Log.e("Ad_chapter_change", "????????????");
//            }
//        });

        adParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AdMobManager.GetInstance().showRewardedVideo(JsReadActivity.this);
            }
        });
        // ?????? API < 18 ??????????????????
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPvPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        //?????????????????????
        mPageLoader = mPvPage.getPageLoader(mCollBook, StartChapter);

        for (int num = 0; num < mCollBook.getBookChapters().size(); num++) {

            Log.d(TAG, "initWidget: " + mCollBook.getBookChapters().get(num).getIs_pay());

        }

        for (int num = 0; num < mPageLoader.getChapterCategory().size(); num++) {

            Log.d(TAG, "initWidget: " + mPageLoader.getChapterCategory().get(num).isIs_pay());

        }


        //??????????????????DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //??????????????????????????????????????????
        mDlSlide.setFocusableInTouchMode(false);
        mSettingDialog = new ReadSettingDialog(this, mPageLoader);

        setUpCategory();
        setUpAdapter();

        //???????????????????????????
        toggleNightMode();

        //????????????
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

        //????????????Activity???Brightness
        //todo ??????
        BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());

        //????????????????????????
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "ireader:keep bright");

        //??????StatusBar
        mPvPage.post(() -> hideSystemBar()
        );

        //?????????TopMenu
        initTopMenu();

        //?????????BottomMenu
        initBottomMenu();
    }

    //???????????????
    private void setUpCategory() {
        Glide.with(this).load(mCollBook.getCoverPic()).into(ivCover);
        tvBookName.setText(mCollBook.getTitle());
        tvTitleName.setText(mCollBook.getTitle());
        tvAuth.setText(mCollBook.getAuthor());
        tvTotalPage.setText((mCollBook.getIswz() == Constants.lianzai ? "Serialize" : "over") + " " + " " + mCollBook.getAllchapter() + " chapter");
        tvOrder.setText(order ? "Reverse order" : "Positive order");
    }

    private void initTopMenu() {
        if (Build.VERSION.SDK_INT >= 19) {
            mAblTopMenu.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        }
    }

    private void initBottomMenu() {
        //??????????????????
        if (ReadSettingManager.getInstance().isFullScreen()) {
            //???????????????mBottomMenu???????????????
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
            mLlBottomMenu.setLayoutParams(params);
        } else {
            //??????mBottomMenu???????????????
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
            params.bottomMargin = 0;
            mLlBottomMenu.setLayoutParams(params);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: " + mAblTopMenu.getMeasuredHeight());
    }

    private void toggleNightMode() {
        if (isNightMode) {
            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_morning));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_morning);
            mTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            night();
        } else {
            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_night));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_read_menu_night);
            mTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            daytime();
        }
    }

    private void setUpAdapter() {
        UserBean bean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        boolean b = false;
        if (bean != null) {
            b = TextUtils.equals(bean.getIs_vip(), "1");
        }

        mCategoryAdapter = new CategoryAdapter(b);
        mLvCategory.setAdapter(mCategoryAdapter);
        mLvCategory.setFastScrollEnabled(true);
    }

    // ?????????????????????
    private void registerBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (!isRegistered) {
                    final ContentResolver cr = getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    isRegistered = true;
                }
            }
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "register mBrightObserver error! " + throwable);
        }
    }

    //?????????
    private void unregisterBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (isRegistered) {
                    getContentResolver().unregisterContentObserver(mBrightObserver);
                    isRegistered = false;
                }
            }
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }

    private int lastPage = -1;
    private int curPage = -1;
    private CollBookBean payBookBean;

    @Override
    protected void initClick() {
        super.initClick();

        mPageLoader.setOnPageChangeListener(
                //???????????????
                new PageLoader.OnPageChangeListener() {
                    @Override
                    public void onChapterChange(int pos) {
                        //todo ??????????????????????????????
                        mCategoryAdapter.setChapter(pos);
                        if (pos < 5){
                            //adParent.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void requestChapters(List<TxtChapter> requestChapters) {
                        mPresenter.loadChapter(mBookId, requestChapters);
                        mHandler.sendEmptyMessage(WHAT_CATEGORY);
                        //????????????
                        mTvPageTip.setVisibility(GONE);
                    }

                    @Override
                    public void onCategoryFinish(List<TxtChapter> chapters) {
                        for (TxtChapter chapter : chapters) {
                            chapter.setTitle(chapter.getTitle());
                        }
                        mCategoryAdapter.refreshItems(chapters);
                    }

                    @Override
                    public void onPageCountChange(int count) {
                        mSbChapterProgress.setMax(Math.max(0, count - 1));
                        mSbChapterProgress.setProgress(0);
                        // ????????????????????????????????????????????????
                        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING
                                || mPageLoader.getPageStatus() == PageLoader.STATUS_ERROR) {
                            mSbChapterProgress.setEnabled(false);
                        } else {
                            mSbChapterProgress.setEnabled(true);
                        }
                    }

                    @Override
                    public void onPageChange(int pos) {
                        mSbChapterProgress.post(() -> mSbChapterProgress.setProgress(pos)
                        );
                    }

                    @Override
                    public void onPayPage(CollBookBean mCollBook, int mCurChapterPos) {
                        Log.e("TAG", "onPayPage: " + mCurChapterPos);
                        payBookBean = mCollBook;
                        curPage = mCurChapterPos;
                    }
                }
        );

        mSbChapterProgress.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (mLlBottomMenu.getVisibility() == VISIBLE) {
                            //????????????
                            mTvPageTip.setText((progress + 1) + "/" + (mSbChapterProgress.getMax() + 1));
                            mTvPageTip.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //????????????
                        int pagePos = mSbChapterProgress.getProgress();
                        if (pagePos != mPageLoader.getPagePos()) {
                            mPageLoader.skipToPage(pagePos);
                        }
                        //????????????
                        mTvPageTip.setVisibility(GONE);
                    }
                }
        );
        mPvPage.setVipListener(new PageView.VipListener() {
            @Override
            public void showPayDialog() {
                if (mSettingDialog != null && mSettingDialog.isShowing()) {
                    return;
                }
                if (vipPageDialog != null && vipPageDialog.isShowing()) {
                    return;
                }

                if (lastPage == curPage) {
                    return;
                } else {
                    lastPage = curPage;
                }
                if (mCollBook.getBookChapters().get(curPage).getIs_pay()) {
                    return;
                }
                if (mmkv.getBoolean(mCollBook.get_id(), false)) {
                    if (mCollBook.getBookChapters().get(curPage).getIs_pay() || mPageLoader.getChapterCategory().get(curPage).isIs_pay()) {
                        return;
                    }
                    HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                            .params("anime_id", mCollBook.get_id())
                            .params("chaps", curPage + 1)
                            .execute(new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {
                                    Log.e(TAG, "msg: " + msg + " ,chap: " + (curPage + 1) + " line606");
                                    if (code == 200) {
                                        mCollBook.getBookChapters().get(curPage).setIs_pay(true);
                                        mPageLoader.getChapterCategory().get(curPage).setIs_pay(true);
                                        mPageLoader.refreshChapterList(false);
                                    }
                                }
                            });
                } else {
                    showPageDialog(mCollBook, curPage);
                    if (vipPageDialog == null) {
                        return;
                    }
                    if (!vipPageDialog.isShowing()) {
                        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        vipPageDialog.setPageLoader(mPageLoader);
                        vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                        vipPageDialog.setChapter_left(GetNonePayCount());
                        vipPageDialog.show();
                        hideSystemBar();
                    }

                }
            }
        });

        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public void prePage() {
            }

            //????????????
            @Override
            public void nextPage() {
            }

            @Override
            public void cancel() {
            }

            @Override
            public void buy() {
                if (mCollBook == null || curPage <= 0) {
                    return;
                }
                BookChapterBean bookChapterBean = mCollBook.getBookChapters().get(curPage);
                if (StringUtil.isVip(mInfoBean) || bookChapterBean.getCoin() <= 0 || bookChapterBean.getIs_pay()) {
                    return;
                }
                if (mSettingDialog != null && mSettingDialog.isShowing()) {
                    return;
                }
                if (vipPageDialog != null && vipPageDialog.isShowing()) {
                    return;
                }
                lastPage = curPage;
                showPageDialog(mCollBook, curPage);
                if (!vipPageDialog.isShowing()) {
                    mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                    vipPageDialog.setPageLoader(mPageLoader);
                    vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                    vipPageDialog.setChapter_left(GetNonePayCount());
                    vipPageDialog.show();
                    hideSystemBar();
                }
            }
        });

        //????????????
        mLvCategory.setOnItemClickListener(
                (parent, view, position, id) -> {
                    if (StringUtil.isVip(mInfoBean)) {
                        //?????????
                        mDlSlide.closeDrawer(GravityCompat.START);
                        mPageLoader.skipToChapter(position);
                    } else {
                        //?????????
                        List<TxtChapter> chapterCategory = mPageLoader.getChapterCategory();
                        TxtChapter txtChapter = chapterCategory.get(position);
                        if (txtChapter.getCoin() == 0) {
                            mDlSlide.closeDrawer(GravityCompat.START);
                            mPageLoader.skipToChapter(position);
                        } else if (txtChapter.isIs_pay()) {
                            mDlSlide.closeDrawer(GravityCompat.START);
                            mPageLoader.skipToChapter(position);
                        } else {
                            mDlSlide.closeDrawer(GravityCompat.START);
                            mPageLoader.skipToChapter(position);

                            //??????????????????
                            boolean totalAuto = mmkv.decodeBool("fuck_auto", false);
                            boolean auto = mmkv.decodeBool(mCollBook.get_id(), false);
                            if (auto && totalAuto) {
                                HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                                        .params("anime_id", mCollBook.get_id())
                                        .params("chaps", position + 1)
                                        .execute(new HttpCallback() {
                                            @Override
                                            public void onSuccess(int code, String msg, String[] info) {
                                                Log.e(TAG, "msg: " + msg + " ,chap: " + (position + 1) + " line719");
                                                if (code == 200) {
                                                    mCollBook.getBookChapters().get(position).setIs_pay(true);
                                                    mPageLoader.getChapterCategory().get(position).setIs_pay(true);
                                                    if (vipPageDialog != null && vipPageDialog.isShowing()) {
                                                        vipPageDialog.dismiss();
                                                    }
                                                    if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
                                                        mDlSlide.closeDrawer(GravityCompat.START);
                                                    }
                                                    mPageLoader.refreshChapterList(false);
                                                    mPageLoader.skipToChapter(position);
                                                    mCategoryAdapter.refreshItems(mPageLoader.getChapterCategory());
                                                } else {
                                                    showPageDialog(mCollBook, position);
                                                    if (!vipPageDialog.isShowing()) {
                                                        vipPageDialog.setChapter_left(GetNonePayCount());
                                                        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                                                        vipPageDialog.setPageLoader(mPageLoader);
                                                        vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                                                        vipPageDialog.show();
                                                        hideSystemBar();
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                //??????????????????
                                showPageDialog(mCollBook, position);
                                if (!vipPageDialog.isShowing()) {
                                    mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                                    vipPageDialog.setPageLoader(mPageLoader);
                                    vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                                    vipPageDialog.setChapter_left(GetNonePayCount());
                                    vipPageDialog.show();
                                    hideSystemBar();
                                }
                            }
                        }
                    }
                }
        );

        mTvCategory.setOnClickListener(
                (v) -> {
                    //?????????????????????
                    if (mCategoryAdapter.getCount() > 0) {
                        mLvCategory.setSelection(mPageLoader.getChapterPos());
                    }
                    //????????????
                    toggleMenu(true);
                    //??????????????????
                    mDlSlide.openDrawer(GravityCompat.START);
                }
        );
        ReadtvDashang.setOnClickListener(
                (v) -> {
                    GetRewordPackage();
                }
        );
        mTvSetting.setOnClickListener(
                (v) -> {
                    toggleMenu(false);
                    mSettingDialog.show();
                }
        );

        mTvPreChapter.setOnClickListener(
                (v) -> {
                    if (mPageLoader.skipPreChapter()) {
                        mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
                    }
                }
        );

        mTvNextChapter.setOnClickListener(
                (v) -> {
                    if (mPageLoader.skipNextChapter()) {
                        mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
                    }
                }
        );

        mTvNightMode.setOnClickListener(
                (v) -> {
                    if (isNightMode) {
                        isNightMode = false;
                        daytime();
                    } else {
                        night();
                        isNightMode = true;
                    }
                    mPageLoader.setNightMode(isNightMode);
                    toggleNightMode();
                }
        );

        mSettingDialog.setOnDismissListener(
                dialog -> hideSystemBar()
        );

        ivBack.setOnClickListener(v -> {
            if (isShelve != true) {
                showAddShelveDialog();
            } else {
                finish();
            }
        });
        ivLister.setOnClickListener(v -> {
            BookChapterBean bookChapterBean = mCollBook.getBookChapterList().get(mPageLoader.getChapterPos());
            if (checkVip()) {
                ListenBookActivity.start(JsReadActivity.this, mCollBook, isShelve, mPageLoader.getChapterPos());
            } else if (bookChapterBean.getIs_pay() || bookChapterBean.getCoin() <= 0) {
                ListenBookActivity.start(JsReadActivity.this, mCollBook, isShelve, mPageLoader.getChapterPos());
            } else {
                showPageDialog(mCollBook, mPageLoader.getChapterPos());
                if (vipPageDialog == null) {
                    return;
                }
                if (!vipPageDialog.isShowing()) {
                    mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                    vipPageDialog.setPageLoader(mPageLoader);
                    vipPageDialog.setOwnCoins(mInfoBean.getCoin());
                    vipPageDialog.setChapter_left(GetNonePayCount());
                    vipPageDialog.show();
                    hideSystemBar();
                }
            }
        });
        ivShare.setOnClickListener(v -> {
            SampleSHare();
        });
    }
    /**
     * ??????????????? ??????????????? ????????????
     */
    private void night() {
        mTvPreChapter.setTextColor(getContext().getResources().getColor(R.color.read_text_night_color));
        mTvNextChapter.setTextColor(getContext().getResources().getColor(R.color.read_text_night_color));
        mTvCategory.setTextColor(getContext().getResources().getColor(R.color.read_text_night_color));
        mTvNightMode.setTextColor(getContext().getResources().getColor(R.color.read_text_night_color));
        mTvSetting.setTextColor(getContext().getResources().getColor(R.color.read_text_night_color));
        tvBookName.setTextColor(getContext().getResources().getColor(R.color.read_text_night_color));
        tvTotalPage.setTextColor(getContext().getResources().getColor(R.color.read_text_night_color));
        tvOrder.setTextColor(getContext().getResources().getColor(R.color.read_text_night_color));
        mCategoryAdapter.setNight(true);

        sideslip.setBackgroundColor(getContext().getResources().getColor(R.color.nb_read_bg_night2));
        mAblTopMenu.setBackgroundColor(getContext().getResources().getColor(R.color.nb_read_bg_night2));
        mLlBottomMenu.setBackgroundColor(getContext().getResources().getColor(R.color.nb_read_bg_night2));
    }

    /**
     * ??????????????? ??????????????? ????????????
     */
    private void daytime() {
        mTvPreChapter.setTextColor(getContext().getResources().getColor(R.color.nb_read_menu_text));
        mTvNextChapter.setTextColor(getContext().getResources().getColor(R.color.nb_read_menu_text));
        mTvCategory.setTextColor(getContext().getResources().getColor(R.color.nb_read_menu_text));
        mTvNightMode.setTextColor(getContext().getResources().getColor(R.color.nb_read_menu_text));
        mTvSetting.setTextColor(getContext().getResources().getColor(R.color.nb_read_menu_text));
        tvBookName.setTextColor(getContext().getResources().getColor(R.color.nb_read_menu_text));
        tvTotalPage.setTextColor(getContext().getResources().getColor(R.color.nb_read_menu_text));
        tvOrder.setTextColor(getContext().getResources().getColor(R.color.nb_read_menu_text));
        mCategoryAdapter.setNight(false);

        sideslip.setBackgroundColor(getContext().getResources().getColor(R.color.nb_read_menu_bg));
        mAblTopMenu.setBackgroundColor(getContext().getResources().getColor(R.color.nb_read_menu_bg));
        mLlBottomMenu.setBackgroundColor(getContext().getResources().getColor(R.color.nb_read_menu_bg));
    }
    AddShelveDialog dialog;

    private List<RewordBean> rewordBeanList;
    private RewordBottomDialog rewordBottomDialog = null;
    int anime_id;

    private void GetRewordPackage() {
        try {
            if (rewordBottomDialog == null) {
                HttpClient.getInstance().post(AllApi.rewordPackage, AllApi.rewordPackage)
                        .execute(new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                RewordRuleBean rewordRuleBean = GsonUtils.parseJObject(info[0], RewordRuleBean.class);
                                rewordBeanList = rewordRuleBean.getList();
                                ShowRewordDialog();
                            }
                        });
            } else {
                ShowRewordDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowRewordDialog() {
        if (rewordBottomDialog == null) {
            rewordBottomDialog = new RewordBottomDialog(getContext(), rewordBeanList);
            rewordBottomDialog.setCancelable(true);
            rewordBottomDialog.setCanceledOnTouchOutside(true);
            rewordBottomDialog.setDialogCallBack(new RewordBottomDialog.RewordDialogCallBack() {
                @Override
                public void rewordClick(@NotNull String coin) {
                    commitReword(coin);
                }
            });
            rewordBottomDialog.show();
        } else {
            rewordBottomDialog.show();
        }

    }

    private Context getContext() {
        return mContext;
    }

    private void commitReword(String coin) {
        HttpClient.getInstance().post(AllApi.commitReword, AllApi.commitReword)
                .params("anid", mCollBook.get_id())
                .params("coin", coin)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        rewordBottomDialog.dismiss();
                        toast(msg);
                        getInfo(mCollBook.get_id());
                    }
                });
    }

    private void toast(CharSequence text) {

        ToastUtils.show((String) text);
    }

    private void showAddShelveDialog() {
        dialog = AddShelveDialog.getMyDialog(JsReadActivity.this);
        dialog.setDialogCallBack(new AddShelveDialog.DialogCallBack() {
            @Override
            public void onActionClick(String code) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                addshelve();
            }

            @Override
            public void onCancelClick() {
                if (dialog != null) {
                    dialog.dismiss();
                }
                finish();
            }
        });
        dialog.show();
    }

    private int GetNonePayCount() {
        List<TxtChapter> chapterCategory = mPageLoader.getChapterCategory();
        //???????????????????????????????????????
        int nonePayChapterCount = 0;
        for (TxtChapter item : chapterCategory) {
            if (item.getCoin() > 0 && !item.isIs_pay()) {
                nonePayChapterCount++;
            }
        }
        return nonePayChapterCount;
    }

    private void CommonShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos.isEmpty()) {
            return;
        }
        List<Intent> targetIntents = new ArrayList<>();
        for (ResolveInfo info : resolveInfos) {
            ActivityInfo ainfo = info.activityInfo;
            switch (ainfo.packageName) {
                case "com.tencent.mm":
                    addShareIntent(targetIntents, ainfo);
                    break;
                case "com.tencent.mobileqq":
                    addShareIntent(targetIntents, ainfo);
                    break;
                case "com.sina.weibo":
                    addShareIntent(targetIntents, ainfo);
                    break;
            }
        }
        if (targetIntents == null || targetIntents.size() == 0) {
            return;
        }
        Intent chooserIntent = Intent.createChooser(targetIntents.remove(0), "?????????????????????");
        if (chooserIntent == null) {
            return;
        }
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
        try {
            startActivity(chooserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
        }
        //startActivity(Intent.createChooser(intent, getTitle()));
    }

    private void addShareIntent(List<Intent> list, ActivityInfo ainfo) {
        Intent target = new Intent(Intent.ACTION_SEND);
        target.setType("text/plain");
        target.putExtra(Intent.EXTRA_TEXT, mCollBook.getShareTitle());
        target.putExtra(Intent.EXTRA_TITLE, mCollBook.getShareTitle());
        target.putExtra(Intent.EXTRA_SUBJECT, mCollBook.getShareTitle());
        target.setPackage(ainfo.packageName);
        target.setClassName(ainfo.packageName, ainfo.name);
        list.add(target);
    }

    private void SampleSHare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mCollBook.getShareTitle());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public Bitmap createBitmap3(View v, int width, int height) {
        //????????????view????????????
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //??????layout??????????????????????????????view???????????????
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bitmap;
    }

    private void createBitmap(View view) {
        int w = ScreenUtils.getDisplayMetrics().widthPixels - ScreenUtils.dpToPx(40);
        view.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec((w - 40) * 2 / 3, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        adBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(adBitmap);
        c.drawColor(Color.WHITE);
        view.draw(c);
        if (adBitmap == null) {
            return;
        }
        Bitmap nextBitmap = mPvPage.getNextBitmap();
        if (nextBitmap != null) {
            Bitmap bitmap = BitmapUtils.combineBitmap(nextBitmap, adBitmap);
            mPvPage.notify();
        }
    }

    private void showPageDialog(CollBookBean mCollBook, int mCurChapterPos) {
        if (mCurChapterPos < 0) {
            return;
        }
        if (vipPageDialog == null) {
            vipPageDialog = new VipPageDialog(JsReadActivity.this, mCollBook, mCurChapterPos);
            vipPageDialog.setPageLoader(mPageLoader);
            vipPageDialog.setCancelable(false);
            vipPageDialog.setPayClickable(true);
            vipPageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideSystemBar();
                }
            });
            vipPageDialog.payListener = new VipPageDialog.PayDialogListener() {
                @Override
                public void dialogClose(boolean auto) {
                    Log.e(TAG, "auto: " + auto);
                }

                @Override
                public void byCurrent(int currentPos, int buyChapterCount) {
                    if (vipPageDialog.getPayClickable()) {
                        byPage(vipPageDialog, currentPos, buyChapterCount);
                    }
                    vipPageDialog.setPayClickable(false);
                }

                @Override
                public void goOpen() {
                    OpenVipActivity.start(JsReadActivity.this);
                }
            };
        } else {
            vipPageDialog.setPageLoader(mPageLoader);
            vipPageDialog.setBook(mCollBook);
            vipPageDialog.setCurrent(mCurChapterPos);
            vipPageDialog.setPayClickable(true);
            vipPageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideSystemBar();
                }
            });
            vipPageDialog.payListener = new VipPageDialog.PayDialogListener() {
                @Override
                public void dialogClose(boolean auto) {
                }

                @Override
                public void byCurrent(int current, int buyChapterCount) {
                    if (vipPageDialog.getPayClickable()) {
                        byPage(vipPageDialog, current, buyChapterCount);
                    }
                    vipPageDialog.setPayClickable(false);
                }

                @Override
                public void goOpen() {
                    OpenVipActivity.start(JsReadActivity.this);
                }
            };
        }
    }

    //????????????
    private void byPage(VipPageDialog vipPageDialog, int current, int buyChapterCount) {
        HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                .params("anime_id", mCollBook.get_id())
                .params("chaps", current + 1)
                .params("chaps_count", buyChapterCount)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        Log.e(TAG, "msg: " + msg + " ,chap: " + (current + 1) + " line895");

                        try {
                            if (code == 200) {
                                ToastUtils.show("Purchase successful");

                                PayChapterBean bean = new Gson().fromJson(info[0], PayChapterBean.class);
                                mInfoBean.setCoin(bean.getCoins());
                                MMKV.defaultMMKV().encode(SpUtil.USER_INFO, mInfoBean);

                                if (buyChapterCount != 100) {
                                    for (int i = 0; i < buyChapterCount; i++) {
                                        if ((current + i) < mCollBook.getBookChapters().size()) {
                                            mCollBook.getBookChapters().get(current + i).setIs_pay(true);
                                            mPageLoader.getChapterCategory().get(current + i).setIs_pay(true);
                                        } else {
                                            for (BookChapterBean item : mCollBook.getBookChapters()) {
                                                if (!item.getIs_pay() && item.getCoin() > 0) {
                                                    item.setIs_pay(true);
                                                    break;
                                                }
                                            }

                                            for (TxtChapter item : mPageLoader.getChapterCategory()) {
                                                if (!item.isIs_pay() && item.getCoin() > 0) {
                                                    item.setIs_pay(true);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    //???????????????
                                    for (BookChapterBean item : mCollBook.getBookChapters()) {
                                        item.setIs_pay(true);
                                    }

                                    for (TxtChapter item : mPageLoader.getChapterCategory()) {
                                        item.setIs_pay(true);
                                    }
                                }

                                //??????????????????
                                OnHidePayPage();
//                        if (JsReadActivity.this.vipPageDialog != null && JsReadActivity.this.vipPageDialog.isShowing()) {
//                            JsReadActivity.this.vipPageDialog.dismiss();
//                        }
                                if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
                                    mDlSlide.closeDrawer(GravityCompat.START);
                                }

                                if (vipPageDialog.isAuto()) {
                                    mmkv.encode("fuck_auto", true);
                                }
                                mmkv.encode(mCollBook.get_id(), vipPageDialog.isAuto());
                                mPageLoader.refreshChapterList(false);
                                mPageLoader.skipToChapter(current);
                            } else {
                                if (JsReadActivity.this.vipPageDialog != null) {
                                    JsReadActivity.this.vipPageDialog.setPayClickable(true);
                                }
                                //??????????????????
                                //OnHidePayPage();

                                ToastUtils.show("Purchase failed " + Constants.getInstance().getConfig().getCoin_name() + " not enough");
                                UserBean userBean1 = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);

                                if (userBean1.getBindStatus().equals("0")) {
                                    ThirdLoginActivity.start(JsReadActivity.this, ThirdLoginActivity.EnterIndex.PAY);
                                } else {
                                    startActivity(TopUpActivity.class);
                                }
                            }
                        } catch (Exception e) {
                            Log.d("JsReadActivity", "JsReadActivity: " + e);
                        }
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        if (JsReadActivity.this.vipPageDialog != null) {
                            JsReadActivity.this.vipPageDialog.setPayClickable(true);
                        }
                    }
                });
    }

    public static void OnHidePayPage() {
        if (jsReadActivity.vipPageDialog != null && jsReadActivity.vipPageDialog.isShowing()) {
            jsReadActivity.vipPageDialog.dismiss();
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @return ??????????????????
     */
    private boolean hideReadMenu() {
        hideSystemBar();
        if (mAblTopMenu.getVisibility() == VISIBLE) {
            toggleMenu(true);
            return true;
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return true;
        }
        return false;
    }

    private void showSystemBar() {
        //??????
        SystemBarUtils.showUnStableStatusBar(this);
        if (isFullScreen) {
            SystemBarUtils.showUnStableNavBar(this);
        }
    }

    private void hideSystemBar() {
        //??????
        SystemBarUtils.hideStableStatusBar(this);
        if (isFullScreen) {
            SystemBarUtils.hideStableNavBar(this);
        }
    }

    /**
     * ??????????????????????????????
     * ??????????????????
     */
    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();

        if (mAblTopMenu.getVisibility() == VISIBLE) {
            //??????
            mAblTopMenu.startAnimation(mTopOutAnim);
            mLlBottomMenu.startAnimation(mBottomOutAnim);
            mAblTopMenu.setVisibility(GONE);
            mLlBottomMenu.setVisibility(GONE);
            mTvPageTip.setVisibility(GONE);

            if (hideStatusBar) {
                hideSystemBar();
            }
        } else {
            mAblTopMenu.setVisibility(VISIBLE);
            mLlBottomMenu.setVisibility(VISIBLE);
            mAblTopMenu.startAnimation(mTopInAnim);
            mLlBottomMenu.startAnimation(mBottomInAnim);

            showSystemBar();
        }
    }

    //?????????????????????
    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //?????????????????????
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        List<BookChapterBean> allChapters = mCollBook.getBookChapters();
        //????????????
        showCategory(allChapters);
    }

    /***************************view************************************/
    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showCategory(List<BookChapterBean> bookChapters) {
        mPageLoader.getCollBook().setBookChapters(bookChapters);
        mPageLoader.refreshChapterList(false);

        // ??????????????????????????????????????????????????????????????????
        if (mCollBook.isUpdate() && isCollected) {
            BookRepository.getInstance().saveBookChaptersWithAsync(bookChapters);
        }
    }

    @Override
    public void finishChapter() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mHandler.sendEmptyMessage(WHAT_CHAPTER);
        }
        // ???????????????????????????????????????
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorChapter() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mPageLoader.chapterError();
        }
    }

    @Override
    public void upDate(String chaps) {
        try {
            int chap = Integer.parseInt(chaps);
            Log.e(TAG, "upDate chap: " + chap);
            mCollBook.getBookChapters().get(chap - 1).setIs_pay(true);
            BookRepository.getInstance().saveBookChaptersWithAsync(mCollBook.getBookChapters());
            mPageLoader.getChapterCategory().get(chap - 1).setIs_pay(true);
            mPageLoader.refreshChapterList(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mAblTopMenu.getVisibility() == VISIBLE) {
            // ?????????????????????????????????????????????
            if (!ReadSettingManager.getInstance().isFullScreen()) {
                toggleMenu(true);
                return;
            }
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return;
        } else if (mDlSlide.isDrawerOpen(GravityCompat.START)) {
            mDlSlide.closeDrawer(GravityCompat.START);
            return;
        }

        Log.e(TAG, "isLocal: " + mCollBook.isLocal() + " , isCollected:" + isCollected);
        isShelve = true;
        if (!isShelve) {
            TipDialog myDialog = TipDialog.getMyDialog(this);
            myDialog.setTitle("Add");
            myDialog.setSubmit("OK");
            myDialog.setCancel("Cancel");
            myDialog.setMessage("If you like the book, add it to Library");
            myDialog.setDialogCallBack(new TipDialog.DialogCallBack() {
                @Override
                public void onActionClick() {
                    if (AppUtils.isLogin()) {
                        //??????????????????
                        isCollected = true;
                        //??????????????????
                        mCollBook.setLastRead(StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
                        addshelve();
                        BookRepository.getInstance().saveCollBookWithAsync(mCollBook);
                    } else {
                        MMKV mmkv = MMKV.defaultMMKV();
                        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        if (userBean.getBindStatus().equals("0")) {
                            ThirdLoginActivity.start(getApplicationContext(), ThirdLoginActivity.EnterIndex.OTHER);
                            return;
                        }
                    }
                }
            });
            myDialog.setCancelCallBack(new TipDialog.DialogCancelCallBack() {
                @Override
                public void onCancel() {
                    exit();
                }
            });
            myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    hideSystemBar();
                }
            });

            myDialog.show();
        } else {
            exit();
        }
    }

    //????????????
    private void addshelve() {
        HttpClient.getInstance().get(AllApi.addshelve, AllApi.addshelve)
                .params("anime_id", mCollBook.get_id())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        ToastUtils.show(msg);
                        exit();
                    }
                });
    }

    // ??????
    private void exit() {
        boolean login = AppUtils.isLogin();
        Log.e(TAG, "login: " + login);
        if (login) {
            Log.e(TAG, "msg: ????????????");
            HttpClient.getInstance().get(AllApi.updatereadtime, AllApi.updatereadtime)
                    .params("time", readMills)
                    .execute(new HttpCallback() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            Log.e(TAG, "code: " + code + " ,msg: " + msg);
                            try {
                                finish();
                                /*
                                if (msg.equals("Successful operation")) {
                                    mCommitView = DialogManager.getInstance().initView(BookDetailActivity.mBookDetailActivity, R.layout.dialog_notice_gain);
                                    TextView txt1 = mCommitView.findViewById(R.id.updataversion_title);
                                    txt1.setText("Congratulations on completing \n the welfare task");
                                    txt1.setTextSize(18);

                                    TextView goldMount = mCommitView.findViewById(R.id.updataversion_name);
                                    goldMount.setText("Click [Me] menu to view and receive");

                                    TextView tv_confirm = mCommitView.findViewById(R.id.tv_confirm);
                                    tv_confirm.setText("OK");
                                    tv_confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DialogManager.getInstance().hide(mCommitView);
                                        }
                                    });
                                    DialogManager.getInstance().show(mCommitView);
                                }
                                */
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            super.onBackPressed();
        }
    }

    private DialogView mCommitView;

    //TODO ??????????????????
    private void inquireCoin() {
        String token = mmkv.decodeString(SpUtil.TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            mInfoBean = new UserBean();
            mInfoBean.setIs_vip("0");
            mmkv.encode(SpUtil.USER_INFO, mInfoBean);
        } else {
            HttpClient.getInstance().get(AllApi.userinfo, AllApi.userinfo)
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            mInfoBean = new Gson().fromJson(info[0], UserBean.class);
                            mmkv.encode(SpUtil.USER_INFO, mInfoBean);
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
    }

    public void getInfo(String anime_id) {
        HttpClient.getInstance().get(AllApi.bookinfo, AllApi.bookinfo)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        BookDetailBean bookDetailBean = new Gson().fromJson(info[0], BookDetailBean.class);
                        isShelve = bookDetailBean.getIs_shelve().equals("1");
                    }
                });
    }

    private void ResumeChapter() {
        BookRecordBean mBookRecord = BookRepository.getInstance().getBookRecord(mCollBook.get_id());
        if (mBookRecord != null && mPageLoader.getChapterPos() != mBookRecord.getChapter()) {
            mPageLoader.skipToChapter(mBookRecord.getChapter());
        }
    }

//    public static void OnReshPayChapterUI()
//    {
//        try{
//            //?????????????????????????????????????????????
//            if (jsReadActivity!= null && jsReadActivity.vipPageDialog != null && jsReadActivity.vipPageDialog.isShowing()) {
//                jsReadActivity.vipPageDialog.setOwnCoins(mInfoBean.getCoin());
//                jsReadActivity.vipPageDialog.show();
//            }
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }

    @Override
    protected void onResume() {
        jsReadActivity = this;
        super.onResume();
        if (mCollBook != null) {
            getInfo(mCollBook.get_id());
        }
        if (cdt != null) {
            cdt.start();
        }
        mInfoBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        //???????????????Vip
        boolean isVip = checkVip();
        if (isVip) {
            if (mPageLoader != null) {
                mPageLoader.refreshChapterList(true);
            }
            if (vipPageDialog != null && vipPageDialog.isShowing()) {
                vipPageDialog.dismiss();
            }
        }

        //?????????????????????????????????????????????
        if (vipPageDialog != null && vipPageDialog.isShowing()) {
            vipPageDialog.setOwnCoins(mInfoBean.getCoin());
            vipPageDialog.show();
        }

        mWakeLock.acquire();
        hideSystemBar();
        ResumeChapter();
        isFirstLoadAd = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        mPageLoader.saveRecord();

        if (cdt != null) {
            cdt.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);

        mHandler.removeMessages(WHAT_CATEGORY);
        mHandler.removeMessages(WHAT_CHAPTER);

        mPageLoader.closeBook();
        mPageLoader = null;
        if (cdt != null) {
            cdt.cancel();
            cdt = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isVolumeTurnPage = ReadSettingManager.getInstance().isVolumeTurnPage();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (isVolumeTurnPage) {
                    return mPageLoader.skipToPrePage();
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (isVolumeTurnPage) {
                    return mPageLoader.skipToNextPage();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SystemBarUtils.hideStableStatusBar(this);
        if (requestCode == REQUEST_MORE_SETTING) {
            boolean fullScreen = ReadSettingManager.getInstance().isFullScreen();
            if (isFullScreen != fullScreen) {
                isFullScreen = fullScreen;
                // ??????BottomMenu
                initBottomMenu();
            }

            // ??????????????????
            if (isFullScreen) {
                SystemBarUtils.hideStableNavBar(this);
            } else {
                SystemBarUtils.showStableNavBar(this);
            }
        }
    }

    //?????????vip
    private boolean checkVip() {
        UserBean bean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        if (bean != null && bean.getIs_vip() != null) {
            return StringUtil.isVip(bean);
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private long lastClick = 0;

    @OnClick(R.id.tvOrder)
    public void onViewClicked() {
        long millis = System.currentTimeMillis();
        if (millis - lastClick < 500) {
            return;
        }
        lastClick = millis;
        if (order) {
            tvOrder.setText("Positive order");
            List<TxtChapter> items = mCategoryAdapter.getItems();
            mCategoryAdapter.refreshItems(KtUtils.reverse(items));
        } else {
            tvOrder.setText("Reverse order");
            List<TxtChapter> items = mCategoryAdapter.getItems();
            mCategoryAdapter.refreshItems(KtUtils.reverse(items));
        }
    }

}

