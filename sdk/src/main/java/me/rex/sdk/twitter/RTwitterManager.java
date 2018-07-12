package me.rex.sdk.twitter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;

import me.rex.sdk.RShare;
import me.rex.sdk.RShareListener;
import me.rex.sdk.RSharePlatform;
import me.rex.sdk.util.RFileHelper;
import me.rex.sdk.util.RPlatformHelper;


/**
 * Twitter <a href="https://apps.twitter.com/">Twitter 应用</a>
 * Twitter SDK 将于 2018/10/31 不再更新和提供任何支持, 但是现有的 SDK 不会作废, 需自行维护以及开发
 * <a href="https://blog.twitter.com/developer/en_us/topics/tools/2018/discontinuing-support-for-twitter-kit-sdk.html">Twitter 官方说明</a>
 */
final public class RTwitterManager extends RShare {


    private final String TAG = "RTwitterManager =====>";
    
    private static RTwitterManager mManager;
    private static RShareListener mListener;
    private static Context mContext;

    private RTwitterManager(){}
    public static RTwitterManager getInstance(){
        if (mManager == null) {
            synchronized (RTwitterManager.class) {
                if (mManager == null) {
                    mManager = new RTwitterManager();
                }
            }
        }
        return mManager;
    }


    /**
     * 初始化 Twitter SDK.
     */
    protected void sdkInitialize(Context context) {

        String key = RPlatformHelper.getTwitterConsumerKey(context);
        String secret = RPlatformHelper.getTwitterConsumerSecret(context);
        TwitterConfig config = new TwitterConfig.Builder(context)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(key, secret))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    /**
     * 分享.
     * @param context 上下文.
     * @param webpageUrl 网页链接.
     * @param text 文字描述.
     * @param image 图片.
     * @param hashTag 话题, 仅对跳转应用的方式分享生效⚠️.
     * @param mode 分享模式 {@linkplain RShare.Mode} 设置 Feed、Web 模式都视作应用内分享⚠️.
     * @param listener 分享结果回调, 仅对 Automatic 的分享模式生效, Twitter 并没有提供跳转应用分享的结果处理.
     *
     */
    public void share(final Context context,
                      final String webpageUrl,
                      final String text,
                      final Bitmap image,
                      final String hashTag,
                      RShare.Mode mode,
                      final RShareListener listener) {

        if (!RPlatformHelper.isInstalled(context , RSharePlatform.Platform
                .Twitter)) {
            Log.e(TAG, "Twitter 未安装");
            return;
        }

        if (webpageUrl == null && text == null && image == null) {
            Log.e(TAG, "分享参数中: 网页链接、文字描述以及图片不能同时为空");
            return;
        }
        if (image != null) {
            RFileHelper.deleteExternalShareDirectory(context);
            RFileHelper.saveBitmapToExternalSharePath(context, image);
        }
        /**
         * 应用内构建 Tweet Intent 分享. 这种方法无法分享网页, 只能把网页链接设置在 text 参数里.
         * */
        if (mode == Mode.Feed || mode == Mode.Web || mode == Mode.Automatic) {
            sdkInitialize(context);
            final RTweetComposerIntentHelper helper = new RTweetComposerIntentHelper();
            /**
             * 判断是否和 Twitter 客户端授权过, 未授权的会先进行授权, 否则直接构建 Intent 分享.
             * **/
            mContext = context;
            if (RTwitterAuthHelper.getInstance().hasLogged()) {
                mListener = listener;
                TwitterSession session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();
                Intent intent = helper.getComposerIntent(session, context, text, RFileHelper
                                .getExternalSharePathFileUris(context).get(0),
                        hashTag);
                context.startActivity(intent);

            } else  {
                RTwitterAuthHelper.getInstance().authorizeTwitter(context, new RTwitterAuthCallback() {
                    @Override
                    public void onComplete() {
                        mListener = listener;
                        TwitterSession session = TwitterCore.getInstance().getSessionManager()
                                .getActiveSession();
                        Intent intent = helper.getComposerIntent(session, context, text, RFileHelper
                                .getExternalSharePathFileUris(context).get(0), hashTag); ;
                        context.startActivity(intent);

                    }

                    @Override
                    public void onFail(String errorInfo) {
                        listener.onFail(RSharePlatform.Platform.Twitter, "Twitter 客户端授权失败!");

                    }
                });
            }
            /**
             * 跳转 Twitter 客户端分享, 这种方式分享没有获得 Tweet 发布的状态.
             * **/
        } else {
            RTweetComposerHelper helper = new RTweetComposerHelper();
            URL url;
            Uri imageUri;
            RFileHelper.detectFileUriExposure();
            try {
                url = new URL(webpageUrl);

                imageUri = RFileHelper
                        .getExternalSharePathFileUris(context).get(0);
                TweetComposer.Builder builder = helper.getComposerBuilder(context, text, imageUri,
                        url);
                builder.show();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }

    protected RShareListener getShareListener() { return mListener; }
    protected Context getContext() { return mContext; }


    private class RTweetComposerIntentHelper {

        public Intent getComposerIntent(TwitterSession session,
                                        Context context,
                                        String text,
                                        Uri imageUri,
                                        String hashTag) {
            if (hashTag == null) {
                Intent intent = new ComposerActivity.Builder(context)
                        .session(session)
                        .text(text)
                        .image(imageUri)
                        .createIntent();
                return intent;
            } else {
                Intent intent = new ComposerActivity.Builder(context)
                        .session(session)
                        .text(text)
                        .image(imageUri)
                        .hashtags(hashTag)
                        .createIntent();
                return intent;
            }
        }
    }
    /**
     * 跳转原生应用的 Tweet 内容处理, 返回 TweetComposer.
     */
    private class RTweetComposerHelper {


        public TweetComposer.Builder getComposerBuilder(Context context,
                                                        String text,
                                                        Uri imageUri,
                                                        URL url)
        {
            if (text == null && (imageUri != null || url != null)) {
                if (imageUri == null) {
                    return  new TweetComposer.Builder(context)
                            .url(url);
                } else if (url == null) {
                    return  new TweetComposer.Builder(context)
                            .image(imageUri);
                } else {
                    return  new TweetComposer.Builder(context)
                            .image(imageUri)
                            .url(url);
                }
            } else if (imageUri == null && (text != null || url != null)) {
                if (text == null) {
                    return new TweetComposer.Builder(context)
                            .url(url);
                } else if (url == null) {
                    return new TweetComposer.Builder(context)
                            .text(text);
                } else {
                    return new TweetComposer.Builder(context)
                            .url(url)
                            .text(text);
                }
            } else if (url == null && (imageUri != null || text != null)) {
                if (imageUri == null) {
                    return new TweetComposer.Builder(context)
                            .text(text);
                } else if (text == null) {
                    return new TweetComposer.Builder(context)
                            .image(imageUri);
                } else {
                    return new TweetComposer.Builder(context)
                            .text(text)
                            .image(imageUri);
                }

            } else {
                return new TweetComposer.Builder(context)
                        .text(text)
                        .image(imageUri)
                        .url(url);
            }
        }
    }

}
