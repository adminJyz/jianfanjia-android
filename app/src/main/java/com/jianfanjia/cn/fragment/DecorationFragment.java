package com.jianfanjia.cn.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jianfanjia.cn.activity.PreviewDecorationActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DecorationAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.List;

/**
 * Description:装修美图
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DecorationFragment extends BaseFragment implements ApiUiUpdateListener, PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = DecorationFragment.class.getName();
    private MainHeadView mainHeadView = null;
    private PullToRefreshRecycleView decoration_listview = null;
    private StaggeredGridLayoutManager mLayoutManager = null;
    private DecorationAdapter decorationAdapter = null;

    @Override
    public void initView(View view) {
        initMainHeadView(view);
        decoration_listview = (PullToRefreshRecycleView) view.findViewById(R.id.decoration_listview);
        decoration_listview.setMode(PullToRefreshBase.Mode.BOTH);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        decoration_listview.setLayoutManager(mLayoutManager);
        decoration_listview.setItemAnimator(new DefaultItemAnimator());
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        decoration_listview.addItemDecoration(decoration);
        searchDecorationImg();
    }

    private void initMainHeadView(View view) {
        mainHeadView = (MainHeadView) view.findViewById(R.id.dec_head);
        mainHeadView.setMianTitle(getResources().getString(R.string.decoration_img));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    @Override
    public void setListener() {
        decoration_listview.setOnRefreshListener(this);
    }

    private void searchDecorationImg() {
        JianFanJiaClient.searchDecorationImg(getContext(), "不限", "不限", "不限", "", -1, 0, 5, this, this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data.toString());
        DecorationItemInfo decorationItemInfo = JsonParser.jsonToBean(data.toString(), DecorationItemInfo.class);
        LogTool.d(TAG, "decorationItemInfo:" + decorationItemInfo);
        if (null != decorationItemInfo) {
            final List<BeautyImgInfo> beautyImgList = decorationItemInfo.getBeautiful_images();
            LogTool.d(TAG, "beautyImgList:" + beautyImgList);
            decorationAdapter = new DecorationAdapter(getActivity(), beautyImgList, new OnItemClickListener() {
                @Override
                public void OnItemClick(int position) {
                    BeautyImgInfo beautyImgInfo = beautyImgList.get(position);
                    String _id = beautyImgInfo.get_id();
                    LogTool.d(TAG, "_id:" + _id);
                    startActivity(PreviewDecorationActivity.class);
                }
            });
            decoration_listview.setAdapter(decorationAdapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        decoration_listview.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        decoration_listview.onRefreshComplete();
    }

    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {

        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    private ApiUiUpdateListener pullUpListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {

        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration;
    }

}
