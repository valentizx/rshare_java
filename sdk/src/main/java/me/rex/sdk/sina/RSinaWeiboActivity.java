package me.rex.sdk.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sina.weibo.sdk.api.StoryMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import me.rex.sdk.RShare;
import me.rex.sdk.RShareListener;
import me.rex.sdk.RSharePlatform;
import me.rex.sdk.util.RFileHelper;


final public class RSinaWeiboActivity extends Activity implements WbShareCallback {


    private WbShareHandler mHandler;
    private RShareListener mListener = RSinaWeiboManager.getInstance().getListener();
    private Context mContext = RSinaWeiboManager.getInstance().getContext();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new WbShareHandler(this);
        mHandler.registerApp();
        handleIntent();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mHandler.doResultIntent(intent, this);
    }

    @Override
    public void onWbShareCancel() {
        if (mListener != null) {
            mListener.onCancel(RSharePlatform.Platform.Sina);
        }
        shareFinish();
    }

    @Override
    public void onWbShareFail() {
        if (mListener != null) {
            mListener.onFail(RSharePlatform.Platform.Sina, "分享失败");
        }
        shareFinish();
    }

    @Override
    public void onWbShareSuccess() {
        if (mListener != null) {
            mListener.onComplete(RSharePlatform.Platform.Sina);
        }
        shareFinish();
    }

    /**
     * 处理 Intent.
     */
    private void handleIntent() {
        Intent intent = getIntent();
        RShare.ShareContentType type = (RShare.ShareContentType)intent.getSerializableExtra("type");

        RSinaWeiboHelper helper = new RSinaWeiboHelper(intent);

        if (type == RShare.ShareContentType.Text) {
            shareWeibo(helper.getTextMessage());
        } else if (type == RShare.ShareContentType.Photo) {
            shareWeibo(helper.getImageMessage());
        } else if (type == RShare.ShareContentType.Webpage) {
            shareWeibo(helper.getWebpageMessage());
        } else if (type == RShare.ShareContentType.Video) {
            shareWeibo(helper.getLocalVideoMessage());
        }

    }

    // 分享到微博
    private void shareWeibo(WeiboMultiMessage message) {
        mHandler.shareMessage(message, true);
    }

    private void shareFinish() {
        mHandler = null;
        RFileHelper.deleteExternalShareDirectory(mContext);
        finish();
    }

}
