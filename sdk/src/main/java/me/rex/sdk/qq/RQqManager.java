package me.rex.sdk.qq;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.facebook.share.Share;

import java.util.ArrayList;

import me.rex.sdk.RShare;
import me.rex.sdk.RShareListener;
import me.rex.sdk.RSharePlatform;
import me.rex.sdk.util.RFileHelper;
import me.rex.sdk.util.RPlatformHelper;
import me.rex.sdk.util.RUtil;


/**
 * QQ <a href=http://wiki.open.qq.com/wiki/mobile/SDK%E4%B8%8B%E8%BD%BD> QQ SDK </a>
 * QQ 分享不能分享纯文字, 目的是为了让用户自行输入「有价值」的信息.
 * */
final public class RQqManager extends RShare {

    private final String TAG = "RQqManager==>";

    private Context mContext;
    private RShareListener mListener;

    private static RQqManager mManager;

    private RQqManager(){}
    public static RQqManager getInstance(){
        if (mManager == null) {
            synchronized (RQqManager.class) {
                if (mManager == null) {
                    mManager = new RQqManager();
                }
            }
        }
        return mManager;
    }



    /**
     * 分享网页到 QQ 客户端.
     * @param context 上下文.
     * @param webpageUrl 网页链接.
     * @param title 网页标题.
     * @param description 网页描述.
     * @param thumbImage 网络链接形式的缩略图, 即图片的 Url.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareWebpage(@NonNull Context context,
                             @NonNull String webpageUrl,
                             @NonNull String title,
                             @Nullable String description,
                             @Nullable String thumbImage,
                             @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.");
            return;
        }

        mContext = context;
        mListener = listener;
        Intent intent = new Intent(context, RQqActivity.class);
        intent.putExtra("webpageUrl", webpageUrl);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("thumbImage", thumbImage);
        intent.putExtra("type", ShareContentType.Webpage);
        context.startActivity(intent);
    }


    /**
     * 分享本地图片到 QQ 客户端.
     * @param context 上下文.
     * @param image Bitmap 格式图片.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareImage(@NonNull Context context,
                           @NonNull Bitmap image,
                           @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.");
            return;
        }

        mContext = context;
        mListener = listener;
        RFileHelper.deleteExternalShareDirectory(context);
        RFileHelper.saveBitmapToExternalSharePath(context, image);
        Intent intent = new Intent(context, RQqActivity.class);
        intent.putExtra("type", ShareContentType.Photo);
        context.startActivity(intent);

    }


    /**
     * 分享音乐, 点击 QQ 消息对话框直接播放对应音频.
     *
     * ⎧‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾⎫
     * |     title       |
     * | ▷   description |  // 点击 ▷ 会在对话框播放音乐; 点击空白处会跳转到 QQ 内置浏览器的对应网页.
     * ⎩_________________⎭>
     *
     * @param context 上下文.
     * @param audioStreamUrl 音乐链接.
     * @param targetUrl 音乐网页.
     * @param title 音乐标题.
     * @param description 音乐描述.
     * @param thumbImage 网络链接形式的缩略图, 即图片的 Url.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     *
     */
    public void shareMusic(@NonNull Context context,
                           @NonNull String audioStreamUrl,
                           @NonNull String targetUrl,
                           @NonNull String title,
                           @Nullable String description,
                           @Nullable String thumbImage,
                           @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.");
            return;
        }

        mContext = context;
        mListener = listener;

