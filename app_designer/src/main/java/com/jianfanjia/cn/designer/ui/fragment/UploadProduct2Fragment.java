package com.jianfanjia.cn.designer.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.api.request.common.UploadPicRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.UploadProductActivity;
import com.jianfanjia.cn.designer.ui.adapter.UploadProductAdapter;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.common.tool.ImageUtil;
import com.jianfanjia.common.tool.LogTool;

import java.io.File;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.designer.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-23 10:05
 */
public class UploadProduct2Fragment extends BaseFragment {

    private static final String TAG = UploadProduct2Fragment.class.getName();
    @Bind(R.id.upload_product_head_layout)
    MainHeadView mMainHeadView;

    private UploadProductActivity mUploadProductActivity;

    @Bind(R.id.upload_product_recyclerview)
    RecyclerView mRecyclerView;

    UploadProductAdapter mUploadProductAdapter;

    private int currentAddType;

    private Product mProduct;

    public static UploadProduct2Fragment getInstance(Product product) {
        UploadProduct2Fragment uploadProduct2Fragment = new UploadProduct2Fragment();
        return uploadProduct2Fragment;
    }

    public Product getmProduct() {
        return mProduct;
    }

    public void setmProduct(Product mProduct) {
        this.mProduct = mProduct;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUploadProductActivity = (UploadProductActivity) getActivity();
        initView();
    }

    @OnClick({R.id.head_back_layout, R.id.head_right_title})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                if (mUploadProductActivity != null) {
                    mUploadProductActivity.lastFragment();
                }
                break;
            case R.id.head_right_title:
                if (mUploadProductActivity != null) {
//                    mUploadProductActivity.nextFragment();
                }
                break;
        }
    }

    private void initView() {
        initMainView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mUploadProductAdapter = new UploadProductAdapter(getContext(), mProduct);
        mRecyclerView.setAdapter(mUploadProductAdapter);

        mUploadProductAdapter.setAddProductImageListener(new UploadProductAdapter.AddProductImageListener() {
            @Override
            public void addProductImage(int type) {
                currentAddType = type;
                pickPicture();
            }
        });
    }

    private void pickPicture() {
        PhotoPickerIntent intent1 = new PhotoPickerIntent(getContext());
        intent1.setPhotoCount(1);
        intent1.setShowGif(false);
        intent1.setShowCamera(true);
        startActivityForResult(intent1, Constant.REQUESTCODE_PICKER_PIC);
    }

    private void upload_image(final Bitmap bitmap) {
        UploadPicRequest uploadPicRequest = new UploadPicRequest();
        uploadPicRequest.setBytes(com.jianfanjia.common.tool.ImageUtil.transformBitmapToBytes(bitmap));
        Api.uploadImage(uploadPicRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                ProductImageInfo productImageInfo = new ProductImageInfo();
                productImageInfo.setImageid(apiResponse.getData());
                if(currentAddType == UploadProductAdapter.UPLOAD_TYPE_EFFECT){
//                    mProduct.getImages().add(productImageInfo);
                    mUploadProductAdapter.addEffectItem(productImageInfo);
                }else {
//                    mProduct.getPlan_images().add(productImageInfo);
                    mUploadProductAdapter.addPlanItem(productImageInfo);
                }
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_PICKER_PIC:
                if (data != null) {
                    List<String> photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                    for (String path : photos) {
                        Bitmap imageBitmap = ImageUtil.getImage(path);
                        LogTool.d(TAG, "imageBitmap: path :" + path);
                        if (null != imageBitmap) {
                            upload_image(imageBitmap);
                        }
                    }
                }
                break;
        }
    }

    private void initMainView() {
        mMainHeadView.setMianTitle(getString(R.string.upload_product));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_upload_product2;
    }
}
