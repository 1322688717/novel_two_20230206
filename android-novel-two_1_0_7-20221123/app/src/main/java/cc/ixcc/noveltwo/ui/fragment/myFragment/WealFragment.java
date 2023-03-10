package cc.ixcc.noveltwo.ui.fragment.myFragment;

import android.content.Intent;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import cc.ixcc.noveltwo.Constants;

import cc.ixcc.noveltwo.R;

import cc.ixcc.noveltwo.bean.ActiviyNoticeInfo;
import cc.ixcc.noveltwo.bean.UserBean;
import cc.ixcc.noveltwo.bean.WealBean;
import cc.ixcc.noveltwo.common.MyFragment;
import cc.ixcc.noveltwo.custom.ShelveBannerLoader;
import cc.ixcc.noveltwo.event.SkipStackEvent;

import cc.ixcc.noveltwo.helper.BindEventBus;
import cc.ixcc.noveltwo.http.AllApi;
import cc.ixcc.noveltwo.http.HttpCallback;
import cc.ixcc.noveltwo.http.HttpClient;
import cc.ixcc.noveltwo.statistics.AdjustUtil;
import cc.ixcc.noveltwo.treader.AppContext;
import cc.ixcc.noveltwo.ui.activity.HomeActivity;
import cc.ixcc.noveltwo.ui.activity.my.BookDetailActivity;
import cc.ixcc.noveltwo.ui.activity.my.ThirdLoginActivity;
import cc.ixcc.noveltwo.ui.activity.my.TopUpActivity;
import cc.ixcc.noveltwo.ui.activity.my.vip.OpenVipActivity;
import cc.ixcc.noveltwo.ui.adapter.myAdapter.TaskBasicAdapter;
import cc.ixcc.noveltwo.ui.adapter.myAdapter.TaskDayAdapter;
import cc.ixcc.noveltwo.ui.adapter.myAdapter.TaskDayReadAdapter;
import cc.ixcc.noveltwo.ui.adapter.myAdapter.TaskNoviceAdapter;
import cc.ixcc.noveltwo.ui.adapter.myAdapter.WealSignAdapter;
import cc.ixcc.noveltwo.ui.dialog.SignSuccessDialog;
import cc.ixcc.noveltwo.ui.dialog.EmailDataDialog;

import com.jiusen.base.util.DpUtil;
import com.jiusen.base.views.MyReboundScrollView;
import com.tencent.mmkv.MMKV;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cc.ixcc.noveltwo.utils.SpUtil;

/**
 * desc   : ??????Fragment
 */
@BindEventBus
public final class
WealFragment extends MyFragment<HomeActivity> implements View.OnClickListener {
    @BindView(R.id.coin)
    TextView coin;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.week)
    TextView week;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.rv_sign)
    RecyclerView rvSign;
    @BindView(R.id.coin_most)
    TextView coinMost;
    @BindView(R.id.time)
    TextView time;
    //???????????? Basic_Task