        Intent intent = new Intent(context, RQqActivity.class);
        intent.putExtra("musicUrl", audioStreamUrl);
        intent.putExtra("targetUrl", targetUrl);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("thumbImage", thumbImage);
        intent.putExtra("type", ShareContentType.Music);
        context.startActivity(intent);

    }

    /**
     * 分享应用, 点击 QQ 消息对话框会跳到对应的应用(应用宝)界面.
     * @param context 上下文.
     * @param appUrl 应用宝对应的应用链接.️
     * @param title 标题.
     * @param description 应用描述.
     * @param thumbImage 网络链接形式的缩略图, 即图片的 Url.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareApp(@NonNull Context context,
                         @NonNull String appUrl,
                         @NonNull String title,
                         @Nullable String description,
                         @Nullable String thumbImage,
                         @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.");
            return;
        }
        mContext = context;
        mListener = listener;

        Intent intent = new Intent(context, RQqActivity.class);
        intent.putExtra("targetUrl", appUrl);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("thumbImage", thumbImage);
        intent.putExtra("type", ShareContentType.App);
        context.startActivity(intent);
    }


    /**
     * 分享网页到 QQ 空间.
     * @param context 上下文.
     * @param webpageUrl 网页链接.
     * @param title 网页标题.
     * @param description 网页描述.
     * @param images 缩略图网链数组, 虽然是以数组形式作为参数, 但是 QQ SDK 文档有说明只选择第一张作为预览图, 也就是意味着现阶段(2018/5/25
     *               更新的 Adnroid V3.3.3版本) 的 SDK 是不具备多图分享功能的, 真正的多图分享后续腾讯开发团队会补完.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void shareWebpageToZone(@NonNull Context context,
                                   @NonNull String webpageUrl,
                                   @NonNull String title,
                                   @Nullable String description,
                                   @NonNull ArrayList<String> images,
                                   @Nullable RShareListener listener) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.");
            return;
        }
        mContext = context;
        mListener = listener;
        Intent intent = new Intent(context, RQZoneActivity.class);
        intent.putExtra("webpageUrl", webpageUrl);
        intent.putExtra("title", title);
        intent.putExtra("description", description);

        intent.putStringArrayListExtra("images", images);
        intent.putExtra("type", ShareContentType.Webpage);
        context.startActivity(intent);
    }


    /**
     * 分享图片到空间.
     * @param context 上下文.
     * @param localPhotos Bitmap 格式的图片数组.
     * @param description 说说内容, 但这条属性是不会被显示的, QQ Android SDK 有说明在分享纯图的过程中即使附加了说说也会被'过滤'掉,
     *                    目的是让用户自行输入有价值的文字内容.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void publishImagesToZone(@NonNull Context context,
                                    @NonNull ArrayList<Bitmap> localPhotos,
                                    @Nullable String description,
                                    @Nullable RShareListener listener) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.");
            return;
        }

        if (localPhotos.size() > 9) {
            Log.e(TAG, "至多可上传 9 张图片!");
            return;
        }

        mContext = context;
        mListener = listener;
        Intent intent = new Intent(context, RQZoneActivity.class);
        intent.putExtra("description", description);

        RFileHelper.deleteExternalShareDirectory(context);
        for (int i = 0; i < localPhotos.size(); i ++) {
            RFileHelper.saveBitmapToExternalSharePath(context, localPhotos.get(i));
        }

        ArrayList<String> stringPaths = RFileHelper.getExternalSharePathFilePaths(context);

        intent.putStringArrayListExtra("images", stringPaths);
        intent.putExtra("type", ShareContentType.Photo);
        context.startActivity(intent);
    }

    /**
     * 分享本地视频到 QQ 空间.
     * @param context 上下文.
     * @param localVideoUrl 本地视频 Uri.
     * @param description 说说正文, 但是会被过滤掉.
     * @param listener 分享监听回调 {@linkplain RShareListener}.
     */
    public void publishVideoToZone(@NonNull Context context,
                                   @NonNull Uri localVideoUrl,
                                   @Nullable String description,
                                   @Nullable RShareListener listener) {


        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.QQ)) {
            Log.e(TAG, "QQ 客户端未安装.");
            return;
        }
        mContext = context;
        mListener = listener;


        String path = RFileHelper.getPath(context, localVideoUrl);

        Intent intent = new Intent(context, RQZoneActivity.class);
        intent.putExtra("description", description);
        intent.putExtra("type", ShareContentType.Video);
        intent.putExtra("local_video_path", path);
        context.startActivity(intent);
    }



    protected RShareListener getListener () {
        return mListener;
    }
    protected Context getContext() {
        return mContext;
    }



}
