package me.rex.sdk.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.util.ArrayList;

import me.rex.sdk.RShare;
import me.rex.sdk.RShareListener;
import me.rex.sdk.RSharePlatform;
import me.rex.sdk.util.RFileHelper;
import me.rex.sdk.util.RPlatformHelper;

/**
 * 新浪微博 SDK <a href=https://github.com/sinaweibosdk/weibo_android_sdk>Android 版 SDK</a>
 * */
final public class RSinaWeiboManager extends RShare {


    private final String TAG = "RSinaWeiboManager";

    private RShareListener mListener;
    private Context mContext;

    private static RSinaWeiboManager mManager;

    private RSinaWeiboManager(){}
    public static RSinaWeiboManager getInstance(){
        if (mManager == null) {
            synchronized (RSinaWeiboManager.class) {
                if (mManager == null) {
                    mManager = new RSinaWeiboManager();
                }
            }
        }
        return mManager;
    }

    /**
     * 初始化新浪 SDK.
     * **/
    protected void sdkInitialize(Context context) {

        String key = RPlatformHelper.getSinaWeiboAppKey(context);
        String redirectUrl = RPlatformHelper.getSinaWeiboRedirectUrl(context);
        String scope = RPlatformHelper.getSinaWeiboScope(context);
        WbSdk.install(context, new AuthInfo(context, key, redirectUrl, scope));
        WbShareHandler handler = new WbShareHandler((Activity) context);
        handler.registerApp();
    }


    /**
     * 分享文字.
     * @param context 上下文.
     * @param text 分享的博文内容.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareText(@NonNull Context context,
                          @NonNull String text,
                          @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Sina)) {
            Log.e(TAG, "新浪微博未安装");
            return;
        }
        sdkInitialize(context);
        mListener = listener;

        Intent intent = new Intent(context, RSinaWeiboActivity.class);
        intent.putExtra("text", text);
        intent.putExtra("type", ShareContentType.Text);
        context.startActivity(intent);

    }


    /**
     * 分享图片.
     * @param context 上下文.
     * @param photos Bitmaps 格式的图片数组, 单张图片不能超过过 10 MB, 最多 9 张.
     * @param text 博文, 在开启「分享到微博故事」的功能下会失效.
     * @param isToStory 是否开启「分享到微博故事」.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void sharePhoto(@NonNull Context context,
                           @NonNull ArrayList<Bitmap> photos,
                           @Nullable String text,
                           Boolean isToStory,
                           @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Sina)) {
            Log.e(TAG, "新浪微博未安装");
            return;
        }

        if (isToStory && photos.size() > 1) {
            Log.e(TAG, "「分享到微博故事」功能开启的情况下, 图片只能传一张!!!");
            return;
        }
        if ((!isToStory && photos.size() == 0) || (!isToStory && photos.size() > 9)) {
            Log.e(TAG, "分享到微博的图片数目不可以为0或者大于9!!!");
            return;
        }

        sdkInitialize(context);
        mListener = listener;
        mContext = context;

        Class cls = isToStory ? RSinaWeiboStoryActivity.class : RSinaWeiboActivity.class;

        Intent intent = new Intent(context, cls);
        intent.putExtra("text", text);
        RFileHelper.deleteExternalShareDirectory(context);
        for (int i = 0; i < photos.size(); i ++) {
            RFileHelper.saveBitmapToExternalSharePath(context, photos.get(i));
        }
        intent.putExtra("type", ShareContentType.Photo);
        context.startActivity(intent);

    }


    /**
     * 分享网页.
     * @param context 上下文.
     * @param webpageUrl 网页链接.
     * @param title 标题.
     * @param description 网页描述.
     * @param thumbImage 缩略图.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareWebpage(@NonNull Context context,
                             @NonNull String webpageUrl,
                             @Nullable String title,
                             @Nullable String description,
                             @Nullable Bitmap thumbImage,
                             @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Sina)) {
            Log.e(TAG, "新浪微博未安装");
            return;
        }
        sdkInitialize(context);
        mListener = listener;
        mContext = context;

        Intent intent = new Intent(context, RSinaWeiboActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("webpage", webpageUrl);
        if (thumbImage != null) {
            RFileHelper.deleteExternalShareDirectory(context);
            RFileHelper.saveBitmapToExternalSharePath(context, thumbImage);
        }
        intent.putExtra("type", ShareContentType.Webpage);
        context.startActivity(intent);

    }

    /**
     * 分享本地视频.
     * @param context 上下文.
     * @param localVideoUrl 本地视频 Uri.
     * @param description 博文, 在开启「分享到微博故事」的功能下会失效.
     * @param isToStory 是否开启「分享到微博故事」.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareLocalVideo(@NonNull Context context,
                                 @NonNull Uri localVideoUrl,
                                 @Nullable String description,
                                 Boolean isToStory,
                                 @Nullable RShareListener listener) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Sina)) {
            Log.e(TAG, "新浪微博未安装");
            return;
        }
        sdkInitialize(context);
        mListener = listener;
        mContext = context;

        Class cls = isToStory ? RSinaWeiboStoryActivity.class : RSinaWeiboActivity.class;

        Intent intent = new Intent(context, cls);
        intent.putExtra("text", description);
        intent.putExtra("local_video_uri", localVideoUrl);
        intent.putExtra("type", ShareContentType.Video);
        context.startActivity(intent);

    }


    protected RShareListener getListener() { return this.mListener; }
    protected Context getContext() { return this.mContext; }



}
