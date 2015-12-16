package com.jianfanjia.cn.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jianfanjia.cn.activity.PreviewDecorationActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DecorationImgAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.DecorationItemInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.SpacesItemDecoration;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

import java.util.List;

/**
 * @author fengliang
 * @ClassName: DecorationImgFragment
 * @Description: 装修美图收藏
 * @date 2015-8-26 下午1:07:52
 */
public class DecorationImgFragment extends BaseFragment {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView decoration_img_listview = null;
    private DecorationImgAdapter decorationImgAdapter = null;
    private String decorationid = null;
    private int itemPosition = -1;

    @Override
    public void initView(View view) {
        decoration_img_listview = (RecyclerView) view.findViewById(R.id.decoration_img_listview);
        decoration_img_listview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        decoration_img_listview.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        decoration_img_listview.addItemDecoration(decoration);
        getDecorationImgList(0, 8, getDecorationImgListListener);
    }

    @Override
    public void setListener() {

    }


    private void getDecorationImgList(int from, int limit, ApiUiUpdateListener listener) {
        JianFanJiaClient.getBeautyImgListByUser(getActivity(), from, limit, listener, this);
    }

    private void deleteDecorationImg(String id) {
        JianFanJiaClient.deleteBeautyImgByUser(getActivity(), id, deleteDecorationImgListListener, this);
    }

    private ApiUiUpdateListener getDecorationImgListListener = new ApiUiUpdateListener() {
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
                decorationImgAdapter = new DecorationImgAdapter(getActivity(), beautyImgList, new RecyclerViewOnItemClickListener() {

                    @Override
                    public void OnItemClick(View view, int position) {
                        BeautyImgInfo beautyImgInfo = beautyImgList.get(position);
                        LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
                        decorationid = beautyImgInfo.get_id();
                        Intent decorationIntent = new Intent(getActivity(), PreviewDecorationActivity.class);
                        Bundle decorationBundle = new Bundle();
                        decorationBundle.putString(Global.DECORATION_ID, decorationid);
                        decorationIntent.putExtras(decorationBundle);
                        startActivity(decorationIntent);
                    }

                    @Override
                    public void OnLongItemClick(View view, int position) {
                        BeautyImgInfo beautyImgInfo = beautyImgList.get(position);
                        LogTool.d(TAG, "beautyImgInfo:" + beautyImgInfo);
                        decorationid = beautyImgInfo.get_id();
                        LogTool.d(TAG, "decorationid =" + decorationid);
                        deleteDecorationImgDialog();
                    }

                    @Override
                    public void OnViewClick(int position) {

                    }
                });
                decoration_img_listview.setAdapter(decorationImgAdapter);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private ApiUiUpdateListener deleteDecorationImgListListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            decorationImgAdapter.remove(itemPosition);
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private void deleteDecorationImgDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setTitle("移出装修美图");
        dialog.setMessage("确定把该美图移除吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteDecorationImg(decorationid);
                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_decoration_img;
    }

}
