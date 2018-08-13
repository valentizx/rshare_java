package me.rex.sdk.tumblr;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.rex.sdk.share.RShare;
import me.rex.sdk.share.RShareListener;

/**
 * Tumblr <a href=https://developer.yahoo.com/flurry/docs/tumblrsharing/android/> Tumblr SDK </a>
 * */
final public class RTumblrManager extends RShare {


    private Context mContext;
    private RShareListener mListener;

    private static RTumblrManager mManager;


    private RTumblrManager(){}
    public static RTumblrManager getInstance(){
        if (mManager == null) {
            synchronized (RTumblrManager.class) {
                if (mManager == null) {
                    mManager = new RTumblrManager();
                }
            }
        }
        return mManager;
    }


    /**
     *
     * 分享图片到 Tumblr. (主体为图片)
     *
     * @param context 上下文.
     * @param imageUrl 可以是本地也可以是网络图片的 Url.
     * @param description 博文.
     * @param webpageUrl 附带网链.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareImage(@NonNull Context context,
                           @NonNull String imageUrl,
                           @Nullable String description,
                           @Nullable String webpageUrl,
                           @Nullable RShareListener listener) {
        mContext = context;
        mListener = listener;

        Intent intent = new Intent(context, RTumblrActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("description", description);
        intent.putExtra("webpageUrl", webpageUrl);
        intent.putExtra("type", ShareContentType.Photo);
        context.startActivity(intent);
    }

    /**
     * 分享文字到 Tumblr (文字为主体).
     * @param context 上下文.
     * @param body 博文.
     * @param title 博文标题.
     * @param webpageUrl 当前控制器.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareText(@NonNull Context context,
                          @NonNull String body,
                          @Nullable String title,
                          @Nullable String webpageUrl,
                          @Nullable RShareListener listener) {


        mContext = context;
        mListener = listener;

        Intent intent = new Intent(context, RTumblrActivity.class);
        intent.putExtra("body", body);
        intent.putExtra("title", title);
        intent.putExtra("webpageUrl", webpageUrl);
        intent.putExtra("type", ShareContentType.Text);
        context.startActivity(intent);

    }


    protected Context getContext() {
        return mContext;
    }

    protected RShareListener getListener() {
        return mListener;
    }

}
