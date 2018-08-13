package me.rex.sdk.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import me.rex.sdk.share.RShare;
import me.rex.sdk.share.RShareListener;
import me.rex.sdk.share.RSharePlatform;
import me.rex.sdk.util.RFileHelper;
import me.rex.sdk.util.RPlatformHelper;


/**
 * 微信 <a href="https://open.weixin.qq.com/">微信开放平台</a>
 */
final public class RWechatManager extends RShare {

    private final String TAG = "RWechatManager=====>";


    private static RShareListener mListener;

    private IWXAPI mIwxapi;

    private RWechatHelper mHelper = new RWechatHelper();


    private static RWechatManager mManager;

    private RWechatManager(){}

    public static RWechatManager getInstance(){
        if (mManager == null) {
            synchronized (RWechatManager.class) {
                if (mManager == null) {
                    mManager = new RWechatManager();
                }
            }
        }
        return mManager;
    }

    /**
     * 初始化 WeChat SDK
     */
    protected void sdkInitialize(Context context) {
        String appId = RPlatformHelper.getWechatAppId(context);
        mIwxapi = WXAPIFactory.createWXAPI(context, appId);
        mIwxapi.registerApp(appId);
    }



    /**
     * 文本分享.
     * @param context 上下文.
     * @param text 分享内容.
     * @param scene 分享场景 {@linkplain TargetScene}.
     * @param listener 分享状态监听.
     */
    public void shareText(@NonNull Context context,
                          @NonNull String text,
                          TargetScene scene,
                          @Nullable RShareListener listener) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WeChat)) {
            Log.e(TAG, "微信未安装");
            return;
        }
        sdkInitialize(context);
        mListener = listener;

        SendMessageToWX.Req req =  mHelper.getTextMessageReq(text, scene);
        mIwxapi.sendReq(req);

    }

    /**
     * 图片分享.
     * @param context 上下文.
     * @param image Bitmap 格式图片.
     * @param scene 分享场景 {@linkplain TargetScene}.
     * @param listener 分享状态监听.
     */
    public void shareImage(@NonNull Context context,
                           @NonNull final Bitmap image,
                           TargetScene scene,
                           @Nullable RShareListener listener) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WeChat)) {
            Log.e(TAG, "微信未安装");
            return;
        }
        sdkInitialize(context);
        mListener = listener;

        SendMessageToWX.Req  req = mHelper.getImgMessageReq(image, scene);
        mIwxapi.sendReq(req);
    }

    /**
     * 网页分享.
     * @param context 上下文.
     * @param webpageUrl 网页链接.
     * @param title 标题.
     * @param descripiton 网页描述.
     * @param thumbImage 对话框中网页的主题图片, 不宜过大.
     * @param scene 分享场景 {@linkplain TargetScene}.
     * @param listener 分享状态监听.
     */
    public void shareWebpage(@NonNull Context context,
                             @NonNull String webpageUrl,
                             @Nullable String title,
                             @Nullable String descripiton,
                             @Nullable Bitmap thumbImage,
                             TargetScene scene,
                             @Nullable RShareListener listener ) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WeChat)) {
            Log.e(TAG, "微信未安装");
            return;
        }
        sdkInitialize(context);
        mListener = listener;

        SendMessageToWX.Req req = mHelper.getWebMessageReq(webpageUrl, title, descripiton,
                thumbImage,
                scene);
        mIwxapi.sendReq(req);

    }


    /**
     * 音乐分享.
     * @param context 上下文.
     * @param musicUrl 音频流链接 (播放流).
     * @param title 音乐链接标题.
     * @param description 音乐链接描述.
     * @param thumbImage 缩略图.
     * @param webpageUrl 音乐网页链接 (网易、QQ 音乐相关网页).
     * @param scene 分享场景 {@linkplain TargetScene}.
     * @param listener 分享状态监听.
     */
    public void shareMusic(@NonNull Context context,
                           @NonNull String musicUrl,
                           @Nullable String title,
                           @Nullable String description,
                           @Nullable Bitmap thumbImage,
                           @NonNull String webpageUrl,
                           TargetScene scene,
                           @Nullable RShareListener listener ) {


        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WeChat)) {
            Log.e(TAG, "微信未安装");
            return;
        }
        sdkInitialize(context);
        mListener = listener;


        SendMessageToWX.Req req = mHelper.getMusicMessageReq(musicUrl, title, description,
                thumbImage, webpageUrl, scene);
        mIwxapi.sendReq(req);

    }

    /**
     * 视频分享.
     * @param context 上下文.
     * @param videoUrl 视频链接.
     * @param title 视频链接标题.
     * @param description 视频链接描述.
     * @param thumbImage 缩略图.
     * @param scene 分享场景 {@linkplain TargetScene}.
     * @param listener 分享状态监听.
     */
    public void shareVideo(@NonNull Context context,
                           @NonNull String videoUrl,
                           @Nullable String title,
                           @Nullable String description,
                           @Nullable Bitmap thumbImage,
                           TargetScene scene,
                           @Nullable RShareListener listener) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WeChat)) {
            Log.e(TAG, "微信未安装");
            return;
        }

        sdkInitialize(context);
        mListener = listener;


        SendMessageToWX.Req req = mHelper.getVideoMessageReq(videoUrl, title, description,
                thumbImage,
                scene);
        mIwxapi.sendReq(req);

    }

    /**
     * 小程序分享.
     * @param context 上下文
     * @param userName 小程序原始 ID.
     * @param path 小程序页面路径.
     * @param type 小程序类型 {@linkplain MiniProgramType}
     * @param webpageUrl 小程序网页.
     * @param title 小程序标题.
     * @param description 小程序描述.
     * @param thumbImage 缩略图.
     * @param scene 分享场景 {@linkplain TargetScene}.
     * @param listener 分享状态监听.
     */
    public void shareMiniProgram(@NonNull Context context,
                                 @NonNull String userName,
                                 @NonNull String path,
                                 @NonNull int type,
                                 @Nullable String webpageUrl,
                                 @Nullable String title,
                                 @Nullable String description,
                                 @Nullable Bitmap thumbImage,
                                 TargetScene scene,
                                 @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WeChat)) {
            Log.e(TAG, "微信未安装");
            return;
        }
        sdkInitialize(context);
        mListener = listener;

        SendMessageToWX.Req req = mHelper.getMiniProgramReq(userName, path, type, webpageUrl,
                title, description, thumbImage, scene);
        mIwxapi.sendReq(req);


    }


    /**
     * 文件分享.
     * @param context 上下文.
     * @param fileUri 文件 Uri.
     * @param title 消息对话框标题.
     * @param thumbImage 缩略图, 只有 Android 发送方能看见.
     * @param scene 分享场景 {@linkplain TargetScene}.
     * @param listener 分享状态监听.
     */
    public void shareFile(@NonNull Context context,
                          @NonNull Uri fileUri,
                          @Nullable String title,
                          @Nullable Bitmap thumbImage,
                          TargetScene scene,
                          @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WeChat)) {
            Log.e(TAG, "微信未安装");
            return;
        }


        sdkInitialize(context);
        mListener = listener;

        String path = RFileHelper.getPath(context, fileUri);
        SendMessageToWX.Req req = mHelper.getFileMessageReq(path, title, thumbImage, scene);
        mIwxapi.sendReq(req);

    }

    protected void unregisterSdk() {
        if (mIwxapi != null) {
            mIwxapi.unregisterApp();
        }
        mListener = null;
    }

    public Boolean handleIntent(IWXAPIEventHandler context, Intent intent) {
        return mIwxapi.handleIntent(intent, context);
    }
    public void onResq(Activity context, BaseResp baseResp) {
        Log.e("WeChat BaseResp====>", baseResp.errCode + "");
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                mListener.onComplete(RSharePlatform.Platform.WeChat);
                break;
            case BaseResp.ErrCode.ERR_BAN:

                /**
                 * 1, 若是小程序分享, 出现该错误有可能是: a. 签名错误; b. 小程序和应用不是同一开放平台的账号.
                 * 2, 若是其他多媒体分享, 出现该错误即是签名错误.
                 * **/
                mListener.onFail(RSharePlatform.Platform.WeChat, "请求被拒绝");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                mListener.onCancel(RSharePlatform.Platform.WeChat);
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                mListener.onFail(RSharePlatform.Platform.WeChat, "发送失败");
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                mListener.onFail(RSharePlatform.Platform.WeChat, "不支持此操作");
                break;
            default: break;

        }
        unregisterSdk();
        context.finish();
    }
    public void onReq(Activity context, BaseReq baseReq) { }

    public enum TargetScene {

        Favorite, /**收藏*/

        Session, /**好友*/

        Timeline /**朋友圈*/
    }

    public class MiniProgramType {
        public static final int RELEASE = 0; /** 发布版本*/
        public static final int TEST = 1; /** 测试版本*/
        public static final int PREVIEW = 2; /** 体验版本*/

    }


}
