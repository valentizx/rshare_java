package me.rex.sdk.twitter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

import me.rex.sdk.share.RShareListener;
import me.rex.sdk.share.RSharePlatform;
import me.rex.sdk.util.RFileHelper;

final public class RTwitterTweetResultReciver extends BroadcastReceiver {

    private RShareListener mListener = RTwitterManager.getInstance().getShareListener();
    private Context mContext = RTwitterManager.getInstance().getContext();

    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 处理发推结果
         **/
        if (mListener != null) {
            if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {

                mListener.onComplete(RSharePlatform.Platform.Twitter);

            } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {

                // 重复的发推会发送失败, 就像新浪微博一样不能重复发同样的内容
                mListener.onFail(RSharePlatform.Platform.Twitter, "发推失败");

            } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {

                mListener.onCancel(RSharePlatform.Platform.Twitter);
            }
        }
        RFileHelper.deleteExternalShareDirectory(mContext);

    }
}
