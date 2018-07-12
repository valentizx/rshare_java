package me.rex.sdk.twitter;

import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

final public class RTwitterAuthHelper {


    private RTwitterAuthCallback mCallback;

    private static RTwitterAuthHelper mManager;

    private RTwitterAuthHelper(){}
    protected static RTwitterAuthHelper getInstance(){
        if (mManager == null) {
            synchronized (RTwitterManager.class) {
                if (mManager == null) {
                    mManager = new RTwitterAuthHelper();
                }
            }
        }
        return mManager;
    }

    protected void authorizeTwitter(Context context, RTwitterAuthCallback callback) {
        mCallback = callback;
        Intent intent = new Intent(context, RTwitterAuthActivity.class);
        context.startActivity(intent);

    }
    protected boolean hasLogged() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        return session != null;
    }

    protected RTwitterAuthCallback getAuthCallback() { return mCallback; }

}
