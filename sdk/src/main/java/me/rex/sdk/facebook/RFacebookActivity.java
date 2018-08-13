package me.rex.sdk.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import me.rex.sdk.share.RShare;
import me.rex.sdk.share.RSharePlatform;
import me.rex.sdk.util.RFileHelper;

final public class RFacebookActivity extends Activity {

    private final String TAG = "RFacebookActivity";

    CallbackManager mCbm;
    ShareDialog mSd;
    FacebookCallback mCb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();
    }

    /**
     * 处理 Intent.
     */
    private void handleIntent() {

        Intent intent = getIntent();
        RFacebookHelper helper = new RFacebookHelper(intent);
        if (intent.getSerializableExtra("type") == RShare.ShareContentType.Webpage) {
            shareWebpage(helper.getLinkContent(), helper.getShareMode());
        } else if (intent.getSerializableExtra("type") == RShare.ShareContentType.Photo) {
            sharePhoto(helper.getPhotoCotent(), ShareDialog.Mode.NATIVE);
        } else if (intent.getSerializableExtra("type") == RShare.ShareContentType.Video) {
            shareVideo(helper.getVideoContent(), ShareDialog.Mode.NATIVE);
        }
    }

    /**
     * 注册分享结果监听.
     */
    private void registerCallback() {

        final Context context = RFacebookManager.getInstance().getContext();

        mCbm = CallbackManager.Factory.create();
        mSd = new ShareDialog(this);
        mCb = new FacebookCallback() {
            @Override
            public void onSuccess(Object o) {
                if (RFacebookManager.getInstance().getListener() != null) {
                    RFacebookManager.getInstance().getListener().onComplete(RSharePlatform
                            .Platform.Facebook);
                }
                RFileHelper.deleteExternalShareDirectory(context);
                finish();
            }

            @Override
            public void onCancel() {
                if (RFacebookManager.getInstance().getListener() != null) {
                    RFacebookManager.getInstance().getListener().onCancel(RSharePlatform
                            .Platform.Facebook);
                }
                RFileHelper.deleteExternalShareDirectory(context);
                finish();

            }

            @Override
            public void onError(FacebookException error) {
                if (RFacebookManager.getInstance().getListener() != null) {
                    RFacebookManager.getInstance().getListener().onFail(RSharePlatform
                                    .Platform.Facebook,
                            error.toString());
                }
                RFileHelper.deleteExternalShareDirectory(context);
                finish();

            }
        };
        mSd.registerCallback(mCbm, mCb);
    }

    // 分享网页
    private void shareWebpage(ShareLinkContent content, ShareDialog.Mode mode) {
        registerCallback();
        if (mSd.canShow(content, mode)) {
            mSd.show(content, mode);
        }
    }
    // 分享图片
    private void sharePhoto(SharePhotoContent content, ShareDialog.Mode mode) {
        registerCallback();
        if (mSd.canShow(content, mode)) {
            if (mode == ShareDialog.Mode.NATIVE || mode == ShareDialog.Mode.AUTOMATIC) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();
            }
            mSd.show(content, mode);
        }
    }
    // 分享视频
    private void shareVideo(ShareVideoContent content, ShareDialog.Mode mode) {
        registerCallback();
        if (mSd.canShow(content, mode)) {
            mSd.show(content, mode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCbm.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
