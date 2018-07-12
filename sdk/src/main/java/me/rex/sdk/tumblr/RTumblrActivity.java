package me.rex.sdk.tumblr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;


import com.flurry.android.FlurryAgent;
import com.flurry.android.tumblr.Post;
import com.flurry.android.tumblr.PostListener;
import com.flurry.android.tumblr.TumblrShare;

import me.rex.sdk.RShare;
import me.rex.sdk.RShareListener;
import me.rex.sdk.RSharePlatform;
import me.rex.sdk.util.RPlatformHelper;

final public class RTumblrActivity extends Activity {

    private final String TAG = "RTumblrActivity==>";



    private RTumblrManager mManager = RTumblrManager.getInstance();
    private RShareListener mListener = mManager.getListener();


    /**
     *
     * Tumblr 提供的 Post 结果监听.
     * **/
    private PostListener mPostListener = new PostListener() {
        @Override
        public void onPostSuccess(Long aLong) {
            if (mListener != null) {
                mListener.onComplete(RSharePlatform.Platform.Tumblr);
            }
            finish();
        }

        @Override
        public void onPostFailure(String s) {
            Log.e(TAG, s);
            if (mListener != null) {
                if (s.equals("Post cancelled.")) {
                    mListener.onCancel(RSharePlatform.Platform.Tumblr);
                } else {
                    mListener.onFail(RSharePlatform.Platform.Tumblr, s);
                }
            }

            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sdkInitialize();
        handleIntent();
    }


    /**
     * 初始化 Tumblr SDK.
     */
    private void sdkInitialize() {

        Context context = mManager.getContext();



        String key = RPlatformHelper.getTumblrConsumerKey(context);
        String secret = RPlatformHelper.getTumblrConsumerSecret(context);
        String flurryKey = RPlatformHelper.getTumblrFlurryKey(context);

        FlurryAgent.setLogEnabled(true);

        FlurryAgent.init(this, flurryKey);

        TumblrShare.setOAuthConfig(key, secret);

    }

    private void  handleIntent() {
        Intent intent = getIntent();
        RTumblrHelper helper = new RTumblrHelper(intent);
        RShare.ShareContentType type = (RShare.ShareContentType)intent.getSerializableExtra("type");
        if (type  == RShare.ShareContentType.Photo) {
            post(helper.getImageParam());
        } else {
            post(helper.getTextParam());
        }
    }

    /** 分享 */
    private void post(Post param) {
        sdkInitialize();

        param.setPostListener(mPostListener);
        TumblrShare.post(this, param);
    }

    /** 监听「返回」的处理 **/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mListener != null) {
            mListener.onCancel(RSharePlatform.Platform.Tumblr);
        }
    }
}
