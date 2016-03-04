package com.jianfanjia.cn.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.my.CollectActivity;
import com.jianfanjia.cn.activity.my.CustomerServiceActivity;
import com.jianfanjia.cn.activity.my.MyProcessActivity;
import com.jianfanjia.cn.activity.my.NotifyActivity;
import com.jianfanjia.cn.activity.my.SettingActivity;
import com.jianfanjia.cn.activity.my.UserInfoActivity_;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.LogTool;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Description:我的
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyFragment extends BaseFragment {
    private static final String TAG = MyFragment.class.getName();
    public static final int REQUESTCODE_USERINFO = 0;
    private RelativeLayout notifyLayout = null;
    private RelativeLayout my_collect_layout = null;
    private RelativeLayout my_designer_layout = null;
    private RelativeLayout my_site_layout = null;
    private RelativeLayout setting_layout = null;
    private RelativeLayout kefu_layout = null;
    private RelativeLayout my_info_layout = null;
    private ImageView head_img = null;
    private ImageView user_head_img = null;
    private TextView my_name = null;
    private TextView my_account = null;

    @Override
    public void initView(View view) {
        notifyLayout = (RelativeLayout) view.findViewById(R.id.notify_layout);
        my_collect_layout = (RelativeLayout) view.findViewById(R.id.collect_layout);
        my_designer_layout = (RelativeLayout) view.findViewById(R.id.my_designer_layout);
        my_site_layout = (RelativeLayout) view.findViewById(R.id.my_site_layout);
        setting_layout = (RelativeLayout) view.findViewById(R.id.setting_layout);
        my_info_layout = (RelativeLayout) view.findViewById(R.id.frag_my_info_layout);
        kefu_layout = (RelativeLayout) view.findViewById(R.id.kefu_layout);
        head_img = (ImageView) view.findViewById(R.id.head_img);
        user_head_img = (ImageView) view.findViewById(R.id.user_head_img);
        my_account = (TextView) view.findViewById(R.id.frag_my_account);
        my_name = (TextView) view.findViewById(R.id.frag_my_name);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void initMyInfo() {
        String imgPath = dataManager.getUserImagePath();
        LogTool.d(TAG, "imgPath=" + imgPath);
        if (!imgPath.contains(Constant.DEFALUT_PIC_HEAD)) {
//            imageShow.displayScreenWidthThumnailImage(getActivity(), imgPath, head_img);
            imageShow.displayImageHeadWidthThumnailImage(getActivity(), imgPath, user_head_img);
            imageShow.loadImage(imgPath, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    //网络头像加载失败就处理默认的背景
                    ImageUtil.blur(ImageUtil.drawableResToBitmap(getActivity().getApplicationContext(), R.mipmap.bg_my), head_img);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    //进行高斯模糊处理
                    ImageUtil.blur(loadedImage, head_img);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } else {
            user_head_img.setImageResource(R.mipmap.icon_default_head);
            ImageUtil.blur(ImageUtil.drawableResToBitmap(getActivity().getApplicationContext(), R.mipmap.bg_my), head_img);
        }
    }

    @Override
    public void setListener() {
        notifyLayout.setOnClickListener(this);
        my_collect_layout.setOnClickListener(this);
        my_designer_layout.setOnClickListener(this);
        my_site_layout.setOnClickListener(this);
        setting_layout.setOnClickListener(this);
        my_info_layout.setOnClickListener(this);
        kefu_layout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initMyInfo();
        my_name.setText(TextUtils.isEmpty(dataManager.getUserName()) ? getResources().getString(R.string.ower) : dataManager.getUserName());
        my_account.setText(TextUtils.isEmpty(dataManager.getAccount()) ? "" : "账号：" + dataManager.getAccount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notify_layout:
                startActivity(NotifyActivity.class);
                break;
            case R.id.collect_layout:
                startActivity(CollectActivity.class);
                break;
            case R.id.my_designer_layout:
                break;
            case R.id.my_site_layout:
                startActivity(MyProcessActivity.class);
                break;
            case R.id.frag_my_info_layout:
                startActivityForResult(UserInfoActivity_.class, REQUESTCODE_USERINFO);
                break;
            case R.id.kefu_layout:
                startActivity(CustomerServiceActivity.class);
                break;
            case R.id.setting_layout:
                startActivity(SettingActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUESTCODE_USERINFO:
                initMyInfo();
                break;
        }
    }
}
