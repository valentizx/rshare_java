package me.rex.sdk.twitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

final public class RTwitterAuthActivity extends Activity {

    private TwitterAuthClient mClient;
    private RTwitterAuthCallback mCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallback = RTwitterAuthHelper.getInstance().getAuthCallback();
        mClient = new TwitterAuthClient();
        mClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                if (mCallback != null) {
                    mCallback.onComplete();
                }
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                if (mCallback != null) {
                    mCallback.onFail(exception.toString());
                }
                finish();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 接收授权 Twitter 客户端的结果.
         * */
        mClient.onActivityResult(requestCode, resultCode, data);
    }
}
