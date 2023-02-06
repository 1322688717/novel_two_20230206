package cc.ixcc.noveltwo.ui.fragment;

import static cc.ixcc.noveltwo.Constants.PAGE_SIZE;

import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jiusen.base.BaseAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.ixcc.noveltwo.Constants;
import cc.ixcc.noveltwo.R;
import cc.ixcc.noveltwo.bean.BookMoreBean;
import cc.ixcc.noveltwo.bean.StackRoomBookBean;
import cc.ixcc.noveltwo.bean.StackRoomChannelBean;
import cc.ixcc.noveltwo.common.MyFragment;
import cc.ixcc.noveltwo.http.AllApi;
import cc.ixcc.noveltwo.http.HttpCallback;
import cc.ixcc.noveltwo.http.HttpClient;
import cc.ixcc.noveltwo.treader.AppContext;
import cc.ixcc.noveltwo.ui.activity.HomeActivity;
import cc.ixcc.noveltwo.ui.activity.my.BookDetailActivity;
import cc.ixcc.noveltwo.ui.adapter.GenresLeftAdapter;
import cc.ixcc.noveltwo.ui.adapter.myAdapter.BookMoreAdapter;

/**
 * desc   : Genres的Fragment
 */
public class GenresFragment extends MyFragment<HomeActivity> implements GenresLeftAdapter.OnLeftItemClickListener {
    @BindView(R.id.rl_refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.rv_left_view)
    RecyclerView mRvLeftView;
    @BindView(R.id.rv_right_view)
    RecyclerView mRvRightView;
    private MMKV mmkv = MMKV.defaultMMKV();
    private GenresLeftAdapter mLeftAdapter;
    private BookMoreAdapter mRightAdapter;
    int page = 1;
    List<BookMoreBean.ListBean> booklist = new ArrayList<>();
    private int id;
    private int type;

    public static GenresFragment newInstance() {
        return new GenresFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.genres_fragment;
    }

    @Override
    protected void initView() {

        //左侧列表
        mRvLeftView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new GenresLeftAdapter();
        mRvLeftView.setAdapter(mLeftAdapter);
        mLeftAdapter.setOnLeftItemClickListener(this);
        //右侧列表
        mRvRightView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new BookMoreAdapter(getContext());
        mRvRightView.setAdapter(mRightAdapter);
        mRvRightView.setNestedScrollingEnabled(false);
        mRightAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(getContext(), booklist.get(position).getAnid());
            }
        });
        initMoreRv();
    }

    @Override
    protected void initData() {
//        getPopularData();
        getGenresData();
    }
//    private void getPopularData(){
//        HttpClient.getInstance().get(AllApi.bookchannel, AllApi.bookchannel)
//                .execute(new HttpCallback() {
//                    @Override
//                    public void onSuccess(int code, String msg, String[] info) {
//                        for (int i = 0; i < info.length; i++) {
//                            StackRoomChannelBean resultBean = new Gson().fromJson(info[i], StackRoomChannelBean.class);
//                            //请求接口
//                            if(resultBean.getTitle().equals("Popular")){
//                                getGenresData(resultBean.getId());
//                            }
//
//                        }
//                    }
//                });
//    }

    private void getGenresData() {
        HttpClient.getInstance().get(AllApi.genrestackroombook, AllApi.genrestackroombook)
                .params("page", page)
                .params("page_size", PAGE_SIZE)
                .params("copyright", AppContext.sInstance.getTenjinFlag())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        StackRoomBookBean bean = new Gson().fromJson(info[0], StackRoomBookBean.class);
                        hideDialog();
//                        if (bean.getColumn() == null || bean.getColumn().isEmpty()) {
//                            refreshLayout.setNoMoreData(true);
//                            return;
//                        }
//                        switch (refreshLayout.getState()) {
//                            case Refreshing:
//                                stackItemList.clear();
//                                refreshLayout.finishRefresh();
//                                break;
//                            case Loading:
//                                refreshLayout.finishLoadMore();
//                                break;
//                        }
                        Log.e("GenresFragment","StackRoomBookBean"+bean);
                        mLeftAdapter.setData(bean.getColumn());
                        id = bean.getColumn().get(0).getId();
                        type = bean.getColumn().get(0).getType();
                        getInfo();
                    }

                    @Override
                    public void onError() {
                        super.onError();
                        hideDialog();
//                        switch (refreshLayout.getState()) {
//                            case Refreshing:
//                                refreshLayout.finishRefresh();
//                                break;
//                            case Loading:
//                                refreshLayout.finishLoadMore();
//                                break;
//                        }
                    }
                });
    }

    @Override
    public void onLeftItemClick(StackRoomBookBean.ColumnBean item) {
        id = item.getId();
        type = item.getType();
        page = 1;
        getInfo();

    }

    private void getInfo() {
        Log.e("ZCQ","copyright==="+AppContext.sInstance.getTenjinFlag());
        HttpClient.getInstance().get(AllApi.getbookmore, AllApi.getbookmore)
                .params("channel_id", id)
                .params("type",  Constants.more_xinshu)
                .params("page", page)
                .params("page_size", PAGE_SIZE)
                .params("copyright", AppContext.sInstance.getTenjinFlag())
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        Log.e("GenresFragment","getInfo"+info);
                        BookMoreBean bean = new Gson().fromJson(info[0], BookMoreBean.class);
                        List<BookMoreBean.ListBean> list = bean.getList();
                        if (page == 1) booklist.clear();
                        booklist.addAll(list);
                        Log.e("GenresFragment", "booklist" + bean);
                        mRightAdapter.setData(booklist);
                        if (page == 1) {
                            refresh.finishRefresh(500);
                        } else {
                            refresh.finishLoadMore(500);
                        }
                    }
                });
    }

    private void initMoreRv() {
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getInfo();
            }
        });
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getInfo();
            }
        });

    }

}
