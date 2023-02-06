package cc.ixcc.noveltwo.ui.fragment.myFragment;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.noveltwo.R;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.http.okhttp.utils.L;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cc.ixcc.noveltwo.bean.ClassficationBookBean;
import cc.ixcc.noveltwo.bean.ClassificationTitleBean;
import cc.ixcc.noveltwo.common.MyFragment;
import cc.ixcc.noveltwo.http.AllApi;
import cc.ixcc.noveltwo.http.Data;
import cc.ixcc.noveltwo.http.HttpCallback;
import cc.ixcc.noveltwo.http.HttpClient;
import cc.ixcc.noveltwo.treader.AppContext;
import cc.ixcc.noveltwo.ui.activity.HomeActivity;
import cc.ixcc.noveltwo.ui.activity.my.BookDetailActivity;
import cc.ixcc.noveltwo.ui.adapter.ClassificationBookAdapter;
import cc.ixcc.noveltwo.ui.adapter.ClassificationTitleAdapter;

import static cc.ixcc.noveltwo.Constants.PAGE_SIZE;

public class ClassificationDataFragment extends MyFragment<HomeActivity> implements OnItemClickListener {
    @BindView(R.id.rlv_title)
    RecyclerView mRlvTitle;
    @BindView(R.id.rlv_content)
    RecyclerView mRlvContent;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int page;
    private int novelId = -1;
    private ClassificationTitleAdapter classificationTitleAdapter;
    private ClassificationBookAdapter classificationBookAdapter;
    private int issex;

    public static ClassificationDataFragment newInstance(int issex) {
        ClassificationDataFragment fragment = new ClassificationDataFragment();
        Bundle args = new Bundle();
        args.putInt("issex", issex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classification_data;
    }

    @Override
    protected void initView() {
        issex = getArguments().getInt("issex", 1);
        classificationTitleAdapter = new ClassificationTitleAdapter();
        classificationBookAdapter = new ClassificationBookAdapter();
        mRlvTitle.setAdapter(classificationTitleAdapter);
        mRlvContent.setAdapter(classificationBookAdapter);
        classificationTitleAdapter.setOnItemClickListener(this);
        classificationBookAdapter.setOnItemClickListener((adapter, view, position) -> BookDetailActivity.start(getContext(), classificationBookAdapter.getItem(position).getId()));
        initRefreshLayout();
        initTitleData(issex);
    }

    private void initTitleData(int issex) {
        HttpClient.getInstance().get(AllApi.bookclass, AllApi.bookclass)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        List<String> strings = Arrays.asList(info);
                        classificationTitleAdapter.setNewInstance(null);
                        if (strings.size() > 0) {
                            for (String string : strings) {
                                ClassificationTitleBean data = new Gson().fromJson(string, ClassificationTitleBean.class);
                                if (issex == 1 && data.sex.equals("1")) {
                                    classificationTitleAdapter.addData(data);
                                } else if (issex == 0 && data.sex.equals("2")) {
                                    classificationTitleAdapter.addData(data);
                                }
                            }
                            classificationTitleAdapter.getItem(0).setSelect(true);
                            novelId = classificationTitleAdapter.getItem(0).getNovel_id();
                            refreshLayout.autoRefresh();
                        }
                    }

                    @Override
                    public void onError(Response<Data> response) {
                        Throwable t = response.getException();
                        L.e("网络请求错误---->" + t.getClass() + " : " + t.getMessage());
                        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException) {
                            ToastUtils.show("Network request failed");
                        }

                        super.onError(response);
                        hideDialog();
                    }
                });

    }

    private void initRefreshLayout() {
        //   refreshLayout.autoRefresh();
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            initDetails();
        });
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 0;
            initDetails();
        });
    }

    @Override
    protected void initData() {

    }

    private void initDetails() {
        if (novelId == -1) return;
        if (issex != 1) {
            issex = 2;
        }
        Application application = AppContext.sInstance;
        AppContext appContext = (AppContext) application;
        HttpClient.getInstance().post(AllApi.getbooks, AllApi.getbooks)
                .params("p", page)
                .params("size", PAGE_SIZE)
                .params("issex", issex)
                .params("novel_id", novelId)
                .params("copyright", appContext.getTenjinFlag())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        hideDialog();
                        List<String> strings = Arrays.asList(info);
                        if (page == 0) {
                            classificationBookAdapter.setNewInstance(null);
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                        if (strings.size() > 0) {
                            for (String string : strings) {
                                classificationBookAdapter.addData(new Gson().fromJson(string, ClassficationBookBean.class));
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        hideDialog();
                        if (page == 0) {
                            refreshLayout.finishRefresh();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                });

    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        ArrayList<ClassificationTitleBean> selectList = new ArrayList<>();
        for (ClassificationTitleBean datum : classificationTitleAdapter.getData()) {
            if (datum.getNovel_id() == (classificationTitleAdapter.getItem(position).getNovel_id())) {
                datum.setSelect(true);
                novelId = datum.getNovel_id();
            } else {
                datum.setSelect(false);
            }
            selectList.add(datum);
        }
        classificationTitleAdapter.setNewInstance(selectList);
        page = 0;
        refreshLayout.autoRefresh();
    }
}