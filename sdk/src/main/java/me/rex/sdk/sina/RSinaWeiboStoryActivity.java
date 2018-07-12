package me.rex.sdk.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sina.weibo.sdk.api.StoryMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import me.rex.sdk.RShare;
import me.rex.sdk.RShareListener;
import me.rex.sdk.RSharePlatform;
import me.rex.sdk.util.RFileHelper;

final public class RSinaWeiboStoryActivity extends Activity  implements WbShareCallback {

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
        RFileHelper.deleteExternalShareDirectory(mContext);
        mHandler = null;
        finish();
    }

    @Override
    public void onWbShareFail() {
        if (mListener != null) {
            mListener.onFail(RSharePlatform.Platform.Sina, "分享失败");
        }
        RFileHelper.deleteExternalShareDirectory(mContext);
        mHandler = null;
        finish();
    }

    @Override
    public void onWbShareSuccess() {
        if (mListener != null) {
            mListener.onComplete(RSharePlatform.Platform.Sina);
        }
        RFileHelper.deleteExternalShareDirectory(mContext);
        mHandler = null;
        finish();
    }

    /**
     * 处理 Intent.
     */
    private void handleIntent() {
        Intent intent = getIntent();
        RShare.ShareContentType type = (RShare.ShareContentType)intent.getSerializableExtra("type");

        RSinaWeiboHelper helper = new RSinaWeiboHelper(intent);

        if (type == RShare.ShareContentType.Photo) {
            shareStory(helper.getPhotoStoryMessage());
        }
        if (type == RShare.ShareContentType.Video) {
            shareStory(helper.getVideoStoryMessage());
        }

    }
    // 分享到「微博故事」
    private void shareStory(StoryMessage message) {
        mHandler.shareToStory(message);
    }

}
