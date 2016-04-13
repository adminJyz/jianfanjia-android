package com.jianfanjia.cn.view.custom_annotation_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.requirement.MyDesignerActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.ImageShow;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description: com.jianfanjia.cn.view.baseview
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-22 10:46
 */
public class MyDesignerViewType8 extends RecyclerView.ViewHolder {

    @Bind(R.id.ltm_my_designer_left_layout)
    protected RelativeLayout ltm_my_designer_left_layout;

    @Bind(R.id.ltm_my_designer_head)
    protected ImageView headView;

    @Bind(R.id.ltm_my_designer_name)
    protected TextView nameView;

    @Bind(R.id.ltm_my_identity_auth)
    protected ImageView identityAuthView;

    @Bind(R.id.ltm_my_info_auth)
    protected ImageView infoAuthView;

    @Bind(R.id.ltm_my_ratingBar)
    protected RatingBar ratingBarView;

    @Bind(R.id.ltm_my_designer_status)
    protected TextView statusView;

    @Bind(R.id.ltm_my_designer_content)
    protected LinearLayout contentLayout;

    @Bind(R.id.merger_button1_layout)
    protected RelativeLayout merger_button1_layout;

    @Bind(R.id.merger_button2_layout)
    protected RelativeLayout merger_button2_layout;

    @Bind(R.id.merger_button1)
    protected TextView button1;

    @Bind(R.id.merger_button2)
    protected TextView button2;

    private Context context;

    public MyDesignerViewType8(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view);
        this.context = context;
    }

    public static MyDesignerViewType8 build(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_my_designer_type1, null);
        return new MyDesignerViewType8(view, context);
    }

    public void bind(Designer designerInfo, final ClickCallBack clickCallBack, final int position) {
        String status = designerInfo.getPlan().getStatus();
        String imageid = designerInfo.getImageid();
        String username = designerInfo.getUsername();
        ltm_my_designer_left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, MyDesignerActivity.VIEW_DESIGNER);
            }
        });
        if (!TextUtils.isEmpty(imageid)) {
            ImageShow.getImageShow().displayImageHeadWidthThumnailImage(context, imageid, headView);
        } else {
            headView.setImageResource(R.mipmap.icon_default_head);
        }
        if (!TextUtils.isEmpty(username)) {
            nameView.setText(username);
        } else {
            nameView.setText(context.getResources().getString(R.string.designer));
        }
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            infoAuthView.setVisibility(View.VISIBLE);
        } else {
            infoAuthView.setVisibility(View.GONE);
        }
        if (designerInfo.getUid_auth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            identityAuthView.setVisibility(View.VISIBLE);
        } else {
            identityAuthView.setVisibility(View.GONE);
        }
        int respond_speed = (int) designerInfo.getRespond_speed();
        int service_attitude = (int) designerInfo.getService_attitude();
        ratingBarView.setRating((respond_speed + service_attitude) / 2);
        if (designerInfo.getEvaluation() == null) {
            button1.setText(context.getResources().getString(R.string.str_comment_str));
            merger_button1_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, MyDesignerActivity.COMMENT);
                }
            });
        } else {
            button1.setText(context.getResources().getString(R.string.str_check_comment));
            merger_button1_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.click(position, MyDesignerActivity.VIEW_COMMENT);
                }
            });
        }
        merger_button2_layout.setVisibility(View.GONE);
        statusView.setText(context.getResources().getString(R.string.str_wait_upload_plan));
        statusView.setTextColor(context.getResources().getColor(R.color.grey_color));
    }
}
