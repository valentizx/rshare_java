package me.rex.sdk.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import me.rex.sdk.share.RShare;
import me.rex.sdk.share.RShareListener;
import me.rex.sdk.share.RSharePlatform;
import me.rex.sdk.util.RFileHelper;
import me.rex.sdk.util.RPlatformHelper;
import me.rex.sdk.util.RThreadManager;

final public class RQqActivity extends Activity {


    private RQqHelper mHelper;
    private Tencent mTencent;
    private Context mContext;
    private RShareListener mListener ;

    private IUiListener mQqShareListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (mListener != null) {
                mListener.onComplete(RSharePlatform.Platform.QQ);
            }
            finish();
        }

        @Override
        public void onError(UiError uiError) {
            if (mListener != null) {
                mListener.onFail(RSharePlatform.Platform.QQ, uiError.errorMessage);
            }
            finish();
        }

        @Override
        public void onCancel() {
            if (mListener != null) {
                mListener.onCancel(RSharePlatform.Platform.QQ);
            }
            finish();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new RQqHelper(getIntent());
        mContext = RQqManager.getInstance().getContext();
        mListener = RQqManager.getInstance().getListener();
        handleIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mQqShareListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /** 释放资源 **/
        if (mTencent != null) {
            mTencent.releaseResource();
        }
        RFileHelper.deleteExternalShareDirectory(mContext);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        RShare.ShareContentType type = (RShare.ShareContentType) intent.getSerializableExtra("type");
        if (type == RShare.ShareContentType.Webpage) {
            share(mHelper.getQQWebpageParams());
        } else if (type == RShare.ShareContentType.Photo) {
            share(mHelper.getQQImageParams());
        } else if (type == RShare.ShareContentType.Music) {
            share(mHelper.getQQMusicParams());
        } else if (type == RShare.ShareContentType.App) {
            share(mHelper.getQQAppParams());
        }
    }

    /**
     *
     * 初始化 QQ SDK.
     * **/
    protected void sdkInitialize() {
        String appId = RPlatformHelper.getQQAppId(mContext);
        mTencent = Tencent.createInstance(appId, mContext);
    }

    private void share(final Bundle params) {
        sdkInitialize();
        // QQ分享要在主线程做
        RThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                if (null != mTencent) {
                    mTencent.shareToQQ(RQqActivity.this, params, mQqShareListener);
                }
            }
        });

    }

}