//    @BindView(R.id.Basic_Task)
//    RecyclerView rvBasicTask;
//    //????????????
//    @BindView(R.id.Novice_Task_Bg)
//    LinearLayout Novice_Task_Bg;
//    @BindView(R.id.Novice_Task)
//    RecyclerView rvNoviceTask;
//    @BindView(R.id.Novice_Task_Title)
//    RelativeLayout Novice_Title;
//    //????????????
//    @BindView(R.id.rv_Tasks)
//    LinearLayout rv_Tasks;
//    @BindView(R.id.Daily_Tasks)
//    RecyclerView rvDayTasks;
//    @BindView(R.id.Daily_Tasks_Title)
//    RelativeLayout Day_Title;
    //??????????????????
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.reboundscroll)
    MyReboundScrollView reboundscroll;
    @BindView(R.id.titletext)
    TextView titleView;
    @BindView(R.id.btn_return)
    TextView btn_return;
    @BindView(R.id.ll_banner)
    LinearLayout ll_banner;
    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.sign2)
    TextView sign2;


    private WealSignAdapter mSignAdapter;
    private TaskDayReadAdapter mDayReadAdapter;
    private TaskBasicAdapter mBasicTaskAdapter;
    private TaskNoviceAdapter mNoviceTaskAdapter;
    private TaskDayAdapter mDayAdapter;

    private WealBean wealBean;
    private String todayCoin = "0";

    public static WealFragment newInstance() {
        WealFragment fragment = new WealFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_weal;
    }

    @Override
    protected void initView() {
        //????????????
        initSignRv();
        //???????????????
        initTaskRv();
        setOnClickListener(R.id.sign);

        reboundscroll.addOnOffsetChangedListener(new MyReboundScrollView.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(View view, int verticalOffset) {
                float scrollY = reboundscroll.getScrollY();
                float rate;
                if (scrollY > DpUtil.dip2px(getContext(), 65)) {
                    rate = 1;
                } else {
                    rate = scrollY / DpUtil.dip2px(getContext(), 65);
                }
                if (titleView != null) {
                    titleView.setAlpha(rate);
                }
            }
        });
        //?????????????????????
        setBtn_return();
        //?????????
        initBanner();


    }

    //????????????????????????
    public void setBtn_return() {
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.mHomeActivity.GotoLastfrag();
            }
        });
    }

    //?????????banner?????????
    public void initBanner() {
        try {

            if (!HomeActivity.mHomeActivity.IsPostNotice) {
                HomeActivity.mHomeActivity.GetActiviyNoticeMsg(() -> {
                    ShowBanner(HomeActivity.mHomeActivity);
                });
            } else {
                ShowBanner(HomeActivity.mHomeActivity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowBanner(HomeActivity mHomeActivity) {
        //??????????????????????????? ???????????????
        if (mHomeActivity.adList_weal.size() > 0) {
//            ll_banner.setVisibility(View.VISIBLE);
            ll_banner.setVisibility(AppContext.sInstance.getTenjinFlag() == -1 ? View.VISIBLE : View.GONE);
            banner.setOutlineProvider(new ViewOutlineProvider() { //??????????????????????????????????????????
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20); //????????????
                }
            });

            banner.setClipToOutline(true);
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //?????????????????????
            banner.setImageLoader(new ShelveBannerLoader());
            //??????????????????
            banner.setImages(mHomeActivity.adList_weal);
            //??????????????????
            banner.setDelayTime(5000);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Log.e("TAG", "banner_position:" + position);
                    if (mHomeActivity.adList_weal != null) {
                        if (position >= 0 && position < mHomeActivity.adList_weal.size()) {
                            ActiviyNoticeInfo bean = mHomeActivity.adList_weal.get(position);
                            if (bean != null) {
//                                AdjustUtil.GetInstance().SendBannerEvent(3);
                                UserBean tempBean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                                //9???????????????????????? 10???????????????????????? 11????????????VIP?????? 12????????????URL??????
                                switch (bean.getType()) {
                                    case 9:
                                        if (tempBean.getBindStatus().equals("0")) {
                                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                                        } else {
                                            startActivity(TopUpActivity.class);
                                        }
                                        break;
                                    case 10:
                                        BookDetailActivity.start(getContext(), bean.getAnid());
                                        break;
                                    case 11:
                                        if (tempBean.getBindStatus().equals("0")) {
                                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                                        } else {
                                            OpenVipActivity.start(getContext());
                                        }
                                        break;
                                    case 12:
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bean.gethttp_url())));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                }
            });
            //banner?????????????????????????????????????????????
            banner.start();
        }
    }

    private void showSignSuccessDialog(String coin) {
        //???????????????
        final SignSuccessDialog dialog = SignSuccessDialog.getMyDialog(getActivity());
        //??????????????????
        dialog.setDialogCallBack(new SignSuccessDialog.DialogCallBack() {
            @Override
            public void onActionClick(String message) {
                //TODO

            }
        });
        dialog.setShuDou(coin);
        dialog.show();
    }

    private void setAndroidNativeLightStatusBar(boolean light) {
        // ???????????????????????????????????? // ?????????????????????????????????????????????????????????false???????????????????????????????????????????????????mode
        ImmersionBar.with(this).statusBarDarkFont(!light).keyboardEnable(true).init();
    }

    @Override
    protected void initData() {

    }

    public void getInfo() {
        HttpClient.getInstance().get(AllApi.welfarebaseinfo, AllApi.welfarebaseinfo)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        wealBean = new Gson().fromJson(info[0], WealBean.class);
                        //????????????
                        wealBean.DataFiltering();
                        setView();
                    }
                });
    }

    public void sign(String coin) {
        HttpClient.getInstance().post(AllApi.sign, AllApi.sign)
                .isMultipart(true)
                .params("coin", coin)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        getInfo();
                        showSignSuccessDialog(coin);

                        sign.setVisibility(View.GONE);

                        sign2.setVisibility(View.VISIBLE);
                        sign2.setText("Sign in tomorrow to get " + coin + " Coins");
                    }
                });
    }

    private void setView() {
        if (wealBean == null) {
            return;
        }

        time.setText((wealBean.getToday_read_time()) + " minutes read today");
        day.setText(wealBean.getD());
        week.setText(wealBean.getW());
        coin.setText(wealBean.getCoin() + ""); // Constants.getInstance().getCoinName()
        date.setText(wealBean.getM() + " " + wealBean.getYear());
        coinMost.setText(wealBean.getSign_total_coin() + Constants.getInstance().getCoinName());
        mSignAdapter.setToday(wealBean.getW());
        initSignRv();
        mSignAdapter.setData(wealBean.getSign_config());

        mDayReadAdapter.setData(wealBean.getDaily_read_config());
        mBasicTaskAdapter.setData(wealBean.getBasicConfig());
        mDayAdapter.setData(wealBean.getDayConfig());
        mNoviceTaskAdapter.setData(wealBean.getNoviceConfig());

        //????????????
        if (wealBean.getDayConfig().size() == 0) {
//            rv_Tasks.setVisibility(View.GONE);
//            rvDayTasks.setVisibility(View.GONE);
//            Day_Title.setVisibility(View.GONE);
        }

        //????????????
        if (wealBean.getNoviceConfig().size() == 0) {
//            Novice_Task_Bg.setVisibility(View.GONE);
//            rvNoviceTask.setVisibility(View.GONE);
//            Novice_Title.setVisibility(View.GONE);
        }

        for (int i = 0; i < wealBean.getSign_config().size(); i++) {
            WealBean.SignConfigBean bean = wealBean.getSign_config().get(i);
            if (bean.getTitle().equals(wealBean.getW())) {
                todayCoin = bean.getCoin();

                break;
            }
        }

        if (!HomeActivity.mHomeActivity.mShowActiveNotie) {
            sign.setVisibility(View.GONE);

            sign2.setVisibility(View.VISIBLE);
            sign2.setText("Sign in tomorrow to get " + todayCoin + " Coins");
        }
    }

    private void initSignRv() {

        try {
            mSignAdapter = new WealSignAdapter(getContext());
            rvSign.setLayoutManager(new GridLayoutManager(getContext(), 7, GridLayoutManager.VERTICAL, false));
            rvSign.setAdapter(mSignAdapter);
            rvSign.setNestedScrollingEnabled(false);
            mSignAdapter.setOnClickListener(new WealSignAdapter.OnClickListener() {

            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isVisibleToUser) return;
        getInfo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAndroidNativeLightStatusBar(true);
            }
        }, 10);
    }

    boolean isVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getInfo();
                    setAndroidNativeLightStatusBar(true);
                }
            }, 10);
            //?????????Fragment???onResume??????true??????Fragment????????????
        } else {
            //?????????Fragment???onPause??????false??????Fragment?????????
        }
    }

    private int AdState = 0;

    //??????????????????
    private void initTaskRv() {
        //??????????????????
        mDayReadAdapter = new TaskDayReadAdapter(getContext());
        rvRecord.setAdapter(mDayReadAdapter);
        rvRecord.setNestedScrollingEnabled(false);
        mDayReadAdapter.setOnClickListener(new TaskDayReadAdapter.OnClickListener() {
            @Override
            public void onJump(WealBean.DailyReadConfigBean bean) {
                //????????????
                EventBus.getDefault().post(new SkipStackEvent());
            }
        });

        //????????????
        mBasicTaskAdapter = new TaskBasicAdapter(getContext());
//        rvBasicTask.setAdapter(mBasicTaskAdapter);
//        rvBasicTask.setNestedScrollingEnabled(false);

        final EmailDataDialog dialog = EmailDataDialog.getMyDialog(this.getActivity());
        dialog.setMessage(AppContext.Emailmsg);
        //??????????????????
        mBasicTaskAdapter.setOnClickListener(new TaskBasicAdapter.OnClickListener() {
            @Override
            public void bindEmail(WealBean.BasicConfigBean bean) {
                //??????????????????
                dialog.setDialogCallBack(new EmailDataDialog.DialogCallBack() {
                    @Override
                    public void onActionClick(String code) {

                        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                        Pattern regex = Pattern.compile(check);
                        Matcher matcher = regex.matcher(code);
                        boolean isMatched = matcher.matches();
                        if (isMatched) {
                            //????????????
                            HttpClient.getInstance().post(AllApi.bindemail, AllApi.bindemail)
                                    .params("email", code)
                                    .execute(new HttpCallback() {
                                        @Override
                                        public void onSuccess(int code, String msg, String[] info) {
                                            toast("Binding success");
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            toast("The mailbox format is incorrect");
                        }

                    }
                });
                dialog.show();

            }
        });


        //????????????
        mNoviceTaskAdapter = new TaskNoviceAdapter(getContext());
//        rvNoviceTask.setAdapter(mNoviceTaskAdapter);
//        rvNoviceTask.setNestedScrollingEnabled(false);
        mNoviceTaskAdapter.setOnClickListener(new TaskNoviceAdapter.OnClickListener() {
            @Override
            public void onJump(WealBean.NoviceConfigBean bean) {
                if (bean.getTitle().equals("Add a book")) {
                    //????????????
                    EventBus.getDefault().post(new SkipStackEvent());
                } else if (bean.getTitle().equals("First recharge")) {
                    //????????????
                    startActivity(TopUpActivity.class);
                } else if (bean.getTitle().equals("First 5 minutes of reading")) {
                    //????????????
                    EventBus.getDefault().post(new SkipStackEvent());
                }
            }
        });

        //????????????
        mDayAdapter = new TaskDayAdapter(getContext());
//        rvDayTasks.setAdapter(mDayAdapter);
//        rvDayTasks.setNestedScrollingEnabled(false);
        mDayAdapter.setOnClickListener(new TaskDayAdapter.OnClickListener() {
            @Override
            public void onJump(WealBean.DayConfigBean bean) {
                if (bean.getTitle().equals("Read books")) {
                    //????????????
                    EventBus.getDefault().post(new SkipStackEvent());
                } else if (bean.getTitle().equals("Make a comment")) {
                    //????????????
                    EventBus.getDefault().post(new SkipStackEvent());
                } else if (bean.getTitle().equals("Purchase coins")) {
                    //????????????
                    startActivity(TopUpActivity.class);
                } else if (bean.getTitle().equals("Unlock the chapters")) {
                    //????????????
                    EventBus.getDefault().post(new SkipStackEvent());
                }
            }
        });
    }

    EmailDataDialog emailDataDialog;

    private void showEmailDataDialog() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateCopyright(String envent) {
//        Log.e("EventBus", "BookShelveFragment--Copyright????????????" + envent);
        if (envent.equals("-1")) {
            ll_banner.setVisibility(View.VISIBLE);
        } else {
            ll_banner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign:
                sign(todayCoin);
                break;
            default:
                break;
        }
    }
}