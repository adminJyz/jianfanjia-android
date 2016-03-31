package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SectionItemGridViewAdapter extends BaseListAdapter<String> {

    public SectionItemGridViewAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View initView(final int position, View convertView) {
        String imgUrl = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_item_check_pic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (imgUrl.equals(Constant.HOME_ADD_PIC)) {
            holder.img.setImageResource(R.mipmap.btn_icon_home_add);
        } else {
            imageShow.displayThumbnailImage(imgUrl, holder.img, MyApplication.dip2px(context, Global.PIC_WIDTH_NODE));
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.img)
        public ImageView img = null;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
