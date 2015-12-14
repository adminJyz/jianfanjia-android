package com.jianfanjia.cn.fragment;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.jianfanjia.cn.activity.DesignerInfoActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.FavoriteDesignerAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.MyFavoriteDesigner;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.baseview.HorizontalDividerItemDecoration;
import com.jianfanjia.cn.view.dialog.CommonDialog;
import com.jianfanjia.cn.view.dialog.DialogHelper;

/**
 * @author fengliang
 * @ClassName: MyFavoriteDesignerFragment
 * @Description: 我的意向设计师
 * @date 2015-8-26 下午1:07:52
 */
public class MyFavoriteDesignerFragment extends BaseFragment implements ApiUiUpdateListener, RecyclerViewOnItemClickListener {
    private static final String TAG = DecorationImgFragment.class.getName();
    private RecyclerView my_favorite_designer_listview = null;
    private MyFavoriteDesigner myFavoriteDesigner = null;

    @Override
    public void initView(View view) {
        my_favorite_designer_listview = (RecyclerView) view.findViewById(R.id.my_favorite_designer_listview);
        my_favorite_designer_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_favorite_designer_listview.setItemAnimator(new DefaultItemAnimator());
        Paint paint = new Paint();
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        my_favorite_designer_listview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).showLastDivider().build());
        JianFanJiaClient.get_MyFavoriteDesignerList(getActivity(), 0, 100, this, this);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data=" + data.toString());
        myFavoriteDesigner = JsonParser.jsonToBean(data.toString(), MyFavoriteDesigner.class);
        LogTool.d(TAG, "myFavoriteDesigner=" + myFavoriteDesigner);
        if (myFavoriteDesigner != null) {
            FavoriteDesignerAdapter adapter = new FavoriteDesignerAdapter(getActivity(), myFavoriteDesigner.getDesigners(), this);
            my_favorite_designer_listview.setAdapter(adapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {

    }

    @Override
    public void OnItemClick(View view, int position) {
        String designerId = myFavoriteDesigner.getDesigners().get(position).get_id();
        LogTool.d(this.getClass().getName(), designerId);
        Bundle designerBundle = new Bundle();
        designerBundle.putString(Global.DESIGNER_ID, designerId);
        startActivity(DesignerInfoActivity.class, designerBundle);
    }

    @Override
    public void OnLongItemClick(View view, int position) {
        LogTool.d(TAG, "position:" + position);
        deleteFavoriteDesignerDialog();
    }


    private void deleteFavoriteDesignerDialog() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setTitle("移出设计师");
        dialog.setMessage("确定把该设计师移出意向列表吗？");
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        dialog.setNegativeButton(R.string.no, null);
        dialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_favorite_designer;
    }


}
